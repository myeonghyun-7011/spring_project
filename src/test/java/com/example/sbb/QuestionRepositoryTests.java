package com.example.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class QuestionRepositoryTests {
  @Autowired
  private QuestionRepository questionRepository;
  private static int lastSampleDataId;

  @BeforeEach
    // 프로그램 실행할때 딱 한번 실행
  void beforeEach() {
    clearData();
    createSampleData();
  }
//-----------------------------------------------------------------------------------------
// static (본사직원생성)
  public static int createSampleData(QuestionRepository questionRepository) {
    Question q1 = new Question();
    q1.setSubject("sbb가 무엇인가요?");
    q1.setContent("sbb에 대해서 알고 싶습니다.");
    q1.setCreateDate(LocalDateTime.now());
    questionRepository.save(q1);  // 첫번째 질문 저장

    Question q2 = new Question();
    q2.setSubject("스프링부트 모델 질문입니다.");
    q2.setContent("id는 자동으로 생성되나요?");
    q2.setCreateDate(LocalDateTime.now());
    questionRepository.save(q2);  // 두번째 질문 저장

    return q2.getId();
  }
  private void createSampleData() {
    lastSampleDataId = createSampleData(questionRepository);
  }
//-----------------------------------------------------------------------------------------
  // static은 본사 직원이기 때문에  questionRepository(본사직원이아님.)
  // clearData(QuestionRepository questionRepository) 넘겨줘야함.
  public static void clearData(QuestionRepository questionRepository) {
    questionRepository.truncateTable();
  }

  private void clearData() {
    clearData(questionRepository);
  }
//-----------------------------------------------------------------------------------------
  @Test
  void 저장() {
    Question q1 = new Question();
    q1.setSubject("sbb가 무엇인가요?");
    q1.setContent("sbb에 대해서 알고 싶습니다.");
    q1.setCreateDate(LocalDateTime.now());
    questionRepository.save(q1);  // 첫번째 질문 저장

    Question q2 = new Question();
    q2.setSubject("스프링부트 모델 질문입니다.");
    q2.setContent("id는 자동으로 생성되나요?");
    q2.setCreateDate(LocalDateTime.now());
    questionRepository.save(q2);  // 두번째 질문 저장

    // sampledata 증가 현재 1 ,2 생성 다음 3,4 생성하게끔.
    assertThat(q1.getId()).isEqualTo(lastSampleDataId + 1);
    assertThat(q2.getId()).isEqualTo(lastSampleDataId + 2);
  }

  @Test
  void 삭제() {
    assertThat(questionRepository.count()).isEqualTo(lastSampleDataId);
    Question q = questionRepository.findById(1).get(); // 그중에 1번을 찾고 oq 에 넣어줌
    questionRepository.delete(q); // 삭제

    assertThat(questionRepository.count()).isEqualTo(lastSampleDataId - 1);
  }
  @Test
  void 수정() {
    assertThat(questionRepository.count()).isEqualTo(lastSampleDataId);

    Question q = questionRepository.findById(1).get(); // 그중에 1번을 찾고 oq 에 넣어줌
    q.setSubject("수정된 제목");
    questionRepository.save(q);

    q = questionRepository.findById(1).get();
    assertThat(q.getSubject()).isEqualTo("수정된 제목");
  }
  @Test
  void findAll() {
    List<Question> all = questionRepository.findAll();
    assertThat(all.size()).isEqualTo(lastSampleDataId);

    Question q = all.get(0);
    assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
  }
  @Test
  void findBySubject() {
    Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
    assertThat(q.getId()).isEqualTo(1);
  }

  @Test
  void findBySubjectAndContent() {
    Question q = this.questionRepository.findBySubjectAndContent(
        "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
    assertThat(q.getId()).isEqualTo(1);
  }


  @Test
  void findBySubjectLike() {
    List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
    Question q = qList.get(0);
    assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
  }

}

/*
  //--------------질문 데이터 저장------------------------------------
  @Test
  void testJpa0() {
    Question q1 = new Question();
    q1.setSubject("sbb가 무엇인가요?");
    q1.setContent("sbb에 대해서 알고 싶습니다.");
    q1.setCreateDate(LocalDateTime.now());
    this.questionRepository.save(q1);  // 첫번째 질문 저장

    Question q2 = new Question();
    q2.setSubject("스프링부트 모델 질문입니다.");
    q2.setContent("id는 자동으로 생성되나요?");
    q2.setCreateDate(LocalDateTime.now());
    this.questionRepository.save(q2);  // 두번째 질문 저장

    assertThat(q1.getId()).isGreaterThan(0);
    assertThat(q2.getId()).isGreaterThan(q1.getId());
  }


  //--------------질문 데이터 깔끔히 삭제 테스트케이스------------------------------------

  @Test
  void testJpa1() {
    this.questionRepository.disableForeignKeyCheck();
    this.questionRepository.truncate();
    this.questionRepository.enableForeignKeyCheck();
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

  //--------------질문데이터 수정하기-----------------------------------------
  @Test
  void testJpa6() {
    Optional<Question> oq = this.questionRepository.findById(1); // optional 포장용기 라고생각
    assertTrue(oq.isPresent());
    Question q = oq.orElse(null); // 데이터가 있으면 들어오고 없으면 null(유연성)
    q.setSubject("수정된 제목"); // q = 영속객체 Id를 포함하고있음.
    this.questionRepository.save(q);
  }

  //--------------질문데이터 삭제하기-----------------------------------------
  @Test
  void testJpa7() {
    assertEquals(2, this.questionRepository.count()); // data 개수 count , 2개만 있기 때문에 2
    Optional<Question> oq = this.questionRepository.findById(1); // 그중에 1번을 찾고 oq 에 넣어줌
    assertTrue(oq.isPresent());
    Question q = oq.get(); // 찾은걸 다시 q에 담아
    this.questionRepository.delete(q); // 삭제
    assertEquals(1, this.questionRepository.count()); // 다시 count 하면 1개
  }

  // Question q = new Quesion();
  // quesiontRepository.save(q): // Insert

  // Question q = questionRepository.findById(1).get();
  // q.setSubject("Hi")
  // quesiontRepository.save(q): // Update

  // Question q = questionRepository.findById(4).get();

  // quesiontRepository.delete(q): // Delete

  //delete from question where id = 4
//-------------------------------------------------------------------------------------------------

 */


