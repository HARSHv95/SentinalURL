import { NavLink } from "react-router-dom";
import { navigation } from "../lib/navigation";

const Sidebar = () => {
  return (
    <aside className="w-64 h-screen border-r bg-background p-4">
      <h1 className="text-2xl font-bold mb-8">
        SentinelURL
      </h1>

      <nav className="flex flex-col gap-2">

    {navigation.map((item) => {

        const Icon = item.icon;

        return (

            <NavLink
                to={item.path}
    className={({ isActive }) =>
        isActive
            ? "bg-blue-600 text-white rounded-lg px-3 py-2"
            : "hover:bg-muted rounded-lg px-3 py-2"
    }
            >

                <div className="flex items-center gap-3">

                    <Icon size={20} />

                    <span>{item.title}</span>

                </div>

            </NavLink>

        );

    })}

</nav>
    </aside>
  );
};

export default Sidebar;