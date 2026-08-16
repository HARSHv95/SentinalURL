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

export async function shareScan(
  scanId: string
): Promise<{ shareToken: string }> {

  const response = await scanClient.post<{ shareToken: string }>(
    `/api/v1/scan/share?scanId=${scanId}`
  );

  return response.data;
}

export async function unshareScan(scanId: string): Promise<void> {
  await scanClient.post(`/api/v1/scan/unshare?scanId=${scanId}`);
}

export async function getSharedScan(
  shareToken: string
): Promise<CreateScanResponse> {

  const response = await scanClient.get<CreateScanResponse>(
    `/api/v1/scan/shared/${shareToken}`
  );

  return response.data;
}
