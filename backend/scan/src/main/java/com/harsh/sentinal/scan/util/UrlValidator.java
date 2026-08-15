package com.harsh.sentinal.scan.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;

public class UrlValidator implements ConstraintValidator<com.harsh.sentinal.scan.util.ValidUrl, String> {

    @Override
    public boolean isValid(String url,
                           ConstraintValidatorContext context) {

        if (url == null || url.isBlank()) {
            return false;
        }

        if (!url.startsWith("http://")
                && !url.startsWith("https://")) {

            url = "https://" + url;
        }

        try {

            URI uri = new URI(url);

            String host = uri.getHost();

            if (host == null || !host.contains(".")) {
                return false;
            }

            return uri.getScheme().equalsIgnoreCase("http")
                    || uri.getScheme().equalsIgnoreCase("https");

        } catch (Exception e) {
            return false;
        }
    }
}