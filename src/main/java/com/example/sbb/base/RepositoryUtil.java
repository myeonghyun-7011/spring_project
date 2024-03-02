package com.example.sbb.base;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryUtil {
  // 비활성화
  @Transactional
  @Modifying
  @Query(value = "SET FOREIGN_KEY_CHECKS = 0", nativeQuery = true)
  void disableForeignKeyCheck();

  // 활성화
  @Transactional
  @Modifying
  @Query(value = "SET FOREIGN_KEY_CHECKS = 1", nativeQuery = true)
  void enableForeignKeyCheck();

  default void truncateTable() {
   // disableForeignKeyCheck();
    truncate();
   // enableForeignKeyCheck();
  }

  void truncate();
}
