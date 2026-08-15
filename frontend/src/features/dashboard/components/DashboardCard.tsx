import type { ReactNode } from "react";

interface DashboardCardProps {
  title: string;
  children: ReactNode;
}

const DashboardCard = ({
  title,
  children,
}: DashboardCardProps) => {
  return (
    <div className="rounded-xl border bg-card p-6 shadow-sm">
      <h2 className="mb-5 text-lg font-semibold">
        {title}
      </h2>

      {children}
    </div>
  );
};

export default DashboardCard;