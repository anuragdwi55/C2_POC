export type Priority = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";
export type TicketStatus =
  | "OPEN"
  | "IN_PROGRESS"
  | "RESOLVED"
  | "CLOSED"
  | "CANCELLED";

export interface Comment {
  id: string;
  author: string;
  body: string;
  createdAt: string;
}

export interface Ticket {
  id: string;
  title: string;
  description: string;
  priority: Priority;
  status: TicketStatus;
  assignee: string | null;
  createdAt: string;
  updatedAt: string;
  comments?: Comment[];
}

export interface PagedTickets {
  content: Ticket[];
  page: number;
  size: number;
  totalElements: number;
}

export interface FieldError {
  field: string;
  message: string;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  fieldErrors: FieldError[];
}

export class ApiClientError extends Error {
  status: number;
  fieldErrors: FieldError[];

  constructor(status: number, message: string, fieldErrors: FieldError[] = []) {
    super(message);
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

const baseUrl =
  process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api/v1";

async function parseError(res: Response): Promise<never> {
  let body: ApiError | null = null;
  try {
    body = (await res.json()) as ApiError;
  } catch {
    /* ignore */
  }
  throw new ApiClientError(
    res.status,
    body?.message ?? res.statusText,
    body?.fieldErrors ?? []
  );
}

export async function listTickets(params: {
  q?: string;
  status?: TicketStatus | "";
  page?: number;
  size?: number;
}): Promise<PagedTickets> {
  const search = new URLSearchParams();
  if (params.q) search.set("q", params.q);
  if (params.status) search.set("status", params.status);
  search.set("page", String(params.page ?? 0));
  search.set("size", String(params.size ?? 20));
  const res = await fetch(`${baseUrl}/tickets?${search.toString()}`, {
    cache: "no-store",
  });
  if (!res.ok) await parseError(res);
  return res.json();
}

export async function getTicket(id: string): Promise<Ticket> {
  const res = await fetch(`${baseUrl}/tickets/${id}`, { cache: "no-store" });
  if (!res.ok) await parseError(res);
  return res.json();
}

export async function getAllowedTransitions(id: string): Promise<TicketStatus[]> {
  const res = await fetch(`${baseUrl}/tickets/${id}/allowed-transitions`, {
    cache: "no-store",
  });
  if (!res.ok) await parseError(res);
  return res.json();
}

export async function createTicket(body: {
  title: string;
  description: string;
  priority: Priority;
  assignee?: string;
}): Promise<Ticket> {
  const res = await fetch(`${baseUrl}/tickets`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) await parseError(res);
  return res.json();
}

export async function updateTicket(
  id: string,
  body: Partial<{
    title: string;
    description: string;
    priority: Priority;
    assignee: string;
  }>
): Promise<Ticket> {
  const res = await fetch(`${baseUrl}/tickets/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) await parseError(res);
  return res.json();
}

export async function updateStatus(
  id: string,
  status: TicketStatus
): Promise<Ticket> {
  const res = await fetch(`${baseUrl}/tickets/${id}/status`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ status }),
  });
  if (!res.ok) await parseError(res);
  return res.json();
}

export async function addComment(
  id: string,
  body: { author: string; body: string }
): Promise<Comment> {
  const res = await fetch(`${baseUrl}/tickets/${id}/comments`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) await parseError(res);
  return res.json();
}

export const STATUS_LABELS: Record<TicketStatus, string> = {
  OPEN: "Open",
  IN_PROGRESS: "In progress",
  RESOLVED: "Resolved",
  CLOSED: "Closed",
  CANCELLED: "Cancelled",
};

export const PRIORITIES: Priority[] = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];
