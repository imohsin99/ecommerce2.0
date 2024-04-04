package com.example.userservice.Exceptions;

public class ServiceException extends RuntimeException{
  private String message;

  public ServiceException() {
  }

  public ServiceException(String message) {
    super(message);
    this.message = message;
  }
}
