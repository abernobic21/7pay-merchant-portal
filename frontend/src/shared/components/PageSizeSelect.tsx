interface Props {
  value: number | undefined;
  onChange: (size: number) => void;
  options?: number[];
}

export const PageSizeSelect = ({
  value,
  onChange,
  options = [10, 25, 50, 100],
}: Props) => {
  return (
    <select
      value={value ?? 10}
      onChange={(e) => onChange(Number(e.target.value))}
    >
      {options.map((o) => (
        <option key={o} value={o}>
          {o}
        </option>
      ))}
    </select>
  );
};
