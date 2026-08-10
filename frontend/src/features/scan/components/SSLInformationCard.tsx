import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";
import { Badge } from "../../../components/ui/badge";

import type { DomainIntelligence } from "../types/threatIntelligence";

interface Props {
  data: DomainIntelligence;
}

export default function SSLInformationCard({ data }: Props) {
  const hasSsl = data.sslIssuer !== null;

  return (
    <Card>
      <CardHeader>
        <CardTitle>SSL Information</CardTitle>
      </CardHeader>

      <CardContent>
        {!hasSsl ? (
          <p className="text-sm text-muted-foreground">SSL certificate information unavailable.</p>
        ) : (
          <div className="space-y-3 text-sm">
            <div className="flex justify-between">
              <span className="text-muted-foreground">Issuer</span>
              <span className="font-medium">{data.sslIssuer}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-muted-foreground">Valid From</span>
              <span className="font-medium">
                {data.sslValidFrom ? new Date(data.sslValidFrom).toLocaleDateString() : "Unavailable"}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-muted-foreground">Valid Until</span>
              <span className="font-medium">
                {data.sslValidTo ? new Date(data.sslValidTo).toLocaleDateString() : "Unavailable"}
              </span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-muted-foreground">Status</span>
              <Badge variant={data.sslValid ? "success" : "destructive"}>
                {data.sslValid ? "Valid" : "Invalid"}
              </Badge>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
