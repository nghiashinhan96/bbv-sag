package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.Login;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Integer> {
}
