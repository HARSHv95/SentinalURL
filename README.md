# SentinalURL

A VirusTotal-inspired URL security scanner. Authenticated users submit a URL,
the platform analyzes it asynchronously via the VirusTotal API, computes a
risk assessment, and surfaces the result through a dashboard-style frontend —
scan history with search/filter/sort, and an analytics reports page.

## Architecture

Two independent Spring Boot services behind a shared Postgres instance, plus
a React SPA:

```
backend/
├── auth/    → authentication & user management (JWT issuance)
├── scan/    → scan submission, async VirusTotal analysis, risk scoring,
│              scan history, and reporting/analytics
└── lib-resource/ (unused scaffold)

frontend/    → React + TypeScript SPA, feature-based structure
```

Each backend service is an independently deployable Spring Boot app with its
own Postgres schema and Flyway migrations. The frontend talks to both over
plain REST + JWT bearer auth — there is no API gateway.

## Tech stack

**Backend**
- Java 21, Spring Boot 3.5
- Spring Security + JWT (`jjwt`)
- Spring Data JPA (Specifications for dynamic filtering) + PostgreSQL
- Flyway migrations
- Async scan processing via `@EnableAsync`/`@Async`
- Docker (one image per service)

**Frontend**
- React 19 + TypeScript + Vite
- TailwindCSS + shadcn-style components built on `@base-ui/react`
- TanStack React Query v5 (server state, polling, cache invalidation)
- React Router, React Hook Form + Zod, Axios, Recharts, Lucide icons

## Features implemented so far

### Authentication
- Register / login with JWT issuance (`auth-service`)
- Axios interceptors attach the bearer token and auto-logout on `401`
- Protected and public route guards on the frontend

### URL scanning
- Submit a URL (`POST /api/v1/scan/create`) — returns immediately with status
  `PENDING`
- Background `@Async` worker submits the URL to VirusTotal, polls for
  completion, then persists the computed risk score and verdict on the scan
- Risk scoring is centralized in `RiskScoreCalculator`: a 0–100 score derived
  from VirusTotal's malicious/suspicious engine counts, mapped to one of five
  verdicts — `SAFE`, `LOW_RISK`, `MEDIUM_RISK`, `HIGH_RISK`, `CRITICAL`
- Scan detail page polls automatically while a scan is `PENDING`/`IN_PROGRESS`
  and stops once it reaches a terminal state, showing a detection breakdown
  and overall verdict once complete

### Scan history dashboard
- Paginated, filterable, sortable scan list (`GET /api/v1/scan/all`) — all
  filtering happens in Postgres via `JpaSpecificationExecutor`, never
  client-side
- Search by URL, filter by status and verdict, sort by newest/oldest/risk
  score
- URL-synced filter state (survives refresh, shareable/bookmarkable),
  skeleton loading, and empty states (including a filter-aware "no results"
  state with a clear-filters action)

### Reports — analytics dashboard
- `GET /api/v1/scan/stats` aggregates a user's scan history into:
  - Summary counts: total scans, safe / suspicious / malicious / pending
  - Verdict distribution (bar chart)
  - Risk score distribution (10-bucket histogram)
  - Daily scan volume over a configurable window (default 30 days,
    zero-filled)
  - Top risky domains (ranked by peak risk score)
  - Recent high/critical-risk threats
- Chart colors are taken from a CVD-validated palette rather than chosen by
  eye
- The same endpoint's summary counts also power the Dashboard page's stat
  cards

### Dashboard
- At-a-glance stat cards (Total / Safe / Suspicious / Malicious) backed by
  the same reporting endpoint

## API reference

**auth-service** (`/api/v1/auth`)
| Method | Path | Description |
|---|---|---|
| POST | `/register` | Create a new account |
| POST | `/login` | Authenticate, returns a JWT |
| GET | `/me` | Current authenticated user |

**scan-service** (`/api/v1/scan`)
| Method | Path | Description |
|---|---|---|
| POST | `/create` | Submit a URL for scanning (returns `PENDING` immediately) |
| GET | `/all` | Paginated scan list — `page`, `size`, `search`, `status`, `verdict`, `sort` |
| GET | `/id?scanId=` | Full detail for a single scan |
| GET | `/delete?scanId=` | Delete a scan |
| GET | `/stats?days=` | Aggregated analytics for the current user |

All endpoints except `/register`/`/login` require an `Authorization: Bearer <jwt>` header.

## Running locally

Requires Docker and Docker Compose.

1. Copy the root `.env` (already present for local dev) and fill in your own
   `JWT_SECRET` and `VIRUSTOTAL_API_KEY` — **do not commit real secrets**.
2. From the repo root:
   ```bash
   docker compose up --build
   ```
   This starts Postgres, `auth-service` (`:8080`), and `scan-service`
   (`:8081`), each running its own Flyway migrations on startup.
3. Frontend (separate terminal):
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   Configure `frontend/.env` with `VITE_API_SERVICE_AUTH` /
   `VITE_API_SERVICE_SCAN` pointing at the two backend services.

## Roadmap

- CSV / PDF export for reports
- Dashboard "Recent Activity" and "Latest Reports" widgets (currently static)
