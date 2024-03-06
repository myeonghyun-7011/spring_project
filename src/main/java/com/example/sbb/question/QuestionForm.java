package com.example.sbb.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionForm {

  // @Vaild Rule에 해당함. ,QuestionForm class를 사용하여 룰을 객체 생성.
  @NotEmpty(message="프로젝트제목은 필수항목입니다.")
  @Size(max=200, message = "프로젝트 제목을 200자 이하로 입력.")
  private String content;

  @NotEmpty(message="프로젝트내용은 필수항목입니다.")
  private String subject;
}

