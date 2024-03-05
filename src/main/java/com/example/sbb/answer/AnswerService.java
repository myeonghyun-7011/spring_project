package com.example.sbb.answer;

import com.example.sbb.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {

  private final AnswerRepository answerRepository;

  public void create(Question question, String content) {
    Answer answer = new Answer();
    answer.setContent(content);
    answer.setCreateDate(LocalDateTime.now());

    question.addAnswer(answer); // 양방향 관계가됨.

    answer.setQuestion(question);
    answerRepository.save(answer);
  }

}
