package com.duonghoang.shopapp_backend.repositories;

import com.duonghoang.shopapp_backend.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
