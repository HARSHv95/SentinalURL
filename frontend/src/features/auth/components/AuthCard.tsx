import type { ReactNode } from "react";

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
    <div className="w-full max-w-sm">
      <h1 className="text-2xl font-bold tracking-tight">
        {title}
      </h1>

      <p className="mt-2 text-muted-foreground">
        {description}
      </p>

      <div className="mt-8">
        {children}
      </div>
    </div>
  );
};

export default AuthCard;
