export type ProviderStatus = "AVAILABLE" | "UNAVAILABLE" | "ERROR" | "TIMEOUT";

export interface ThreatProviderResult {
  providerName: string;
  status: ProviderStatus;
  malicious: boolean;
  suspicious: boolean;
  maliciousEngineCount: number | null;
  harmlessEngineCount: number | null;
  suspiciousEngineCount: number | null;
  undetectedEngineCount: number | null;
  categories: string[];
  reference: string | null;
  checkedAt: string | null;
  details: string | null;
}

export interface RiskFactor {
  label: string;
  contribution: number;
}

export interface DnsRecords {
  A?: string[];
  AAAA?: string[];
  MX?: string[];
  NS?: string[];
  CNAME?: string[];
}

export interface DomainIntelligence {
  domain: string;
  registrar: string | null;
  creationDate: string | null;
  expirationDate: string | null;
  updatedDate: string | null;
  domainAgeDays: number | null;
  country: string | null;
  hostingProvider: string | null;
  asn: string | null;
  ipAddresses: string | null;
  dnsRecords: DnsRecords | null;
  sslIssuer: string | null;
  sslValidFrom: string | null;
  sslValidTo: string | null;
  sslValid: boolean | null;
}

export interface AIThreatAnalysis {
  executiveSummary: string;
  technicalAnalysis: string;
  riskFactors: string[];
  recommendations: string[];
  confidence: number;
  model: string;
  generatedAt: string;
}
