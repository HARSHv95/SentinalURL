import type { RiskVerdict } from "../types/scan";

interface VerdictMeta {
  label: string;
  badgeVariant: "success" | "secondary" | "warning" | "destructive";
}

export const VERDICT_META: Record<RiskVerdict, VerdictMeta> = {
  SAFE: { label: "Safe", badgeVariant: "success" },
  LOW_RISK: { label: "Low Risk", badgeVariant: "secondary" },
  MEDIUM_RISK: { label: "Medium Risk", badgeVariant: "warning" },
  HIGH_RISK: { label: "High Risk", badgeVariant: "warning" },
  CRITICAL: { label: "Critical", badgeVariant: "destructive" },
};
