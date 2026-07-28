import type { VariantProps } from "class-variance-authority";

import { Badge, badgeVariants } from "../../../components/ui/badge";

import type { ScanStatus } from "../types/scan";

interface Props {
  status: ScanStatus;
}

type BadgeVariant = VariantProps<typeof badgeVariants>["variant"];

const STATUS_META: Record<ScanStatus, { label: string; variant: BadgeVariant }> = {
  PENDING: { label: "Pending", variant: "warning" },
  IN_PROGRESS: { label: "In Progress", variant: "secondary" },
  COMPLETED: { label: "Completed", variant: "success" },
  FAILED: { label: "Failed", variant: "destructive" },
};

export default function ScanStatusBadge({ status }: Props) {

  const meta = STATUS_META[status];

  return (
    <Badge variant={meta.variant}>
      {meta.label}
    </Badge>
  );

}
