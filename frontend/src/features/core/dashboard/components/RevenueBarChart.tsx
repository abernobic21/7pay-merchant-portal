import {
  Bar,
  BarChart,
  CartesianGrid,
  Legend,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

type TooltipEntry = {
  name: string;
  value: number;
  color: string;
};

type RevenueTooltipProps = {
  active?: boolean;
  payload?: TooltipEntry[];
  label?: string;
};

const RevenueTooltip = ({ active, payload, label }: RevenueTooltipProps) => {
  if (!active || !payload?.length) return null;
  return (
    <div>
      <p>{label}</p>
      {payload.map((entry) => (
        <p key={entry.name} style={{ color: entry.color }}>
          {entry.name}:{" "}
          {entry.name === "Revenue"
            ? `$${entry.value.toFixed(2)}`
            : entry.value}
        </p>
      ))}
    </div>
  );
};

export const RevenueBarChart = <T extends { revenue: number; count: number }>({
  data,
  categoryKey,
}: {
  data: T[];
  categoryKey: keyof T & string;
}) => (
  <ResponsiveContainer width="100%" height={300}>
    <BarChart data={data} margin={{ top: 8, right: 16, bottom: 8, left: 0 }}>
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey={categoryKey as string} />
      <YAxis yAxisId="left" orientation="left" />
      <YAxis yAxisId="right" orientation="right" />
      <Tooltip content={<RevenueTooltip />} />
      <Legend />
      <Bar yAxisId="left" dataKey="revenue" fill="#6366f1" name="Revenue" />
      <Bar yAxisId="right" dataKey="count" fill="#22d3ee" name="Count" />
    </BarChart>
  </ResponsiveContainer>
);
