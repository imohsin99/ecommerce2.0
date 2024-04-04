package com.example.commentservice.Exceptions;

public class ServiceException extends RuntimeException{
  private String message;

  public ServiceException() {
  }

  public ServiceException(String message) {
    super(message);
    this.message = message;
  }
}
