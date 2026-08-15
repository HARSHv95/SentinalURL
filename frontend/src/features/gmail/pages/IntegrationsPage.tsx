import { useSearchParams } from "react-router-dom";

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
      />

      {connected === "gmail" && (
        <p className="text-sm text-emerald-600">
          Gmail connected successfully.
        </p>
      )}

      {error && (
        <p className="text-sm text-red-500">
          Failed to connect Gmail: {error}
        </p>
      )}

      <GmailConnectionCard />

      <EmailBatchList />
    </div>
  );
}
