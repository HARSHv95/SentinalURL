package com.harsh.sentinal.scan.dto;

public record SubmitUrlResponse(
        Data data
) {

    public record Data(
            String id,
            String type
    ) {}

}