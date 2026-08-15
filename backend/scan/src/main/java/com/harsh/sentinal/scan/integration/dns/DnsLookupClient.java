package com.harsh.sentinal.scan.integration.dns;

import org.springframework.stereotype.Service;
import org.xbill.DNS.AAAARecord;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DnsLookupClient {

    public Map<String, List<String>> lookupRecords(String hostname) {
        Map<String, List<String>> records = new LinkedHashMap<>();

        records.put("A", lookup(hostname, Type.A));
        records.put("AAAA", lookup(hostname, Type.AAAA));
        records.put("MX", lookup(hostname, Type.MX));
        records.put("NS", lookup(hostname, Type.NS));
        records.put("CNAME", lookup(hostname, Type.CNAME));

        records.values().removeIf(List::isEmpty);
        return records;
    }

    private List<String> lookup(String hostname, int type) {
        List<String> results = new ArrayList<>();
        try {
            Lookup lookup = new Lookup(hostname, type);
            Record[] answers = lookup.run();

            if (lookup.getResult() == Lookup.SUCCESSFUL && answers != null) {
                for (Record record : answers) {
                    results.add(formatRecord(record));
                }
            }
        } catch (Exception e) {
            // best-effort — leave this record type empty rather than failing the whole lookup
        }
        return results;
    }

    private String formatRecord(Record record) {
        return switch (record) {
            case ARecord a -> a.getAddress().getHostAddress();
            case AAAARecord aaaa -> aaaa.getAddress().getHostAddress();
            case MXRecord mx -> mx.getTarget().toString(true) + " (priority " + mx.getPriority() + ")";
            case NSRecord ns -> ns.getTarget().toString(true);
            case CNAMERecord cname -> cname.getTarget().toString(true);
            default -> record.rdataToString();
        };
    }
}
