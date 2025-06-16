import { format } from "date-fns";

export function formatDate(date: string | number | Date | undefined): string {
  if (!date) return "";
  try {
    return format(new Date(date), "M/d/yy, h:mm a");
  } catch {
    return "";
  }
}