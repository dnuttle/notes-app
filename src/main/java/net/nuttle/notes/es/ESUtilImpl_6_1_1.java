package net.nuttle.notes.es;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Hashtable;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import net.nuttle.notes.Note;

@Service
public class ESUtilImpl_6_1_1 implements ESUtil {
  
  private static final Logger LOG = LoggerFactory.getLogger(ESUtilImpl_6_1_1.class);
  public static final String TYPE = "note";

  @Override
  public SearchResponse search(String index, String query) throws SearchException {
    try (RestHighLevelClient client = getClient()) {
      SearchRequest req = new SearchRequest(index);
      SearchSourceBuilder builder = new SearchSourceBuilder();
      builder.query(QueryBuilders.queryStringQuery(query));
      req.source(builder);
      return client.search(req);
    } catch (IOException e) {
      throw new SearchException("Error searching", e);
    }
  }
  
  @Override
  public void index(String index, Note note) throws SearchException {
    try (RestHighLevelClient client = getClient()) {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.valueToTree(note);
      IndexRequest req = new IndexRequest(index, TYPE);
      req.source(node, XContentType.JSON);
      IndexResponse resp = client.index(req);
      LOG.info("Indexed ID: " + resp.getId());
    } catch (IOException e) {
      throw new SearchException("Error indexing note", e);
    }
  }
  
  @Override
  public void update(String index, String noteid, String note) throws SearchException {
    try (RestHighLevelClient client = getClient()) {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.valueToTree(note);
      UpdateRequest req = new UpdateRequest(index, TYPE, noteid);
      LOG.info("JSON: " + note);
      req.doc(note, XContentType.JSON);
      client.update(req);
    } catch (IOException e) {
      throw new SearchException("Error updating note", e);
    }
  }
  
  public void indexTestNotes(String index) throws SearchException {
    try (RestHighLevelClient client = getClient()) {
      ObjectMapper mapper = new ObjectMapper();
      String jsonFile = System.getProperty("user.dir") + "/src/main/resources/notes.json";
      ArrayNode notes = (ArrayNode) mapper.readTree(new File(jsonFile));
      for (JsonNode node : notes) {
        IndexRequest req = new IndexRequest(index, TYPE);
        req.source(mapper.writeValueAsString(node), XContentType.JSON);
        IndexResponse resp = client.index(req);
        LOG.info("Indexed ID: " + resp.getId()); 
      }
    } catch (IOException e) {
      throw new SearchException("Error indexing test notes", e);
    }
    return;
  }
  
  @Override
  public boolean createIndex(String index) throws SearchException {
    try (RestClient client = getLowLevelClient()) {
      if (isIndexExists(client, index)) {
        LOG.info("Index already exists");
        return false;
      }
      Response resp = client.performRequest("PUT", "/" + index);
      LOG.info("Status code: " + resp.getStatusLine().getStatusCode());
      if (resp.getStatusLine().getStatusCode() < 400) {
        return true;
      }
      return false;
    } catch (IOException e) {
      throw new SearchException("Error creating index", e);
    }
  }
  
  @Override
  public void createMappings(String index, String mappingsFile) throws SearchException {
    try (RestClient client = getLowLevelClient()) {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode json = mapper.readTree(new File(mappingsFile));
      LOG.info(mapper.writeValueAsString(json));
      StringEntity entity = new StringEntity(mapper.writeValueAsString(json), ContentType.APPLICATION_JSON);
      client.performRequest("PUT", "/" + index + "/" + TYPE + "/_mappings", new Hashtable<>(), entity);
    } catch (IOException e) {
      throw new SearchException("Error creating mappings", e);
    }
  }
  
  @Override
  public void createAlias(String index, String alias) throws SearchException {
    return;
  }
  
  private boolean isIndexExists(RestClient client, String index) throws SearchException {
    try {
      Response resp = client.performRequest("GET", "/_cat/indices?format=json");
      StringWriter writer = new StringWriter    ();
      IOUtils.copy(resp.getEntity().getContent(), writer, "utf-8");
      ObjectMapper mapper = new ObjectMapper();
      ArrayNode indices = (ArrayNode) mapper.readTree(writer.toString());
      for (JsonNode node : indices) {
        if (node.get("index").asText().equals(index)) {
          return true;
        }
      }
      return false;
    } catch (IOException e) {
      throw new SearchException("Error checking index", e);
    }
  }
  
  @Override
  public boolean isIndexExists(String index) throws SearchException {
    try (RestClient client = getLowLevelClient()) {
      return isIndexExists(client, index);
    } catch (IOException e) {
      throw new SearchException("Error checking index", e);
    }
  }

  public RestHighLevelClient getClient() {
    return new RestHighLevelClient(getRestClientBuilder());
  }
  
  public RestClient getLowLevelClient() {
    return getRestClientBuilder().build();
  }
  
  public RestClientBuilder getRestClientBuilder() {
    String host = System.getenv("NOTES_ES_HOST");
    int port = Integer.parseInt(System.getenv("NOTES_ES_PORT"));
    String scheme = System.getenv("NOTES_ES_SCHEME");
    return RestClient.builder(new HttpHost(host, port, scheme));
  }
}
