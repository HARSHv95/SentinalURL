import { Bell, Search, UserCircle2 } from "lucide-react";

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

        <button className="hover:text-primary transition relative">
          <Bell size={20} />
          <span className="absolute -top-1 -right-1 h-2 w-2 rounded-full bg-red-500"></span>
        </button>

        <button className="flex items-center gap-2">

          <UserCircle2 size={34} />

          <div className="text-left">
            <p className="font-medium">Harsh</p>
            <p className="text-xs text-muted-foreground">
              Security Analyst
            </p>
          </div>

        </button>

      </div>

    </header>
  );
};

export default Navbar;