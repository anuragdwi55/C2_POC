# UI flows

## Pages

1. **/** — Ticket list
   - Table: title, status, priority, assignee, updated
   - Search input (debounced) → `q`
   - Status filter dropdown → `status`
   - Link to create, link to detail

2. **/tickets/new** — Create ticket
   - Form: title, description, priority, assignee
   - Show field errors from API

3. **/tickets/[id]** — Detail
   - View/edit title, description, priority, assignee (save)
   - Status actions: buttons only for **legal** next states (derived from current status)
   - Comments list + add comment form
   - Show transition errors (e.g. invalid status) in alert banner

## Error UX

- 400: inline field errors
- 409 status: banner with backend `message`
- Network: generic retry message
