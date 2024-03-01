package com.example.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class SbbApplicationTests {

  @Autowired
  private QuestionRepository questionRepository;

  //--------------질문 데이터 저장------------------------------------
  @Test
  void testJpa1() {
    Question q1 = new Question();
    q1.setSubject("sbb가 무엇인가요?");
    q1.setContent("sbb에 대해서 알고 싶습니다.");
    q1.setCreateDate(LocalDateTime.now());
    this.questionRepository.save(q1);  // 첫번째 질문 저장

    System.out.println(q1.getId());

    Question q2 = new Question();
    q2.setSubject("스프링부트 모델 질문입니다.");
    q2.setContent("id는 자동으로 생성되나요?");
    q2.setCreateDate(LocalDateTime.now());
    this.questionRepository.save(q2);  // 두번째 질문 저장

    assertThat(q1.getId()).isGreaterThan(0);
    assertThat(q1.getId()).isGreaterThan(q1.getId());
  }

  //------------------findAll-----------------------------------------
  @Test
  void testJpa2() {
    // SELECT * FROM question;
    // 찾은결과값이 여러개일 수 있기 때문에 List 작성
    List<Question> all = this.questionRepository.findAll();
    // data가 2개이기 때문에 2,
    assertEquals(2, all.size());
    // List에서 첫번째 값을 가져와서 비교. 참.
    Question q = all.get(0);
    assertEquals("sbb가 무엇인가요?", q.getSubject());
  }

  //--------------FindBySubject-----------------------------------------
  // questionRepository에 알아서 생성해줌.
  @Test
  void testJpa3() {
    Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
    assertEquals(1, q.getId());
  }

  //--------------FindBySubjectAndContent-----------------------------------------
  @Test
  void testJpa4() {
    Question q = this.questionRepository.findBySubjectAndContent(
        "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
    assertEquals(1, q.getId());
  }
//--------------FindBySubjectLike-----------------------------------------
@Test
void testJpa5() {
  List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
  Question q = qList.get(0);
  assertEquals("sbb가 무엇인가요?", q.getSubject());
}


}