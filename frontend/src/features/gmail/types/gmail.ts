export interface GmailStatus {
  connected: boolean;
  lastSyncedAt: string | null;
  lastSyncError: string | null;
}

export interface EmailScanBatch {
  id: string;
  subject: string | null;
  senderPreview: string | null;
  urlCount: number;
  createdAt: string;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}
