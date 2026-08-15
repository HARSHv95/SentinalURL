package com.harsh.sentinal.scan.dto;

import com.harsh.sentinal.scan.util.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ScanRequest (
        @NotBlank(message = "Url is required!!")
        @ValidUrl
        @URL(message = "Invalid URL")
        String url
){}
