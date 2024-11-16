package com.duonghoang.shopapp_backend.repositories;

import com.duonghoang.shopapp_backend.models.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
}
