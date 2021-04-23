package com.example.OrderProcessing;

class CardNotFoundException extends RuntimeException {

  CardNotFoundException(String num) {
    super("Could not find employee " + num);
  }
}