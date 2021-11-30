package com.bookmarkmanager.bookmarkmanager.db.repository;

import com.bookmarkmanager.bookmarkmanager.db.entity.Url;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url,Long> {
}
