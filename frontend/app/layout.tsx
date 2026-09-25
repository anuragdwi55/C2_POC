import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Support Tickets",
  description: "Support Ticket Management System",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <header className="site-header">
          <a href="/">Support Tickets</a>
        </header>
        <main className="container">{children}</main>
      </body>
    </html>
  );
}
