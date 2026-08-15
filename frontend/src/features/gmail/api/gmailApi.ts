import { scanClient } from "../../../shared/api/apiClient";

import type { EmailScanBatch, GmailStatus, PageResponse } from "../types/gmail";

export async function getAuthorizeUrl(): Promise<string> {
  const response = await scanClient.get<{ url: string }>(
    "/api/v1/gmail/authorize-url"
  );

  return response.data.url;
}

export async function getGmailStatus(): Promise<GmailStatus> {
  const response = await scanClient.get<GmailStatus>("/api/v1/gmail/status");

  return response.data;
}

export async function disconnectGmail(): Promise<void> {
  await scanClient.post("/api/v1/gmail/disconnect");
}

export async function syncGmailNow(): Promise<void> {
  await scanClient.post("/api/v1/gmail/sync-now");
}

export async function getEmailBatches(
  page: number,
  size: number
): Promise<PageResponse<EmailScanBatch>> {

  const response = await scanClient.get<PageResponse<EmailScanBatch>>(
    "/api/v1/email-batches/all",
    { params: { page, size } }
  );

  return response.data;
}
