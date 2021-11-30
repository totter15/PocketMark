package com.bookmarkmanager.bookmarkmanager.db.listener;

import java.time.LocalDateTime;

public interface Auditable {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    void setCreatedAt(LocalDateTime now);
    void setUpdatedAt(LocalDateTime now);
}
