import { useNavigate } from "react-router-dom";
import { Globe } from "lucide-react";

import { Card, CardContent } from "../../../components/ui/card";
import { Badge } from "../../../components/ui/badge";

import ScanStatusBadge from "./ScanStatusBadge";

import { VERDICT_META } from "../lib/verdict";

import type { ScanSummary } from "../types/scan";

interface Props {
  scan: ScanSummary;
}

export default function ScanCard({ scan }: Props) {

  const navigate = useNavigate();

  return (
    <Card
      className="cursor-pointer transition hover:shadow-md"
      onClick={() => navigate(`/scan/${scan.scanId}`)}
    >
      <CardContent className="space-y-3 p-5">

        <div className="flex items-start justify-between gap-3">

          <div className="flex min-w-0 items-center gap-2">
            <Globe className="size-4 shrink-0 text-muted-foreground" />
            <h3 className="truncate font-semibold">
              {scan.url}
            </h3>
          </div>

          <div className="flex shrink-0 items-center gap-2">
            {scan.verdict && (
              <Badge variant={VERDICT_META[scan.verdict].badgeVariant}>
                {VERDICT_META[scan.verdict].label}
              </Badge>
            )}
            <ScanStatusBadge status={scan.scanStatus} />
          </div>

        </div>

        <div className="flex justify-between text-sm text-muted-foreground">

          <span>
            Risk Score: {scan.riskScore}
          </span>

          <span>
            {new Date(scan.createdAt).toLocaleString()}
          </span>

        </div>

      </CardContent>
    </Card>
  );

}
