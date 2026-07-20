package com.harsh.sentinal.scan.integration.virustotal;

public record VirusTotalResponse(

        Data data

) {

    public record Data(

            Attributes attributes

    ) {}

    public record Attributes(

            String status

    ) {}
}
