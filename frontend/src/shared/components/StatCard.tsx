import type { ReactNode } from "react";

interface StatCardProps {
  title: string;
  value: number | string;
  description?: string;
  icon: ReactNode;
}

const StatCard = ({
  title,
  value,
  description,
  icon,
}: StatCardProps) => {
  return (
    <div className="rounded-xl border bg-card p-6 shadow-sm transition-all duration-300 hover:-translate-y-1 hover:shadow-lg">

      {/* Top Section */}
      <div className="flex items-center justify-between">

        <div>
          <p className="text-sm text-muted-foreground">
            {title}
          </p>

          <h2 className="mt-2 text-3xl font-bold">
            {value}
          </h2>
        </div>

        <div className="flex h-14 w-14 items-center justify-center rounded-full bg-primary/10 text-primary">
          {icon}
        </div>

      </div>

      {/* Bottom Section */}
      {description && (
        <p className="mt-4 text-sm text-muted-foreground">
          {description}
        </p>
      )}

    </div>
  );
};

export default StatCard;
