package com.example.sbb.question;

import com.example.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionService {

  private  final QuestionRepository questionRepository;

  public List<Question> getList() {
    return questionRepository.findAll();
  }

  // 예외처리 없으면 종료 시켜라.
  public Question getQuestion(int id) {
    return questionRepository.findById(id)
        .orElseThrow(() -> new DataNotFoundException("no %d question not found".formatted(id)));
  }

  public void create(String subject, String content) {
    Question q = new Question(); // Question class 에 q라는 객체를 생성하여 정보들을 저장.
    q.setSubject(subject);
    q.setContent(content);
    q.setCreateDate(LocalDateTime.now());
    questionRepository.save(q);
  }
}
