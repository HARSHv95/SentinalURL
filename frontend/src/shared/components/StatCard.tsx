import type { ReactNode } from "react";

import { cn } from "../../lib/utils";

type StatCardTone = "primary" | "success" | "warning" | "destructive";

interface StatCardProps {
  title: string;
  value: number | string;
  description?: string;
  icon: ReactNode;
  tone?: StatCardTone;
}

const TONE_STYLES: Record<StatCardTone, { icon: string; glow: string }> = {
  primary: {
    icon: "bg-primary/10 text-primary",
    glow: "from-primary/10",
  },
  success: {
    icon: "bg-green-500/10 text-green-600 dark:text-green-400",
    glow: "from-green-500/10",
  },
  warning: {
    icon: "bg-yellow-500/10 text-yellow-600 dark:text-yellow-400",
    glow: "from-yellow-500/10",
  },
  destructive: {
    icon: "bg-destructive/10 text-destructive",
    glow: "from-destructive/10",
  },
};

const StatCard = ({
  title,
  value,
  description,
  icon,
  tone = "primary",
}: StatCardProps) => {
  const styles = TONE_STYLES[tone];

  return (
    <div className="relative overflow-hidden rounded-xl border bg-card p-6 shadow-sm transition-all duration-300 hover:-translate-y-1 hover:shadow-lg">

      <div
        className={cn("absolute inset-0 bg-gradient-to-br to-transparent", styles.glow)}
        aria-hidden="true"
      />

      {/* Top Section */}
      <div className="relative flex items-center justify-between">

        <div>
          <p className="text-sm text-muted-foreground">
            {title}
          </p>

          <h2 className="mt-2 text-3xl font-bold">
            {value}
          </h2>
        </div>

        <div className={cn("flex h-14 w-14 items-center justify-center rounded-full", styles.icon)}>
          {icon}
        </div>

      </div>

      {/* Bottom Section */}
      {description && (
        <p className="relative mt-4 text-sm text-muted-foreground">
          {description}
        </p>
      )}

    </div>
  );
};

export default StatCard;
