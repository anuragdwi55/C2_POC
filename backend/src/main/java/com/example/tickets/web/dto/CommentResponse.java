package com.example.tickets.web.dto;

import java.time.Instant;
import java.util.UUID;

public record CommentResponse(UUID id, String author, String body, Instant createdAt) {
}
