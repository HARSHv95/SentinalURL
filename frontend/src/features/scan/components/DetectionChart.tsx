import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";
import { Progress } from "../../../components/ui/progress";

import type { AnalysisReport } from "../types/scan";

interface Props {
  analysisReport: AnalysisReport | null;
}

export default function DetectionChart({ analysisReport }: Props) {
  if (!analysisReport) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Detection Summary</CardTitle>
        </CardHeader>

        <CardContent>
          <p className="text-muted-foreground">
            Analysis is still running...
          </p>
        </CardContent>
      </Card>
    );
  }

  const {
    harmless,
    malicious,
    suspicious,
    undetected,
  } = analysisReport;

  const total =
    harmless +
    malicious +
    suspicious +
    undetected;

  const rows = [
    {
      label: "Harmless",
      value: harmless,
      percent: total ? (harmless / total) * 100 : 0,
    },
    {
      label: "Malicious",
      value: malicious,
      percent: total ? (malicious / total) * 100 : 0,
    },
    {
      label: "Suspicious",
      value: suspicious,
      percent: total ? (suspicious / total) * 100 : 0,
    },
    {
      label: "Undetected",
      value: undetected,
      percent: total ? (undetected / total) * 100 : 0,
    },
  ];

  return (
    <Card>
      <CardHeader>
        <CardTitle>Detection Summary</CardTitle>
      </CardHeader>

      <CardContent className="space-y-6">
        {rows.map((row) => (
          <div key={row.label} className="space-y-2">
            <div className="flex justify-between text-sm">
              <span>{row.label}</span>

              <span className="font-semibold">
                {row.value}
              </span>
            </div>

            <Progress value={row.percent} />
          </div>
        ))}
      </CardContent>
    </Card>
  );
}