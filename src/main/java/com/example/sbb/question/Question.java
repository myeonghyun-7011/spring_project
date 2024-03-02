package com.example.sbb.question;

import com.example.sbb.answer.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity // 아래 Question 클래스는 엔티티 클래스이다.
// 아래 클래스와 1:1로 매칭되는 테이블이 DB에 없다면, 자동으로 생성되어야 한다.
public class Question {
  @Id // Primary key
  @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
  private Integer id;

  @Column(length = 200) // Varchar(200)
  private String subject;

  @Column(columnDefinition = "TEXT")
  private String content;

  private LocalDateTime createDate;

  //OneToMany는 원래 fetch가 LAZY(지연로딩) = >  fetch = FetchType.EAGER(즉시로딩) 변경
  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  private List<Answer> answerList = new ArrayList<>();

  public void addAnswer(Answer answer) {
    answer.setQuestion(this);
    getAnswerList().add(answer);
  }
}
