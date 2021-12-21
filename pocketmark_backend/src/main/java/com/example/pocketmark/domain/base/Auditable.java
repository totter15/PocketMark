package com.example.pocketmark.domain.base;

import java.time.LocalDateTime;

public interface Auditable {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    void setCreatedAt(LocalDateTime now);
    void setUpdatedAt(LocalDateTime now);
}
