import type { ReactNode } from "react";
import { ShieldCheck } from "lucide-react";

interface AuthCardProps {
  title: string;
  description: string;
  children: ReactNode;
}

const AuthCard = ({
  title,
  description,
  children,
}: AuthCardProps) => {
  return (
    <div className="w-full max-w-md rounded-2xl border bg-card p-8 shadow-lg">

      <div className="mb-8 flex flex-col items-center">

        <div className="mb-4 rounded-full bg-primary/10 p-3">
          <ShieldCheck className="text-primary" size={32} />
        </div>

        <h1 className="text-2xl font-bold">
          SentinelURL
        </h1>

        <h2 className="mt-6 text-xl font-semibold">
          {title}
        </h2>

        <p className="mt-2 text-center text-sm text-muted-foreground">
          {description}
        </p>

      </div>

      {children}

    </div>
  );
};

export default AuthCard;