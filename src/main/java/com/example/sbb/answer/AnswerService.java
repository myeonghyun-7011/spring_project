package com.example.sbb.answer;

import com.example.sbb.DataNotFoundException;
import com.example.sbb.question.Question;
import com.example.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
  private final AnswerRepository answerRepository;

  public Answer create(Question question, String content, SiteUser author) {
    Answer answer = new Answer();
    answer.setContent(content);
    answer.setCreateDate(LocalDateTime.now());
    answer.setAuthor(author);
    question.addAnswer(answer); // 양방향 관계가됨.

    answerRepository.save(answer);

    return answer;
  }

  public Answer getAnswer(Long id) {
    return answerRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new DataNotFoundException("answer not found"));
  }

  public void modify(Answer answer, String content) {
    answer.setContent(content);
    answer.setModifyDate(LocalDateTime.now());
    answerRepository.save(answer);
  }

  public void delete(Answer answer) {
    answerRepository.delete(answer);
  }

  public void vote(Answer answer, SiteUser siteUser) {
    answer.getVoter().add(siteUser);
    answerRepository.save(answer);
  }
}
