import { Loader2, ShieldQuestion } from "lucide-react";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../../../components/ui/card";

import { Progress } from "../../../components/ui/progress";

export default function PendingAnalysisCard() {
  return (
    <Card>

      <CardHeader>

        <CardTitle className="flex items-center gap-2">

          <ShieldQuestion className="size-5 text-primary" />

          Analyzing URL

        </CardTitle>

      </CardHeader>

      <CardContent className="space-y-6">

        <div className="flex items-center gap-2 text-muted-foreground">

          <Loader2 className="size-4 animate-spin" />

          <span>Contacting VirusTotal...</span>

        </div>

        <Progress value={65} className="animate-pulse" />

        <p className="text-sm text-muted-foreground">
          This usually takes a few seconds. The report will
          update automatically once analysis is complete.
        </p>

      </CardContent>

    </Card>
  );
}