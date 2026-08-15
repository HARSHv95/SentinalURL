import { Search } from "lucide-react";
import UserMenu from "../../features/auth/components/UserMenu";
import AlertsBell from "../../features/watchlist/components/AlertsBell";

const Navbar = () => {
  return (
    <header className="h-16 border-b bg-background px-6 flex items-center justify-between">

      {/* Left Section */}
      <div>
        <h1 className="text-2xl font-bold">Dashboard</h1>
        <p className="text-sm text-muted-foreground">
          Welcome back!
        </p>
      </div>

      {/* Right Section */}
      <div className="flex items-center gap-5">

        <button className="hover:text-primary transition">
          <Search size={20} />
        </button>

        <AlertsBell />

        <button className="flex items-center gap-2">

          <UserMenu />

        </button>

      </div>

    </header>
  );
};

export default Navbar;