package com.example.sbb;

import com.example.sbb.base.RepositoryUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer>, RepositoryUtil {

  Question findBySubject(String subject);

  Question findBySubjectAndContent(String subject, String content);

  List<Question> findBySubjectLike(String subject);

  //--------------truncate를 위한 Foreign key 비활성화,활성화------------------------------------
  @Transactional
  @Modifying
  @Query(value = "truncate question", nativeQuery = true)
  void truncate();
}