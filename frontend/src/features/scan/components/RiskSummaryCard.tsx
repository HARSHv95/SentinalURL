import {
  Card,
  CardContent,
} from "../../../components/ui/card";

import type { RiskReport } from "../types/scan";

interface Props {
  riskReport: RiskReport | null;

  status: string;
}

export default function RiskSummaryCard({

  riskReport,

  status,

}: Props) {

  const items = [

    {
      label: "Risk Score",

      value: riskReport?.riskScore ?? "-",
    },

    {
      label: "Verdict",

      value: riskReport?.verdict ?? "-",
    },

    {
      label: "Confidence",

      value: riskReport
        ? `${riskReport.confidence}%`
        : "-",
    },

    {
      label: "Status",

      value: status,
    },

  ];

  return (

    <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-4">

      {items.map((item) => (

        <Card key={item.label}>

          <CardContent className="space-y-2 p-6">

            <p className="text-sm text-muted-foreground">

              {item.label}

            </p>

            <p className="text-2xl font-bold">

              {item.value}

            </p>

          </CardContent>

        </Card>

      ))}

    </div>

  );

}