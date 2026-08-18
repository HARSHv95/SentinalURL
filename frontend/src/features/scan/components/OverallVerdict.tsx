import {
  ShieldCheck,
  ShieldAlert,
  ShieldX,
} from "lucide-react";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../../components/ui/card";

import type { RiskReport } from "../types/scan";

interface Props {
  riskReport: RiskReport | null;
}

export default function OverallVerdict({
  riskReport,
}: Props) {
  if (!riskReport) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Overall Verdict</CardTitle>
        </CardHeader>

        <CardContent>
          <p className="text-muted-foreground">
            Waiting for analysis...
          </p>
        </CardContent>
      </Card>
    );
  }

  const { verdict } = riskReport;

  // Reuses the same success/secondary/warning/destructive tones as
  // VERDICT_META (lib/verdict.ts) and the Badge variants, instead of a
  // separate ad hoc 5-color scale — keeps every verdict color in the app
  // (badges, charts, this card) drawn from the same visual language.
  let Icon = ShieldCheck;
  let title = "Safe";
  let description =
    "No security vendors flagged this URL as malicious.";
  let textColor = "text-green-600 dark:text-green-400";

  switch (verdict) {
    case "LOW_RISK":
      Icon = ShieldAlert;
      title = "Low Risk";
      description =
        "The URL appears mostly safe but should be used with caution.";
      textColor = "text-muted-foreground";
      break;

    case "MEDIUM_RISK":
      Icon = ShieldAlert;
      title = "Medium Risk";
      description =
        "Some security concerns were detected.";
      textColor = "text-yellow-600 dark:text-yellow-400";
      break;

    case "HIGH_RISK":
      Icon = ShieldX;
      title = "High Risk";
      description =
        "Multiple vendors reported suspicious activity.";
      textColor = "text-yellow-600 dark:text-yellow-400";
      break;

    case "CRITICAL":
      Icon = ShieldX;
      title = "Critical Risk";
      description =
        "This URL is considered malicious and should be avoided.";
      textColor = "text-destructive";
      break;

    case "SAFE":
    default:
      break;
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Overall Verdict</CardTitle>
      </CardHeader>

      <CardContent className="flex items-start gap-4">
        <Icon className={`h-10 w-10 ${textColor}`} />

        <div>
          <h3 className={`text-xl font-bold ${textColor}`}>
            {title}
          </h3>

          <p className="mt-2 text-muted-foreground">
            {description}
          </p>
        </div>
      </CardContent>
    </Card>
  );
}