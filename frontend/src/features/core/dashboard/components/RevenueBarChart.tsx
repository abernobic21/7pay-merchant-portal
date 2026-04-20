import { useState } from "react";
import { useTranslation } from "react-i18next";
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
  formatCategory?: (value: string) => string;
};

const RevenueTooltip = ({
  active,
  payload,
  label,
  formatCategory,
}: RevenueTooltipProps) => {
  if (!active || !payload?.length) return null;
  return (
    <div className="tooltip">
      <p>{formatCategory ? formatCategory(label ?? "") : label}</p>
      {payload.map((entry) => (
        <p key={entry.name} style={{ color: entry.color }}>
          {entry.name}: {entry.value.toFixed(2)}
        </p>
      ))}
    </div>
  );
};

export const RevenueBarChart = <T extends { revenue: number; count: number }>({
  data,
  categoryKey,
  formatCategory,
}: {
  data: T[];
  categoryKey: keyof T & string;
  formatCategory?: (value: string) => string;
}) => {
  const { t } = useTranslation("dashboard");
  const [tooltipPos, setTooltipPos] = useState<
    { x: number; y: number } | undefined
  >();

  return (
    <ResponsiveContainer width="100%" height={300}>
      <BarChart
        data={data}
        margin={{ top: 8, right: 16, bottom: 8, left: 0 }}
        onMouseMove={(state) => {
          if (state.isTooltipActive && state.activeCoordinate) {
            setTooltipPos({
              x: state.activeCoordinate.x,
              y: state.activeCoordinate.y,
            });
          }
        }}
        onMouseLeave={() => setTooltipPos(undefined)}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey={categoryKey as string} tickFormatter={formatCategory} />
        <YAxis
          yAxisId="left"
          orientation="left"
          label={{
            value: t("charts.revenue"),
            angle: -90,
            position: "insideLeft",
          }}
        />
        <YAxis
          yAxisId="right"
          orientation="right"
          label={{
            value: t("charts.count"),
            angle: 90,
            position: "insideRight",
          }}
        />
        <Tooltip
          content={<RevenueTooltip formatCategory={formatCategory} />}
          position={tooltipPos ? { x: tooltipPos.x - 60, y: 20 } : undefined}
          isAnimationActive={false}
        />
        <Legend />
        <Bar
          yAxisId="left"
          dataKey="revenue"
          fill="#F5454B"
          name={t("charts.revenue")}
        />
        <Bar
          yAxisId="right"
          dataKey="count"
          fill="#d01d1d"
          name={t("charts.count")}
        />
      </BarChart>
    </ResponsiveContainer>
  );
};
