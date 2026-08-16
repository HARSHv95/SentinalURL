import { useState } from "react";
import { Check, Copy, Share2 } from "lucide-react";

import { Button } from "../../../components/ui/button";
import { Input } from "../../../components/ui/input";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../../../components/ui/dialog";

import { useShareScan } from "../hooks/useShareScan";
import { useUnshareScan } from "../hooks/useUnshareScan";
import { ROUTES } from "../../../shared/lib/routes";

interface Props {
  scanId: string;
  shared: boolean;
  shareToken: string | null;
}

export default function ShareReportDialog({ scanId, shared, shareToken }: Props) {
  const shareScan = useShareScan(scanId);
  const unshareScan = useUnshareScan(scanId);
  const [copied, setCopied] = useState(false);

  const shareUrl = shareToken
    ? `${window.location.origin}${ROUTES.SHARED_REPORT.replace(":shareToken", shareToken)}`
    : null;

  const handleCopy = async () => {
    if (!shareUrl) return;
    await navigator.clipboard.writeText(shareUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <Dialog>
      <DialogTrigger render={<Button variant="outline" />}>
        <Share2 />
        Share
      </DialogTrigger>

      <DialogContent>
        <DialogHeader>
          <DialogTitle>Share this report</DialogTitle>
          <DialogDescription>
            {shared
              ? "Anyone with this link can view a read-only copy of this report — no login required."
              : "Make this report public to generate a link anyone can use to view it, without logging in."}
          </DialogDescription>
        </DialogHeader>

        {shared && shareUrl ? (
          <div className="flex gap-2">
            <Input readOnly value={shareUrl} onFocus={(e) => e.target.select()} />
            <Button variant="outline" size="icon" onClick={handleCopy} aria-label="Copy link">
              {copied ? <Check /> : <Copy />}
            </Button>
          </div>
        ) : (
          <Button
            disabled={shareScan.isPending}
            onClick={() => shareScan.mutate()}
          >
            {shareScan.isPending ? "Generating link..." : "Make Public"}
          </Button>
        )}

        <DialogFooter>
          {shared && (
            <Button
              variant="destructive"
              disabled={unshareScan.isPending}
              onClick={() => unshareScan.mutate()}
            >
              {unshareScan.isPending ? "Stopping..." : "Stop Sharing"}
            </Button>
          )}
          <DialogClose render={<Button variant="outline" />}>
            Close
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
