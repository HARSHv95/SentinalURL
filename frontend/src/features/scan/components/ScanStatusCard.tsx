import type { CreateScanResponse } from "../types/scan";

interface Props {
    scan: CreateScanResponse;
}

export default function ScanStatusCard({
    scan,
}: Props) {

    return (
        <div className="rounded-lg border p-6 mt-6">

            <h2 className="font-semibold text-lg">
                Scan Submitted
            </h2>

            <div className="mt-4 space-y-2">

                <p>
                    <strong>Scan ID:</strong> {scan.scanId}
                </p>

                <p>
                    <strong>URL:</strong> {scan.url}
                </p>

                <p>
                    <strong>Status:</strong> {scan.status}
                </p>

                <p>
                    <strong>Created:</strong>{" "}
                    {new Date(scan.createdAt).toLocaleString()}
                </p>

            </div>

        </div>
    );
}