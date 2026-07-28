import { Globe } from "lucide-react";

import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";
import { Badge } from "../../../components/ui/badge";

import EmptyState from "../../../shared/components/EmptyState";

import type { TopRiskyDomain } from "../types/report";

interface Props {
  data: TopRiskyDomain[];
}

function badgeVariantForScore(score: number) {
  if (score <= 10) return "success" as const;
  if (score <= 30) return "secondary" as const;
  if (score <= 60) return "warning" as const;
  return "destructive" as const;
}

export default function TopRiskyDomainsList({ data }: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Top Risky Domains</CardTitle>
      </CardHeader>

      <CardContent>
        {data.length === 0 ? (
          <EmptyState
            icon={Globe}
            title="No risky domains yet"
            description="Domains with elevated risk scores will show up here."
          />
        ) : (
          <ul className="divide-y">
            {data.map((domain) => (
              <li
                key={domain.domain}
                className="flex items-center justify-between gap-3 py-3 first:pt-0 last:pb-0"
              >
                <div className="flex min-w-0 items-center gap-2">
                  <Globe className="size-4 shrink-0 text-muted-foreground" />
                  <span className="truncate text-sm font-medium">
                    {domain.domain}
                  </span>
                </div>

                <div className="flex shrink-0 items-center gap-3 text-sm text-muted-foreground">
                  <span>
                    {domain.scanCount} scan{domain.scanCount === 1 ? "" : "s"}
                  </span>
                  <Badge variant={badgeVariantForScore(domain.maxRiskScore)}>
                    Max {domain.maxRiskScore}
                  </Badge>
                </div>
              </li>
            ))}
          </ul>
        )}
      </CardContent>
    </Card>
  );
}
