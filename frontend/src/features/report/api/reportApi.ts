import { scanClient } from "../../../shared/api/apiClient";

import type { ScanStatsResponse } from "../types/report";

export async function getReportStats(
  days = 30
): Promise<ScanStatsResponse> {

  const response = await scanClient.get<ScanStatsResponse>(
    "/api/v1/scan/stats",
    { params: { days } }
  );

  return response.data;
}
