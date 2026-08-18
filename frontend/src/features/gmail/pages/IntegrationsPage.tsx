import { useSearchParams } from "react-router-dom";
import { Plug } from "lucide-react";

import PageHeader from "../../../shared/components/PageHeader";

import GmailConnectionCard from "../components/GmailConnectionCard";
import EmailBatchList from "../components/EmailBatchList";

export default function IntegrationsPage() {
  const [searchParams] = useSearchParams();

  const connected = searchParams.get("connected");
  const error = searchParams.get("error");

  return (
    <div className="space-y-8">
      <PageHeader
        title="Integrations"
        description="Connect external accounts SentinalURL can monitor for you."
        icon={Plug}
      />

      {connected === "gmail" && (
        <p className="text-sm text-green-700 dark:text-green-400">
          Gmail connected successfully.
        </p>
      )}

      {error && (
        <p className="text-sm text-destructive">
          Failed to connect Gmail: {error}
        </p>
      )}

      <GmailConnectionCard />

      <EmailBatchList />
    </div>
  );
}
