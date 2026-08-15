import { LogOut, Settings } from "lucide-react";
import { useNavigate } from "react-router-dom";

import { Avatar, AvatarFallback } from "../../../components/ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
    DropdownMenuGroup,
} from "../../../components/ui/dropdown-menu";

import  useAuth  from "../hooks/useAuth";
import { ROUTES } from "../../../shared/lib/routes";

const ROLE_LABELS = {
    USER: "Security Analyst",
    ADMIN: "Administrator",
};

export default function UserMenu() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const initials =
        `${user?.firstName?.[0] ?? ""}${user?.lastName?.[0] ?? ""}`;

    const handleLogout = () => {
        logout();
        navigate(ROUTES.LOGIN, { replace: true });
    };

    return (
        <DropdownMenu>
            <DropdownMenuTrigger className="flex items-center gap-3 rounded-lg px-2 py-1 hover:bg-muted transition">
    <Avatar>
        <AvatarFallback>{initials}</AvatarFallback>
    </Avatar>

    <div className="text-left">
        <p className="text-sm font-semibold">
            {user?.firstName} {user?.lastName}
        </p>

        <p className="text-xs text-muted-foreground">
            {ROLE_LABELS[user?.role ?? "USER"]}
        </p>
    </div>
</DropdownMenuTrigger>

            <DropdownMenuContent align="end">

    <DropdownMenuGroup>

        <DropdownMenuLabel>
            My Account
        </DropdownMenuLabel>

        <DropdownMenuItem onClick={() => navigate(ROUTES.SETTINGS_INTEGRATIONS)}>
            <Settings className="mr-2 h-4 w-4" />
            Settings
        </DropdownMenuItem>

    </DropdownMenuGroup>

    <DropdownMenuSeparator />

    <DropdownMenuItem
        variant="destructive"
        onClick={handleLogout}
    >
        <LogOut className="mr-2 h-4 w-4" />
        Logout
    </DropdownMenuItem>

</DropdownMenuContent>
        </DropdownMenu>
    );
}