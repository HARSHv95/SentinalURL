import {
  Area,
  AreaChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";

import { SEQUENTIAL_CHART_COLOR } from "../lib/verdictChartColors";

import type { DailyScanCount } from "../types/report";

interface Props {
  data: DailyScanCount[];
}

function formatDate(dateStr: string) {
  const date = new Date(dateStr);
  return date.toLocaleDateString(undefined, { month: "short", day: "numeric" });
}

export default function DailyScanCountChart({ data }: Props) {
  const chartData = data.map((item) => ({
    date: item.date,
    label: formatDate(item.date),
    count: item.count,
  }));

  return (
    <Card>
      <CardHeader>
        <CardTitle>Daily Scan Count</CardTitle>
      </CardHeader>

      <CardContent>
        <ResponsiveContainer width="100%" height={280}>
          <AreaChart data={chartData} margin={{ top: 16, right: 8, left: -16, bottom: 0 }}>
            <defs>
              <linearGradient id="dailyScanFill" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stopColor={SEQUENTIAL_CHART_COLOR} stopOpacity={0.25} />
                <stop offset="100%" stopColor={SEQUENTIAL_CHART_COLOR} stopOpacity={0.02} />
              </linearGradient>
            </defs>
            <CartesianGrid
              vertical={false}
              stroke="var(--border)"
              strokeOpacity={0.5}
            />
            <XAxis
              dataKey="label"
              tickLine={false}
              axisLine={false}
              tick={{ fill: "var(--muted-foreground)", fontSize: 12 }}
              minTickGap={24}
            />
            <YAxis
              allowDecimals={false}
              tickLine={false}
              axisLine={false}
              tick={{ fill: "var(--muted-foreground)", fontSize: 12 }}
              width={32}
            />
            <Tooltip
              contentStyle={{
                background: "var(--popover)",
                border: "1px solid var(--border)",
                borderRadius: 8,
                fontSize: 13,
              }}
            />
            <Area
              type="monotone"
              dataKey="count"
              stroke={SEQUENTIAL_CHART_COLOR}
              strokeWidth={2}
              fill="url(#dailyScanFill)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
