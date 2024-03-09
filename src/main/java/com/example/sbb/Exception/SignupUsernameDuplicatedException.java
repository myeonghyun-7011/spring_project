package com.example.sbb.Exception;

public class SignupUsernameDuplicatedException extends RuntimeException {
  public SignupUsernameDuplicatedException(String message) {
    super(message);
  }
}