export interface ShortenRequest {
  originalUrl: string;
  ttlInDays?: number;
  customAlias?: string;
}

export interface ShortenResponse {
  shortUrl: string;
  originalUrl: string;
  alias: string;
  expiresAt: string;
}
