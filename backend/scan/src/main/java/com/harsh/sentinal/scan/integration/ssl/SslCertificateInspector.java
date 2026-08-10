package com.harsh.sentinal.scan.integration.ssl;

import com.harsh.sentinal.scan.util.SsrfGuard;
import org.springframework.stereotype.Service;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.List;

/**
 * The one component that connects directly to the scanned domain itself —
 * resolves through SsrfGuard first, then connects to the validated IP
 * directly (never re-resolving the hostname), with SNI set explicitly so
 * virtual-hosted HTTPS servers still complete the handshake correctly.
 */
@Service
public class SslCertificateInspector {

    private static final int HTTPS_PORT = 443;
    private static final int TIMEOUT_MS = 5000;

    public record SslInfo(String issuer, Instant validFrom, Instant validTo, boolean valid) {}

    public SslInfo inspect(String hostname) {
        try {
            InetAddress safeAddress = SsrfGuard.resolveSafeAddress(hostname);

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            try (SSLSocket socket = (SSLSocket) factory.createSocket()) {
                socket.connect(new InetSocketAddress(safeAddress, HTTPS_PORT), TIMEOUT_MS);
                socket.setSoTimeout(TIMEOUT_MS);

                SSLParameters params = socket.getSSLParameters();
                params.setServerNames(List.of(new SNIHostName(hostname)));
                socket.setSSLParameters(params);

                socket.startHandshake();

                X509Certificate cert = (X509Certificate) socket.getSession().getPeerCertificates()[0];

                Instant notBefore = cert.getNotBefore().toInstant();
                Instant notAfter = cert.getNotAfter().toInstant();
                boolean valid = Instant.now().isAfter(notBefore) && Instant.now().isBefore(notAfter);

                return new SslInfo(cert.getIssuerX500Principal().getName(), notBefore, notAfter, valid);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
