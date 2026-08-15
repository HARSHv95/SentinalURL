package com.harsh.sentinal.scan.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Guards outbound connections that this application makes directly to a
 * user-submitted host (currently: the SSL certificate inspector). DNS/WHOIS/
 * geo-IP lookups go to fixed, trusted provider servers and don't need this —
 * this exists specifically for the one place we open a socket to whatever
 * domain a user asked us to scan.
 *
 * Resolves the hostname once, validates every resolved address, and returns
 * one validated address for the caller to connect to directly — callers must
 * NOT let the underlying socket re-resolve the hostname itself, or the
 * validated-then-connect check can be bypassed by a DNS answer that changes
 * between resolution and connection.
 */
public final class SsrfGuard {

    private SsrfGuard() {}

    public static InetAddress resolveSafeAddress(String hostname) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(hostname);

        for (InetAddress address : addresses) {
            if (!isSafe(address)) {
                throw new SsrfViolationException(
                        "Refusing to connect to " + hostname + " — resolved to unsafe address " + address.getHostAddress()
                );
            }
        }

        return addresses[0];
    }

    public static boolean isSafe(InetAddress address) {
        if (address.isLoopbackAddress()
                || address.isLinkLocalAddress()   // covers 169.254.0.0/16, including the 169.254.169.254 cloud-metadata address
                || address.isSiteLocalAddress()   // covers RFC1918 (10/8, 172.16/12, 192.168/16)
                || address.isMulticastAddress()
                || address.isAnyLocalAddress()) {
            return false;
        }

        return !isIPv6UniqueLocal(address);
    }

    /**
     * Java's isSiteLocalAddress() only recognizes the deprecated IPv6
     * site-local range (fec0::/10), not the modern unique-local range
     * (fc00::/7) that private IPv6 networks actually use today.
     */
    private static boolean isIPv6UniqueLocal(InetAddress address) {
        if (!(address instanceof Inet6Address)) {
            return false;
        }
        byte[] bytes = address.getAddress();
        return (bytes[0] & 0xFE) == 0xFC;
    }
}
