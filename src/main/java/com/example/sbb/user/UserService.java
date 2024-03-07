package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
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


  public SiteUser create(String username, String email, String password) {
    SiteUser user = new SiteUser();

    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPassword(passwordEncoder.encode(password));
    this.userRepository.save(user);

    return user;
  }
}