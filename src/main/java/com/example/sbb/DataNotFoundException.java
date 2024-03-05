package com.example.sbb;

// 예외가 있을 때 보기 좋게 시각화 해주기 위해 사용함.
// RuntimeException => runtime 계열 => 평소에 대비하지 않아도 되는 그 외 계열
// 평소에 대비해야 하는
public class DataNotFoundException extends RuntimeException {

  public DataNotFoundException(String questionNotFound) {
  }
}
