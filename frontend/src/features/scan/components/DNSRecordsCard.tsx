import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";

import type { DnsRecords } from "../types/threatIntelligence";

interface Props {
  records: DnsRecords | null;
}

const RECORD_LABELS: { key: keyof DnsRecords; label: string }[] = [
  { key: "A", label: "A" },
  { key: "AAAA", label: "AAAA" },
  { key: "MX", label: "MX" },
  { key: "NS", label: "NS" },
  { key: "CNAME", label: "CNAME" },
];

export default function DNSRecordsCard({ records }: Props) {
  const hasAny = records != null && Object.values(records).some((v) => v && v.length > 0);

  return (
    <Card>
      <CardHeader>
        <CardTitle>DNS Information</CardTitle>
      </CardHeader>

      <CardContent className="space-y-4">
        {!hasAny ? (
          <p className="text-sm text-muted-foreground">No DNS records available.</p>
        ) : (
          RECORD_LABELS.map(({ key, label }) => {
            const values = records?.[key];
            if (!values || values.length === 0) {
              return null;
            }
            return (
              <div key={key}>
                <p className="text-xs font-semibold text-muted-foreground">{label}</p>
                <ul className="mt-1 space-y-1">
                  {values.map((value) => (
                    <li key={value} className="font-mono text-sm">
                      {value}
                    </li>
                  ))}
                </ul>
              </div>
            );
          })
        )}
      </CardContent>
    </Card>
  );
}
