package net.nuttle.notes.es;

public class SearchException extends Exception {
  
  /**
   * 
   */
  private static final long serialVersionUID = 7855118915923943879L;

  public SearchException(String msg) {
    super(msg);
  }
  
  public SearchException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
