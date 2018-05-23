package net.nuttle.notes;

public class Note {

  private String noteid;
  private String title;
  private String category;
  private String tags;
  private String note;
  
  public String getNoteid() {
    return noteid;
  }
  
  public void setNoteid(String noteid) {
    this.noteid = noteid;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getCategory() {
    return category;
  }
  
  public String getTags() {
    return tags;
  }
  
  public String getNote() {
    return note;
  }
  
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Noteid=").append(noteid).append(", ")
      .append("title=").append(title).append(", ")
      .append("category=").append(category).append(", ")
      .append("tags=").append(tags).append(", ")
      .append("note=").append(note);
    return sb.toString();
  }
}
