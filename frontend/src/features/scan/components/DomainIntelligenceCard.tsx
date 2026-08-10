import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";

import type { DomainIntelligence } from "../types/threatIntelligence";

interface Props {
  data: DomainIntelligence;
}

function formatDate(value: string | null) {
  return value ? new Date(value).toLocaleDateString() : "Unavailable";
}

export default function DomainIntelligenceCard({ data }: Props) {
  const items = [
    { label: "Domain", value: data.domain },
    { label: "Domain Age", value: data.domainAgeDays !== null ? `${data.domainAgeDays} days` : "Unavailable" },
    { label: "Registrar", value: data.registrar ?? "Unavailable" },
    { label: "Created", value: formatDate(data.creationDate) },
    { label: "Expires", value: formatDate(data.expirationDate) },
    { label: "Country", value: data.country ?? "Unavailable" },
    { label: "Hosting Provider", value: data.hostingProvider ?? "Unavailable" },
    { label: "ASN", value: data.asn ?? "Unavailable" },
  ];

  return (
    <Card>
      <CardHeader>
        <CardTitle>Domain Intelligence</CardTitle>
      </CardHeader>

      <CardContent>
        <dl className="grid grid-cols-2 gap-x-6 gap-y-4 sm:grid-cols-4">
          {items.map((item) => (
            <div key={item.label}>
              <dt className="text-xs text-muted-foreground">{item.label}</dt>
              <dd className="mt-1 truncate text-sm font-medium">{item.value}</dd>
            </div>
          ))}
        </dl>
      </CardContent>
    </Card>
  );
}
