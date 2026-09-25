"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";
import {
  ApiClientError,
  PRIORITIES,
  createTicket,
  type Priority,
} from "@/lib/api";

export default function NewTicketPage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<Priority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [banner, setBanner] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setFieldErrors({});
    setBanner(null);
    setSubmitting(true);
    try {
      const ticket = await createTicket({
        title,
        description,
        priority,
        assignee: assignee || undefined,
      });
      router.push(`/tickets/${ticket.id}`);
    } catch (err) {
      if (err instanceof ApiClientError) {
        setBanner(err.message);
        const map: Record<string, string> = {};
        err.fieldErrors.forEach((fe) => {
          map[fe.field] = fe.message;
        });
        setFieldErrors(map);
      } else {
        setBanner("Could not create ticket");
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="card">
      <p>
        <Link href="/">← Back to list</Link>
      </p>
      <h1>Create ticket</h1>
      {banner && <div className="banner-error">{banner}</div>}
      <form onSubmit={onSubmit}>
        <div className="field">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
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
            required
          />
          {fieldErrors.description && (
            <div className="field-error">{fieldErrors.description}</div>
          )}
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
          <label htmlFor="assignee">Assignee (optional)</label>
          <input
            id="assignee"
            value={assignee}
            onChange={(e) => setAssignee(e.target.value)}
          />
        </div>
        <button className="btn btn-primary" type="submit" disabled={submitting}>
          {submitting ? "Creating…" : "Create"}
        </button>
      </form>
    </div>
  );
}
