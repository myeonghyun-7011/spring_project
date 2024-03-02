package com.example.sbb;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// @Controller 의미
// 개발자가 스프링부트 말한다.
// 아래 있는 MainController 컨트롤러이다.
@Controller
public class MainController {
  // private int count = 0;
  private int count;

  // 생성자 메서드를 이용해서
  // count++ 이기 때문에 -1 로 시작해야 페이지에 0 부터 나오게됨.
  public MainController() {
    count = -1;
  }

  //  @GetMapping("/home/main") 의 의미
  // 개발자가 스프링부트 말한다.
  // 만약에 /home/main/ 이런 요청이 들어 오면 아래 메서드를 실행해줘

  @GetMapping("/home/main")
  // @ResponseBody 의 의미X
  // 아래 메서드를 실행한 후 그 리턴값을 응답으로 삼아줘.
  @ResponseBody
  public String showHome() {
    return "안녕하세요.";
  }


  @PostMapping("/page2")
  @ResponseBody
  public String showPage2Post(@RequestParam(defaultValue = "0") int age) {
    return """
        <h1>입력된 나이 : %d</h1>
        <h1>안녕하세요. Post 방식으로 오신걸 환영합니다.</h1>
            
        """.formatted(age);
  }

  @GetMapping("/page2")
  @ResponseBody
  public String showPost(@RequestParam(defaultValue = "0") int age) {
    return """
        <h1>입력된 나이 : %d</h1>
        <h1>안녕하세요. Get 방식으로 오신걸 환영합니다.</h1>
              
                
        """.formatted(age);
  }

  @GetMapping("/plus")
  @ResponseBody
  public int showPlus(int a, int b) {
    return a + b;
  }

  @GetMapping("/plus2")
  @ResponseBody
  public void showPlus2(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int a = Integer.parseInt(req.getParameter("a"));
    int b = Integer.parseInt(req.getParameter("b"));

    resp.getWriter().append(a + b + "");
  }

  @GetMapping("/minus")
  @ResponseBody
  public int showMinus(int a, int b) {
    return a - b;
  }


  @GetMapping("/home/increase")
  @ResponseBody
  public int showIncrease() {
    count++;
    return count;
  }

  @GetMapping("/saveSession/{name}/{value}")
  @ResponseBody
  public String saveSession(@PathVariable String name, @PathVariable String value, HttpServletRequest req) {
    HttpSession session = req.getSession();
    session.setAttribute(name, value);

    return "세션변수 %s의 값이 %s로 설정되었습니다.".formatted(name, value);
  }

  @GetMapping("/getSession/{name}")
  @ResponseBody
  public String getSession(@PathVariable String name, HttpSession session) {
    // req => 쿠키 => JESSIONID => 세션을 얻을 수 있다.

    // 형변환 해줘야힘
    String value = (String) session.getAttribute(name);

    return "세션변수 %s의 값은 %s입니다.".formatted(name, value);
  }
//-----------------------------------------------------------------------------------------------------------

  // 저장공간 필요하기 때문에 list 만들어줘야함.
  //프로그램 시작하자마자 data 2개 생성
  // asList는 추가나 삭제가 되지않는다.
  // immutable = 수정할수없는
  //  private List<Article> articles = Arrays.asList(new Article("제목","내용"),new Article("제목","내용"));
  // new ArrayList<> 사용함으로써 mutable로 변경 , 수정 삭제 가능.
  // test data
  private List<Article> articles = new ArrayList<>(
      Arrays.asList(
          new Article("제목", "내용"),
          new Article("제목", "내용"))
  );

  //------------------addArticle---------------------------------------------------------------
  @GetMapping("/addArticle")
  @ResponseBody
  public String addArticle(String title, String body) {
    Article article = new Article(title, body); // 한 번 쓰면 없어짐.

    articles.add(article);

    return "%d번 게시물이 생성되었습니다.".formatted(article.getId());
  }

  //------------------------getArticle---------------------------------------------------------------
  // getArticle은 해당 data 를 뽑아와야 하기때문에 @PathVariable 사용.
  @GetMapping("/getArticle/{id}")
  @ResponseBody
  public Article getArticle(@PathVariable int id) {
    Article article = articles // id 가 1번인 게시물이 앞에서 3번째
        .stream()
        .filter(a -> a.getId() == id) // 1번
        .findFirst() // 찾은것중 하나만 출력
        .orElse(null); // null을 반환

    return article;
  }

  //---------------------modify-----------------------------------------------------------------------------
  @GetMapping("/modifyArticle/{id}")
  @ResponseBody
  public String modifyArticle(@PathVariable int id, String title, String body) {
    Article article = articles // id 가 1번인 게시물이 앞에서 3번째
        .stream()
        .filter(a -> a.getId() == id) // 1번
        .findFirst() // 찾은것중 하나만 출력
        .orElse(null); // null을 반환

    if (article == null) {
      return "%d번 게시물은 존재하지 않습니다.".formatted(id);
    }
    article.setTitle(title);
    article.setBody(title);

    return "%d번 게시물 수정하였습니다.".formatted(article.getId());
  }

  //---------------------delete-----------------------------------------------------------------------------
  @GetMapping("/deleteArticle/{id}")
  @ResponseBody
  public String deleteArticle(@PathVariable int id) {
    Article article = articles // id 가 1번인 게시물이 앞에서 3번째
        .stream()
        .filter(a -> a.getId() == id) // 1번
        .findFirst() // 찾은것중 하나만 출력
        .orElse(null); // null을 반환

    if (article == null) {
      return "%d번 게시물은 존재하지 않습니다.".formatted(id);
    }
    articles.remove(article);
    return "%d번 게시물이 삭제되었습니다.".formatted(article.getId());
  }

  //---------------------addPersonOldWay-----------------------------------------------------------------------------
  @GetMapping("/addPersonOldWay")
  @ResponseBody
  public Person addPersonOldWay(int id, int age, String name) {
    Person p = new Person(id, age, name);

    return p;
  }

  //---------------------addPerson-----------------------------------------------------------------------------
  @GetMapping("/addPerson")
  @ResponseBody
  public Person addPerson(Person p) {
    return p;
  }

}

//----------------생성자-----------------------------------------------------------------------------
@AllArgsConstructor
@Getter
@Setter
class Article {
  // static 변수는 프로그램이 실행될때 딱한번 최초로 실행
  private static int lastId = 0;

  private int id;
  private String title;
  private String body;

  // new Article 객체가 생성이되는 순간 값을 순서대로 꽂아 넣어줌.
  // lastId는 실행될때 0 이지만 ++붙어있기 때문에 1로 출력
  public Article(String title, String body) {
    this(++lastId, title, body);
  }

  //--------------------------------------------------------------------------------------------------



  @GetMapping("/")
  public String root() {
    return "redirect:/home";
  }
}


//---------------person 생성자----------------------------------------------------------------------------

@AllArgsConstructor
@NoArgsConstructor // 빈생성자일경우 null
@Getter
@Setter
class Person {
  private int id;
  private int age;
  private String name;

  // 2개의 인자를 넘겼을 경우
  public Person(int age, String name) {
    this.age = age;
    this.name = name;
  }
}
