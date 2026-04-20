interface SortableHeaderProps {
  label: string;
  field: string;
  currentSort?: string;
  onSort: (sort: string) => void;
}

export const SortableHeader = ({
  label,
  field,
  currentSort,
  onSort,
}: SortableHeaderProps) => {
  const [sortField, sortDir] = (currentSort ?? "").split(",");
  const isActive = sortField === field;

  const handleClick = () => {
    if (!isActive) {
      onSort(`${field},asc`);
    } else {
      onSort(`${field},${sortDir === "asc" ? "desc" : "asc"}`); // toggle
    }
  };

  return (
    <th onClick={handleClick} style={{ cursor: "pointer", userSelect: "none" }}>
      {label} {isActive ? (sortDir === "asc" ? "↑" : "↓") : "↕"}
    </th>
  );
};
