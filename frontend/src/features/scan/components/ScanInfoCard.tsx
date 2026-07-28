import { Globe, CalendarDays } from "lucide-react";

import {
  Card,
  CardContent,
} from "../../../components/ui/card";

import ScanStatusBadge from "./ScanStatusBadge";

import type { CreateScanResponse } from "../types/scan";

interface Props {
  scan: CreateScanResponse;
}

export default function ScanInfoCard({
  scan,
}: Props) {

  return (

    <Card>

      <CardContent className="space-y-5 p-6">

        <div className="flex items-start justify-between gap-4">

          <div className="flex gap-3">

            <Globe className="mt-1 h-5 w-5 text-muted-foreground" />

            <div>

              <p className="text-sm text-muted-foreground">
                URL
              </p>

              <p className="break-all text-lg font-semibold">
                {scan.url}
              </p>

            </div>

          </div>

          <ScanStatusBadge status={scan.status} />

        </div>

        <div className="flex items-center gap-2 text-sm text-muted-foreground">

          <CalendarDays className="h-4 w-4" />

          <span>

            {new Date(scan.createdAt).toLocaleString()}

          </span>

        </div>

      </CardContent>

    </Card>

  );

}