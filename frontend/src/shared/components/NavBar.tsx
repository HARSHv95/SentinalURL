import { Search } from "lucide-react";
import UserMenu from "../../features/auth/components/UserMenu";
import AlertsBell from "../../features/watchlist/components/AlertsBell";

const Navbar = () => {
  return (
    <header className="h-16 border-b bg-background px-6 flex items-center justify-end">

      <div className="flex items-center gap-5">

        <button className="hover:text-primary transition">
          <Search size={20} />
        </button>

        <AlertsBell />

        <UserMenu />

      </div>

    </header>
  );
};

export default Navbar;