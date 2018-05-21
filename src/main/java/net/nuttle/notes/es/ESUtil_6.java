package net.nuttle.notes.es;

import org.elasticsearch.client.Response;

public interface ESUtil_6 {

  Response search(String index, String query) throws SearchException;
  
  void indexTestDocs(String index) throws SearchException;
  
  boolean createIndex(String index) throws SearchException;
  
  void createMappings(String index, String mappingsFile) throws SearchException;
  
  void createAlias(String index, String alias) throws SearchException;
  
  boolean isIndexExists(String index) throws SearchException;
}
