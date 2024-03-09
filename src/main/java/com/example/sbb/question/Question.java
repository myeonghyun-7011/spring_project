package com.example.sbb.question;

import com.example.sbb.answer.Answer;
import com.example.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

  @CreatedDate
  private LocalDateTime createDate;
  private LocalDateTime modifyDate;

  //OneToMany는 원래 fetch가 LAZY(지연로딩) = >  fetch = FetchType.EAGER(즉시로딩) 변경
  // 질문 하나에 여러게 답변
  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  private List<Answer> answerList = new ArrayList<>();

  //author 속성에는 @ManyToOne 애너테이션을 적용했는데, 이는 사용자 한 명이 질문을 여러 개 작성할 수 있기 때문이다.
  // 글쓴이
  @ManyToOne
  private SiteUser author;

  @ManyToMany
  Set<SiteUser> voter;

  public void addAnswer(Answer answer) {
    answer.setQuestion(this);
    getAnswerList().add(answer);
  }


}
