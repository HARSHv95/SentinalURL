import type { VariantProps } from "class-variance-authority";

import { Badge, badgeVariants } from "../../../components/ui/badge";

import type { AlertSeverity } from "../types/watchlist";

interface Props {
  severity: AlertSeverity;
}

type BadgeVariant = VariantProps<typeof badgeVariants>["variant"];

const SEVERITY_META: Record<AlertSeverity, { label: string; variant: BadgeVariant }> = {
  INFO: { label: "Info", variant: "outline" },
  WARNING: { label: "Warning", variant: "warning" },
  CRITICAL: { label: "Critical", variant: "destructive" },
};

export default function WatchlistStatusBadge({ severity }: Props) {
  const meta = SEVERITY_META[severity];

  return <Badge variant={meta.variant}>{meta.label}</Badge>;
}
