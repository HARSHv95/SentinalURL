import {
    Shield,
    ShieldCheck,
    ShieldAlert,
    Clock3
} from "lucide-react";

import StatCard from "../../../shared/components/StatCard";

const DashboardPage = () => {

    return (

        <>
            <div className="mb-8">

    <h1 className="text-4xl font-bold">
        Welcome back 👋
    </h1>

    <p className="text-muted-foreground mt-2">
        Start scanning URLs to detect phishing,
        malware and malicious websites.
    </p>

</div>
            <div className="grid grid-cols-4 gap-6">

                <StatCard
                    title="Total Scans"
                    value={0}
                    icon={<Shield />}
                />

                <StatCard
                    title="Safe URLs"
                    value={0}
                    icon={<ShieldCheck />}
                />

                <StatCard
                    title="Suspicious"
                    value={0}
                    icon={<Clock3 />}
                />

                <StatCard
                    title="Malicious"
                    value={0}
                    icon={<ShieldAlert />}
                />

            </div>

        </>

    );

};

export default DashboardPage;