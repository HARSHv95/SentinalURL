import { scanClient } from "../../../shared/api/apiClient";

import type {
    CreateScanRequest,
    CreateScanResponse,
    PageResponse,
    ScanFilters,
    ScanSummary,
} from "../types/scan";

export async function createScan(
    data: CreateScanRequest
): Promise<CreateScanResponse> {

    const response = await scanClient.post(
        "/api/v1/scan/create",
        data
    );

    return response.data;
}

export async function getScans(
    filters: ScanFilters
): Promise<PageResponse<ScanSummary>> {

  const response = await scanClient.get<PageResponse<ScanSummary>>(
    "/api/v1/scan/all",
    { params: filters }
  );

  return response.data;
}

export async function getScan(
  scanId: string
): Promise<CreateScanResponse> {

  const response = await scanClient.get<CreateScanResponse>(
    `/api/v1/scan/id?scanId=${scanId}`
  );

  return response.data;
}
