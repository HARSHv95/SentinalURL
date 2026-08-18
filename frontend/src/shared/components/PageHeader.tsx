import type { ReactNode } from "react";
import type { LucideIcon } from "lucide-react";

interface PageHeaderProps {
  title: string;
  description?: string;
  actions?: ReactNode;
  icon?: LucideIcon;
}

const PageHeader = ({
  title,
  description,
  actions,
  icon: Icon,
}: PageHeaderProps) => {
  return (
    <div className="relative overflow-hidden rounded-2xl border bg-gradient-to-br from-primary/5 via-transparent to-transparent p-6">

      <div className="relative flex flex-col gap-4 md:flex-row md:items-center md:justify-between">

        <div className="flex items-center gap-4">

          {Icon && (
            <div className="hidden size-12 shrink-0 items-center justify-center rounded-xl bg-primary/10 text-primary sm:flex">
              <Icon size={24} />
            </div>
          )}

          <div>

            <h1 className="text-3xl font-bold tracking-tight">
              {title}
            </h1>

            {description && (
              <p className="mt-2 text-muted-foreground">
                {description}
              </p>
            )}

          </div>

        </div>

        {actions && (
          <div className="flex items-center gap-2">
            {actions}
          </div>
        )}

      </div>

    </div>
  );
};

export default PageHeader;
