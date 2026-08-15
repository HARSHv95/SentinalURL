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

  let Icon = ShieldCheck;
  let title = "Safe";
  let description =
    "No security vendors flagged this URL as malicious.";
  let textColor = "text-green-600";

  switch (verdict) {
    case "LOW_RISK":
      Icon = ShieldAlert;
      title = "Low Risk";
      description =
        "The URL appears mostly safe but should be used with caution.";
      textColor = "text-blue-600";
      break;

    case "MEDIUM_RISK":
      Icon = ShieldAlert;
      title = "Medium Risk";
      description =
        "Some security concerns were detected.";
      textColor = "text-yellow-600";
      break;

    case "HIGH_RISK":
      Icon = ShieldX;
      title = "High Risk";
      description =
        "Multiple vendors reported suspicious activity.";
      textColor = "text-orange-600";
      break;

    case "CRITICAL":
      Icon = ShieldX;
      title = "Critical Risk";
      description =
        "This URL is considered malicious and should be avoided.";
      textColor = "text-red-600";
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