import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";

import type { RiskFactor } from "../types/threatIntelligence";

interface Props {
  factors: RiskFactor[];
}

export default function RiskBreakdownCard({ factors }: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Risk Breakdown</CardTitle>
      </CardHeader>

      <CardContent className="space-y-2">
        {factors.length === 0 ? (
          <p className="text-sm text-muted-foreground">No contributing risk factors.</p>
        ) : (
          factors.map((factor, index) => (
            <div
              key={index}
              className="flex items-center justify-between rounded-lg border px-3 py-2 text-sm"
            >
              <span>{factor.label}</span>
              <span className="font-semibold text-destructive">+{factor.contribution}</span>
            </div>
          ))
        )}
      </CardContent>
    </Card>
  );
}
