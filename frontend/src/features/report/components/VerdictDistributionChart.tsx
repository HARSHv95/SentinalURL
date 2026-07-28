import {
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  LabelList,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import { Card, CardContent, CardHeader, CardTitle } from "../../../components/ui/card";

import { VERDICT_META } from "../../scan/lib/verdict";
import { VERDICT_CHART_COLORS } from "../lib/verdictChartColors";

import type { VerdictCount } from "../types/report";

interface Props {
  data: VerdictCount[];
}

export default function VerdictDistributionChart({ data }: Props) {
  const chartData = data.map((item) => ({
    verdict: item.verdict,
    label: VERDICT_META[item.verdict].label,
    count: item.count,
  }));

  return (
    <Card>
      <CardHeader>
        <CardTitle>Verdict Distribution</CardTitle>
      </CardHeader>

      <CardContent>
        <ResponsiveContainer width="100%" height={280}>
          <BarChart data={chartData} margin={{ top: 16, right: 8, left: -16, bottom: 0 }}>
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
              interval={0}
            />
            <YAxis
              allowDecimals={false}
              tickLine={false}
              axisLine={false}
              tick={{ fill: "var(--muted-foreground)", fontSize: 12 }}
              width={32}
            />
            <Tooltip
              cursor={{ fill: "var(--muted)", opacity: 0.4 }}
              contentStyle={{
                background: "var(--popover)",
                border: "1px solid var(--border)",
                borderRadius: 8,
                fontSize: 13,
              }}
            />
            <Bar dataKey="count" radius={[4, 4, 0, 0]} maxBarSize={40}>
              <LabelList
                dataKey="count"
                position="top"
                style={{ fill: "var(--foreground)", fontSize: 12, fontWeight: 600 }}
              />
              {chartData.map((entry) => (
                <Cell
                  key={entry.verdict}
                  fill={VERDICT_CHART_COLORS[entry.verdict].light}
                />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}
