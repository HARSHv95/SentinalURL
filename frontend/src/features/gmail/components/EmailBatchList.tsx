import { useNavigate } from "react-router-dom";
import { Inbox } from "lucide-react";

import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";

import { useEmailBatches } from "../hooks/useEmailBatches";
import { ROUTES } from "../../../shared/lib/routes";

export default function EmailBatchList() {
  const navigate = useNavigate();
  const { data, isLoading } = useEmailBatches(0, 10);

  const batches = data?.content ?? [];

  return (
    <Card>
      <CardHeader>
        <CardTitle>Recent Email Scans</CardTitle>
      </CardHeader>

      <CardContent>
        {isLoading ? (
          <p className="text-sm text-muted-foreground">Loading...</p>
        ) : batches.length === 0 ? (
          <div className="flex flex-col items-center gap-2 py-8 text-center text-muted-foreground">
            <Inbox className="size-6" />
            <p className="text-sm">No emails scanned yet.</p>
          </div>
        ) : (
          <div className="divide-y">
            {batches.map((batch) => (
              <button
                key={batch.id}
                className="flex w-full items-center justify-between gap-3 py-3 text-left hover:bg-muted/50"
                onClick={() =>
                  navigate(`${ROUTES.HISTORY}?emailScanBatchId=${batch.id}`)
                }
              >
                <div className="min-w-0">
                  <p className="truncate text-sm font-medium">
                    {batch.subject ?? "(no subject)"}
                  </p>
                  <p className="truncate text-xs text-muted-foreground">
                    {batch.senderPreview ?? "Unknown sender"}
                  </p>
                </div>

                <div className="shrink-0 text-right text-xs text-muted-foreground">
                  <p>{batch.urlCount} URL{batch.urlCount === 1 ? "" : "s"}</p>
                  <p>{new Date(batch.createdAt).toLocaleDateString()}</p>
                </div>
              </button>
            ))}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
