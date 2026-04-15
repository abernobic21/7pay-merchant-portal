export const getPages = (current: number, total: number) => {
  const delta = 2;
  const pages: (number | string)[] = [];

  const rangeStart = Math.max(0, current - delta);
  const rangeEnd = Math.min(total - 1, current + delta);

  if (rangeStart > 0) {
    pages.push(0);
    if (rangeStart > 1) pages.push("...");
  }

  for (let i = rangeStart; i <= rangeEnd; i++) {
    pages.push(i);
  }

  if (rangeEnd < total - 1) {
    if (rangeEnd < total - 2) pages.push("...");
    pages.push(total - 1);
  }

  return pages;
};
