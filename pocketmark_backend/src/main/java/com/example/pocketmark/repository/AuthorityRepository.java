package com.example.pocketmark.repository;

import com.example.pocketmark.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {

}
