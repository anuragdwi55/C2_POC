"use client";

import Link from "next/link";
import { useCallback, useEffect, useState } from "react";
import {
  listTickets,
  STATUS_LABELS,
  type Ticket,
  type TicketStatus,
  ApiClientError,
} from "@/lib/api";

const STATUSES: (TicketStatus | "")[] = [
  "",
  "OPEN",
  "IN_PROGRESS",
  "RESOLVED",
  "CLOSED",
  "CANCELLED",
];

export default function TicketListPage() {
  const [q, setQ] = useState("");
  const [status, setStatus] = useState<TicketStatus | "">("");
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const page = await listTickets({ q, status, page: 0, size: 50 });
      setTickets(page.content);
    } catch (e) {
      setError(e instanceof ApiClientError ? e.message : "Failed to load tickets");
    } finally {
      setLoading(false);
    }
  }, [q, status]);

  useEffect(() => {
    const t = setTimeout(load, 300);
    return () => clearTimeout(t);
  }, [load]);

  return (
    <div className="card">
      <div className="toolbar">
        <input
          type="search"
          placeholder="Search by keyword…"
          value={q}
          onChange={(e) => setQ(e.target.value)}
          aria-label="Search tickets"
          style={{ flex: 1, minWidth: "12rem" }}
        />
        <select
          value={status}
          onChange={(e) => setStatus(e.target.value as TicketStatus | "")}
          aria-label="Filter by status"
        >
          {STATUSES.map((s) => (
            <option key={s || "all"} value={s}>
              {s ? STATUS_LABELS[s] : "All statuses"}
            </option>
          ))}
        </select>
        <Link className="btn btn-primary" href="/tickets/new">
          New ticket
        </Link>
      </div>

      {error && <div className="banner-error">{error}</div>}
      {loading && <p>Loading…</p>}

      {!loading && tickets.length === 0 && <p>No tickets found.</p>}

      {!loading && tickets.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Status</th>
              <th>Priority</th>
              <th>Assignee</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map((t) => (
              <tr key={t.id}>
                <td>
                  <Link href={`/tickets/${t.id}`}>{t.title}</Link>
                </td>
                <td>
                  <span className="badge">{STATUS_LABELS[t.status]}</span>
                </td>
                <td>{t.priority}</td>
                <td>{t.assignee ?? "—"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
