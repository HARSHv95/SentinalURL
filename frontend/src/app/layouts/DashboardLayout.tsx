import { Outlet } from "react-router-dom";
import NavBar from "../../shared/components/NavBar";
import SideBar from "../../shared/components/SideBar";

const DashboardLayout = () => {
  return (
    <div className="flex h-screen">

      <SideBar />

      <div className="flex flex-col flex-1">

        <NavBar />

        <main className="flex-1 overflow-auto p-6">
          <Outlet />
        </main>

      </div>

    </div>
  );
};

export default DashboardLayout;