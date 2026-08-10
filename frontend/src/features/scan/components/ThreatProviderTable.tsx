import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";

import ProviderStatusBadge from "./ProviderStatusBadge";
import ThreatIndicatorBadge from "./ThreatIndicatorBadge";

import type { ThreatProviderResult } from "../types/threatIntelligence";

interface Props {
  results: ThreatProviderResult[];
}

function formatProviderName(name: string) {
  return name
    .replace(/_/g, " ")
    .toLowerCase()
    .replace(/\b\w/g, (c) => c.toUpperCase());
}

function detectionSummary(result: ThreatProviderResult) {
  if (result.maliciousEngineCount !== null) {
    const total =
      (result.harmlessEngineCount ?? 0) +
      (result.maliciousEngineCount ?? 0) +
      (result.suspiciousEngineCount ?? 0) +
      (result.undetectedEngineCount ?? 0);
    return `${result.maliciousEngineCount} / ${total} engines`;
  }
  if (result.status !== "AVAILABLE") {
    return "—";
  }
  return result.malicious ? "Flagged" : "Clean";
}

export default function ThreatProviderTable({ results }: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Threat Intelligence</CardTitle>
      </CardHeader>

      <CardContent className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b text-left text-muted-foreground">
              <th className="py-2 pr-4 font-medium">Provider</th>
              <th className="py-2 pr-4 font-medium">Status</th>
              <th className="py-2 pr-4 font-medium">Verdict</th>
              <th className="py-2 font-medium">Detections</th>
            </tr>
          </thead>
          <tbody>
            {results.map((result) => (
              <tr key={result.providerName} className="border-b last:border-0">
                <td className="py-2.5 pr-4 font-medium">
                  {formatProviderName(result.providerName)}
                </td>
                <td className="py-2.5 pr-4">
                  <ProviderStatusBadge status={result.status} />
                </td>
                <td className="py-2.5 pr-4">
                  {result.status === "AVAILABLE" ? (
                    <ThreatIndicatorBadge malicious={result.malicious} suspicious={result.suspicious} />
                  ) : (
                    <span className="text-muted-foreground">—</span>
                  )}
                </td>
                <td className="py-2.5 text-muted-foreground">
                  {detectionSummary(result)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </CardContent>
    </Card>
  );
}
