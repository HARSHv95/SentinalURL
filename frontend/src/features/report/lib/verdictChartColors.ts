import type { RiskVerdict } from "../../scan/types/scan";

interface ChartColor {
  light: string;
  dark: string;
}

/**
 * Validated categorical order (dataviz skill, palette.md slots 1-5) —
 * passes adjacent-pair CVD/contrast checks in both modes. Do not reorder;
 * reordering requires re-running scripts/validate_palette.js.
 */
export const VERDICT_CHART_COLORS: Record<RiskVerdict, ChartColor> = {
  SAFE: { light: "#2a78d6", dark: "#3987e5" },
  LOW_RISK: { light: "#eb6834", dark: "#d95926" },
  MEDIUM_RISK: { light: "#1baf7a", dark: "#199e70" },
  HIGH_RISK: { light: "#eda100", dark: "#c98500" },
  CRITICAL: { light: "#e87ba4", dark: "#d55181" },
};

export const SEQUENTIAL_CHART_COLOR = "#2a78d6";
