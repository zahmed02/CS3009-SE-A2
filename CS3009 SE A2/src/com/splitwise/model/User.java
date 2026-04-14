package com.splitwise.model;
import java.util.*;

public class User {
  private String userId;
  private String name;
  private String email;
  private Set<String> contactNumbers;

  public User(String userId, String name, String email) {
    this.userId = userId;
    this.name = name;
    setEmail(email);
    this.contactNumbers = new HashSet<>();
  }

  public String getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Set<String> getContactNumbers() {
    return contactNumbers;
  }

  public void setEmail(String email) {
    if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
      throw new IllegalArgumentException("Invalid email");
    this.email = email;
  }

  public void addContactNumber(String number) {
    if (number == null || number.trim().isEmpty())
      throw new IllegalArgumentException("Contact number cannot be empty");
    contactNumbers.add(number);
  }

  public void removeContactNumber(String number) {
    contactNumbers.remove(number);
  }
}