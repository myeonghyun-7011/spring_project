package com.example.sbb.user;

import lombok.Getter;

// enum = class
// enum은 객체를 싱글톤으로 쓰고 싶다. + 객체의 개수가 무조건 정해져 있다.
// 자유대를 제한할때 enum을 사용.

@Getter
public enum UserRole {
  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");

  UserRole(String value) {
    this.value = value;
  }

  private String value;
}
