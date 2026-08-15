package com.harsh.sentinal.scan.integration.whois;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Raw WHOIS protocol (RFC 3912, TCP port 43) — there's no clean free JSON
 * WHOIS API, so this queries IANA's bootstrap WHOIS server and follows its
 * "refer:" pointer to the authoritative registry server for the TLD. The
 * target servers here are always IANA/registries, never user-controlled —
 * the SsrfGuard is not needed on this path (see its own javadoc).
 */
@Service
public class WhoisClient {

    private static final String IANA_WHOIS_SERVER = "whois.iana.org";
    private static final int WHOIS_PORT = 43;
    private static final int TIMEOUT_MS = 5000;

    public String lookup(String domain) {
        try {
            String ianaResponse = query(IANA_WHOIS_SERVER, domain);
            String referredServer = extractReferral(ianaResponse);

            if (referredServer != null && !referredServer.isBlank()) {
                String referredResponse = query(referredServer, domain);
                return referredResponse != null ? referredResponse : ianaResponse;
            }

            return ianaResponse;
        } catch (Exception e) {
            return null;
        }
    }

    private String query(String server, String domain) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(server, WHOIS_PORT), TIMEOUT_MS);
            socket.setSoTimeout(TIMEOUT_MS);

            try (OutputStream out = socket.getOutputStream();
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

                out.write((domain + "\r\n").getBytes(StandardCharsets.UTF_8));
                out.flush();

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line).append('\n');
                }
                return response.toString();
            }
        } catch (IOException e) {
            return null;
        }
    }

    private String extractReferral(String response) {
        if (response == null) {
            return null;
        }
        for (String line : response.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.toLowerCase().startsWith("refer:")) {
                return trimmed.substring("refer:".length()).trim();
            }
        }
        return null;
    }
}
