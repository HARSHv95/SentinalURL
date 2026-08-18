import { Card, CardContent } from "../../../components/ui/card";
import { Skeleton } from "../../../components/ui/skeleton";

export default function ScanReportSkeleton() {
  return (
    <div className="space-y-6">
      <Card>
        <CardContent className="space-y-5 p-5">
          <div className="flex items-start justify-between gap-4">
            <div className="flex gap-3">
              <Skeleton className="size-5 rounded-full" />
              <div className="space-y-2">
                <Skeleton className="h-3 w-10" />
                <Skeleton className="h-5 w-64" />
              </div>
            </div>
            <Skeleton className="h-5 w-20 rounded-full" />
          </div>
          <Skeleton className="h-4 w-40" />
        </CardContent>
      </Card>

      <Skeleton className="h-40 rounded-xl" />
      <Skeleton className="h-64 rounded-xl" />
    </div>
  );
}
