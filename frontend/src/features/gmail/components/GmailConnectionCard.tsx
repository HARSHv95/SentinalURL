import { Mail } from "lucide-react";

import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";
import { Badge } from "../../../components/ui/badge";
import { Button } from "../../../components/ui/button";

import { useGmailStatus } from "../hooks/useGmailStatus";
import { useGmailAuthorizeUrl } from "../hooks/useGmailAuthorizeUrl";
import { useDisconnectGmail } from "../hooks/useDisconnectGmail";
import { useSyncGmailNow } from "../hooks/useSyncGmailNow";

export default function GmailConnectionCard() {
  const { data: status, isLoading } = useGmailStatus();
  const authorizeUrl = useGmailAuthorizeUrl();
  const disconnect = useDisconnectGmail();
  const syncNow = useSyncGmailNow();

  const connected = Boolean(status?.connected);

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Mail className="size-4 text-muted-foreground" />
            <CardTitle>Gmail</CardTitle>
          </div>

          <Badge variant={connected ? "success" : "secondary"}>
            {connected ? "Connected" : "Not connected"}
          </Badge>
        </div>
      </CardHeader>

      <CardContent className="space-y-3">
        <p className="text-sm text-muted-foreground">
          Automatically scan URLs found in your incoming Gmail messages.
        </p>

        {!isLoading && connected && (
          <p className="text-sm text-muted-foreground">
            {status?.lastSyncedAt
              ? `Last synced ${new Date(status.lastSyncedAt).toLocaleString()}`
              : "Not yet synced"}
          </p>
        )}

        {status?.lastSyncError && (
          <p className="text-sm text-destructive">
            Last sync failed: {status.lastSyncError}
          </p>
        )}

        <div className="flex gap-2">
          {connected ? (
            <>
              <Button
                variant="outline"
                disabled={syncNow.isPending}
                onClick={() => syncNow.mutate()}
              >
                {syncNow.isPending ? "Syncing..." : "Sync now"}
              </Button>

              <Button
                variant="destructive"
                disabled={disconnect.isPending}
                onClick={() => disconnect.mutate()}
              >
                {disconnect.isPending ? "Disconnecting..." : "Disconnect"}
              </Button>
            </>
          ) : (
            <Button
              disabled={authorizeUrl.isPending}
              onClick={() => authorizeUrl.mutate()}
            >
              {authorizeUrl.isPending ? "Redirecting..." : "Connect Gmail"}
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
