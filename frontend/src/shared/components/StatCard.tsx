import type { ReactNode } from "react";

interface StatCardProps {
  title: string;
  value: number;
  icon: ReactNode;
}

const StatCard = ({ title, value, icon }: StatCardProps) => {
  return (
    <div className="rounded-xl border p-6 shadow-sm bg-card">

      <div className="flex justify-between items-center">

        <div>

          <p className="text-sm text-muted-foreground">
            {title}
          </p>

          <h2 className="text-3xl font-bold mt-2">
            {value}
          </h2>

        </div>

        <div>
          {icon}
        </div>

      </div>

    </div>
  );
};

export default StatCard;