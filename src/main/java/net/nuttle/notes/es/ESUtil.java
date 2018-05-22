package net.nuttle.notes.es;

import org.elasticsearch.action.search.SearchResponse;

import net.nuttle.notes.Note;

public interface ESUtil {

  SearchResponse search(String index, String query) throws SearchException;
  
  String fetch(String index, String noteid) throws SearchException;
  
  void indexTestNotes(String index) throws SearchException;
  
  void index(String index, Note note) throws SearchException;
  
  void update(String index, String noteid, String note) throws SearchException;
  
  boolean createIndex(String index) throws SearchException;
  
  void createMappings(String index, String mappingsFile) throws SearchException;
  
  void createAlias(String index, String alias) throws SearchException;
  
  boolean isIndexExists(String index) throws SearchException;
  

}
