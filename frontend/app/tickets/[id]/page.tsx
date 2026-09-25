"use client";

import Link from "next/link";
import { useParams } from "next/navigation";
import { FormEvent, useCallback, useEffect, useState } from "react";
import {
  ApiClientError,
  PRIORITIES,
  STATUS_LABELS,
  addComment,
  getAllowedTransitions,
  getTicket,
  updateStatus,
  updateTicket,
  type Priority,
  type Ticket,
  type TicketStatus,
} from "@/lib/api";

export default function TicketDetailPage() {
  const params = useParams();
  const id = params.id as string;

  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [allowed, setAllowed] = useState<TicketStatus[]>([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<Priority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [commentAuthor, setCommentAuthor] = useState("");
  const [commentBody, setCommentBody] = useState("");
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [banner, setBanner] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const refresh = useCallback(async () => {
    setLoading(true);
    setBanner(null);
    try {
      const [t, next] = await Promise.all([
        getTicket(id),
        getAllowedTransitions(id),
      ]);
      setTicket(t);
      setAllowed(next);
      setTitle(t.title);
      setDescription(t.description);
      setPriority(t.priority);
      setAssignee(t.assignee ?? "");
    } catch (e) {
      setBanner(e instanceof ApiClientError ? e.message : "Failed to load ticket");
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    refresh();
  }, [refresh]);

  async function saveDetails(e: FormEvent) {
    e.preventDefault();
    setFieldErrors({});
    setBanner(null);
    try {
      const updated = await updateTicket(id, {
        title,
        description,
        priority,
        assignee,
      });
      setTicket((prev) =>
        prev ? { ...prev, ...updated, comments: prev.comments } : updated
      );
    } catch (err) {
      if (err instanceof ApiClientError) {
        setBanner(err.message);
        const map: Record<string, string> = {};
        err.fieldErrors.forEach((fe) => {
          map[fe.field] = fe.message;
        });
        setFieldErrors(map);
      }
    }
  }

  async function onTransition(status: TicketStatus) {
    setBanner(null);
    try {
      await updateStatus(id, status);
      await refresh();
    } catch (err) {
      setBanner(err instanceof ApiClientError ? err.message : "Status update failed");
    }
  }

  async function onAddComment(e: FormEvent) {
    e.preventDefault();
    setFieldErrors({});
    setBanner(null);
    try {
      await addComment(id, { author: commentAuthor, body: commentBody });
      setCommentBody("");
      await refresh();
    } catch (err) {
      if (err instanceof ApiClientError) {
        setBanner(err.message);
        const map: Record<string, string> = {};
        err.fieldErrors.forEach((fe) => {
          map[fe.field] = fe.message;
        });
        setFieldErrors(map);
      }
    }
  }

  if (loading && !ticket) {
    return <p>Loading…</p>;
  }

  if (!ticket) {
    return (
      <div className="card">
        {banner && <div className="banner-error">{banner}</div>}
        <Link href="/">← Back</Link>
      </div>
    );
  }

  return (
    <div className="card">
      <p>
        <Link href="/">← Back to list</Link>
      </p>
      <h1>{ticket.title}</h1>
      <p>
        Status: <span className="badge">{STATUS_LABELS[ticket.status]}</span> ·{" "}
        Priority: {ticket.priority}
      </p>

      {banner && <div className="banner-error">{banner}</div>}

      <form onSubmit={saveDetails}>
        <div className="field">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
          {fieldErrors.title && (
            <div className="field-error">{fieldErrors.title}</div>
          )}
        </div>
        <div className="field">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            rows={5}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        <div className="field">
          <label htmlFor="priority">Priority</label>
          <select
            id="priority"
            value={priority}
            onChange={(e) => setPriority(e.target.value as Priority)}
          >
            {PRIORITIES.map((p) => (
              <option key={p} value={p}>
                {p}
              </option>
            ))}
          </select>
        </div>
        <div className="field">
          <label htmlFor="assignee">Assignee</label>
          <input
            id="assignee"
            value={assignee}
            onChange={(e) => setAssignee(e.target.value)}
          />
        </div>
        <button className="btn btn-primary" type="submit">
          Save changes
        </button>
      </form>

      <div className="actions">
        {allowed.map((s) => (
          <button
            key={s}
            type="button"
            className="btn"
            onClick={() => onTransition(s)}
          >
            Move to {STATUS_LABELS[s]}
          </button>
        ))}
        {allowed.length === 0 && (
          <span>No further status transitions available.</span>
        )}
      </div>

      <section className="comments">
        <h2>Comments</h2>
        {(ticket.comments ?? []).map((c) => (
          <div key={c.id} className="comment">
            <div className="comment-meta">
              {c.author} · {new Date(c.createdAt).toLocaleString()}
            </div>
            <p>{c.body}</p>
          </div>
        ))}

        <form onSubmit={onAddComment}>
          <div className="field">
            <label htmlFor="author">Your name</label>
            <input
              id="author"
              value={commentAuthor}
              onChange={(e) => setCommentAuthor(e.target.value)}
              required
            />
            {fieldErrors.author && (
              <div className="field-error">{fieldErrors.author}</div>
            )}
          </div>
          <div className="field">
            <label htmlFor="body">Comment</label>
            <textarea
              id="body"
              rows={3}
              value={commentBody}
              onChange={(e) => setCommentBody(e.target.value)}
              required
            />
            {fieldErrors.body && (
              <div className="field-error">{fieldErrors.body}</div>
            )}
          </div>
          <button className="btn btn-primary" type="submit">
            Add comment
          </button>
        </form>
      </section>
    </div>
  );
}
