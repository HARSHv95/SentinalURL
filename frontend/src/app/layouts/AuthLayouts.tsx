import { Outlet } from "react-router-dom";
import { ShieldCheck } from "lucide-react";

const AuthLayout = () => {
  return (
    <main className="flex min-h-screen">
      <div
        className="relative hidden w-1/2 flex-col justify-between overflow-hidden bg-gradient-to-br from-primary to-primary/80 p-12 text-primary-foreground lg:flex"
      >
        <div
          className="absolute inset-0 bg-[radial-gradient(circle_at_1px_1px,rgba(255,255,255,0.15)_1px,transparent_0)] bg-[size:28px_28px]"
          aria-hidden="true"
        />

        <div className="relative flex items-center gap-2">
          <ShieldCheck size={28} />
          <span className="text-xl font-bold">SentinalURL</span>
        </div>

        <div className="relative max-w-md space-y-4">
          <h2 className="text-3xl leading-tight font-bold">
            Know before you click.
          </h2>
          <p className="text-primary-foreground/75">
            Real-time URL scanning, watchlist monitoring, and multi-engine
            threat intelligence — all in one place.
          </p>
        </div>

        <p className="relative text-sm text-primary-foreground/60">
          © {new Date().getFullYear()} SentinalURL
        </p>
      </div>

      <div className="flex flex-1 flex-col items-center justify-center bg-background p-6">
        <div className="mb-8 flex items-center gap-2 lg:hidden">
          <ShieldCheck className="text-primary" size={24} />
          <span className="text-lg font-bold">SentinalURL</span>
        </div>

        <Outlet />
      </div>
    </main>
  );
};

export default AuthLayout;
