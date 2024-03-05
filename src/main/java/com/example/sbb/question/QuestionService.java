package com.example.sbb.question;

import com.example.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
