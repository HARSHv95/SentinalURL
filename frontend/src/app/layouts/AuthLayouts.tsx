import { Outlet } from "react-router-dom";

const AuthLayout = () => {
  return (
    <main className="flex min-h-screen items-center justify-center bg-muted/30 p-6">
      <Outlet />
    </main>
  );
};

export default AuthLayout;