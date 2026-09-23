package org.example.library;

/** Reader */
public class Reader {

  private long id;
  private String passportNumber;
  private String fullname;

  public void setId(long id) {
    this.id = id;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public Reader() {}

  public Reader(String passportNumber, String fullname) {
    this.passportNumber = passportNumber;
    this.fullname = fullname;
  }

  public long getId() {
    return id;
  }

  public String getPassportNumber() {
    return passportNumber;
  }

  public String getFullname() {
    return fullname;
  }

  @Override
  public String toString() {
    return "Reader{passportNumber=" + passportNumber + ", fullname=" + fullname + "}";
  }
}
