import { ShieldCheck } from "lucide-react";

import useAuth from "../../auth/hooks/useAuth";

interface Props {
  totalScans?: number;
  maliciousCount?: number;
  isLoading: boolean;
}

export default function DashboardHero({ totalScans, maliciousCount, isLoading }: Props) {
  const { user } = useAuth();

  const status = isLoading
    ? "Loading your latest activity..."
    : !totalScans
      ? "Run your first scan to start building your security history."
      : maliciousCount
        ? `${maliciousCount} scan${maliciousCount === 1 ? "" : "s"} flagged as malicious — worth a look.`
        : `All clear across ${totalScans} scan${totalScans === 1 ? "" : "s"}.`;

  return (
    <div className="relative overflow-hidden rounded-2xl border bg-gradient-to-br from-primary/10 via-primary/5 to-transparent p-8">

      <div
        className="absolute inset-0 bg-[radial-gradient(circle_at_1px_1px,rgba(0,0,0,0.06)_1px,transparent_0)] bg-[size:24px_24px] dark:bg-[radial-gradient(circle_at_1px_1px,rgba(255,255,255,0.08)_1px,transparent_0)]"
        aria-hidden="true"
      />

      <div className="relative flex items-center justify-between gap-4">
        <div>
          <p className="text-sm font-medium text-muted-foreground">
            Welcome back,
          </p>

          <h1 className="mt-1 text-3xl font-bold tracking-tight">
            {user?.firstName ?? "there"}
          </h1>

          <p className="mt-3 max-w-md text-muted-foreground">
            {status}
          </p>
        </div>

        <div className="hidden size-16 shrink-0 items-center justify-center rounded-2xl bg-primary/10 text-primary sm:flex">
          <ShieldCheck size={32} />
        </div>
      </div>

    </div>
  );
}
