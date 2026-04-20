import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

const COLORS = [
  "#6366f1",
  "#22d3ee",
  "#f59e0b",
  "#10b981",
  "#f43f5e",
  "#8b5cf6",
];

type PieTooltipProps = {
  active?: boolean;
  payload?: { name: string; value: number; payload: { percentage: string } }[];
};

const RevenueTooltip = ({ active, payload }: PieTooltipProps) => {
  if (!active || !payload?.length) return null;
  const entry = payload[0];
  return (
    <div>
      <p style={{ fontWeight: 500 }}>{entry.name}</p>
      <p>${entry.value.toFixed(2)}</p>
      <p style={{ color: "#6b7280" }}>{entry.payload.percentage}%</p>
    </div>
  );
};

type PieEntry = {
  name: string;
  value: number;
  percentage: string;
};

type LegendEntry = {
  payload: PieEntry;
};

export const RevenuePieChart = <T extends { revenue: number }>({
  data,
  categoryKey,
}: {
  data: T[];
  categoryKey: keyof T & string;
}) => {
  const total = data.reduce((sum, item) => sum + item.revenue, 0);

  const enriched: PieEntry[] = data.map((item) => ({
    name: String(item[categoryKey]),
    value: item.revenue,
    percentage: ((item.revenue / total) * 100).toFixed(1),
  }));

  return (
    <ResponsiveContainer width="100%" height={320}>
      <PieChart>
        <Pie
          data={enriched}
          cx="50%"
          cy="50%"
          outerRadius={110}
          innerRadius={55}
          dataKey="value"
          paddingAngle={2}
        >
          {enriched.map((_, index) => (
            <Cell key={index} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip content={<RevenueTooltip />} />
        <Legend
          formatter={(value, entry) =>
            `${value} — ${(entry as unknown as LegendEntry).payload.percentage}%`
          }
        />
      </PieChart>
    </ResponsiveContainer>
  );
};
