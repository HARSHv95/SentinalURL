import type { VariantProps } from "class-variance-authority";

import { Badge, badgeVariants } from "../../../components/ui/badge";

import type { ProviderStatus } from "../types/threatIntelligence";

interface Props {
  status: ProviderStatus;
}

type BadgeVariant = VariantProps<typeof badgeVariants>["variant"];

const STATUS_META: Record<ProviderStatus, { label: string; variant: BadgeVariant }> = {
  AVAILABLE: { label: "Available", variant: "success" },
  UNAVAILABLE: { label: "Unavailable", variant: "secondary" },
  ERROR: { label: "Error", variant: "destructive" },
  TIMEOUT: { label: "Timeout", variant: "warning" },
};

export default function ProviderStatusBadge({ status }: Props) {
  const meta = STATUS_META[status];

  return <Badge variant={meta.variant}>{meta.label}</Badge>;
}
