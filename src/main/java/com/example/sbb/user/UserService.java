package com.example.sbb.user;

import com.example.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  // 스프링이 책임지고 passwordEncoder 타입의 객체를 만들어야 하는 상황
  // 스프링이 알아서 config 파일을 학습하고 @Bean을 확인함.

  private final PasswordEncoder passwordEncoder;


  public SiteUser create(String username, String email, String password) throws SignupUsernameDuplicatedException, SignupEmailDuplicatedException {
    SiteUser user = new SiteUser();

    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(password));

    // user 정보를 저장하고 에러가 뜨면 출력해라
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException e) {

      if(userRepository.existsByUsername(username)) {
        throw new SignupUsernameDuplicatedException("이미 사용중인 username 입니다.");
      } else {
        throw new SignupEmailDuplicatedException("이미 사용중인 email 입니다.");
      }
    }
    return user;
  }

  //
  // username을 찾으면 레포지토리에 저장, 못 넣어주면 exception 출력
  public SiteUser getUser(String username) {
    return userRepository.findByUsername(username).orElseThrow(() ->
        new DataNotFoundException("siteuser not found")
    );
  }
}