import { useTranslation } from "react-i18next";

interface ReportTableProps {
  data: string[][];
  filename: string;
  blob: Blob;
}

export const ReportTable = ({ data, filename, blob }: ReportTableProps) => {
  const { t: tReports } = useTranslation("reports");
  const { t: tTransactions } = useTranslation("transactions");
  const [headers, ...rows] = data;

  const translateHeader = (header: string) => {
    const key = `fields.${header.trim()}`;
    const translated = tTransactions(key);

    return translated === key ? header.trim() : translated;
  };

  const handleDownload = () => {
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div>
      <button onClick={handleDownload}>
        {tReports("actions.download")}
        {filename}
      </button>
      <div style={{ overflow: "auto" }}>
        <table>
          <thead>
            <tr>
              {headers.map((h, i) => (
                <th key={i}>{translateHeader(h)}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {rows.map((row, i) => (
              <tr key={i}>
                {row.map((cell, j) => (
                  <td key={j}>{cell}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
