package net.nuttle.notes.es;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MetaData;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class ESUtilImpl_2_4 /*implements ESUtil*/ {
/*
 * I have never seen any software framework so casually, recklessly indifferent to atomizing
 * backward compatibility as elasticsearch
  private static final Logger LOG = LoggerFactory.getLogger(ESUtilImpl_2_4.class);
  
  public SearchResponse search(String index, String queryString) throws SearchException {
    try (Client client = getClient()) {
      SearchResponse resp = client.prepareSearch(index).setTypes("doc").setSearchType(SearchType.DEFAULT)
        .setQuery(QueryBuilders.queryStringQuery(queryString))
        .execute().actionGet();
      return resp;
    } catch (UnknownHostException e) {
      throw new SearchException("Error searching", e);
    }
  }
  
  public void indexTestDocs(String index) throws SearchException {
    try (Client client = getClient()) {
      ObjectMapper mapper = new ObjectMapper();
      String jsonFile = System.getProperty("user.dir") + "/src/main/resources/docs.json";
      ArrayNode docs = (ArrayNode) mapper.readTree(new File(jsonFile));
      BulkRequestBuilder bulkBuilder = client.prepareBulk();
      for (JsonNode node : docs) {
        LOG.info(node.get("docid").asText());
        Iterator<String> it = node.fieldNames();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
          sb.append(it.next()).append(", ");
        }
        LOG.info(sb.toString());
        bulkBuilder.add(client.prepareIndex(index, "doc", node.get("docid").asText())
          .setSource(mapper.writeValueAsBytes(node)));
        try {
          IndexResponse ir = client.prepareIndex(index, "doc", node.get("docid").asText())
            .setSource(mapper.writeValueAsBytes(node)).execute().actionGet();
        } catch (Exception e) {
          LOG.error("Error", e);
        }
      }
      if (true) return;
      bulkBuilder.setTimeout("1m");
      BulkResponse resp = bulkBuilder.execute().actionGet();
      for (BulkItemResponse itemResp : resp.getItems()) {
        if (itemResp.isFailed()) {
          LOG.info(itemResp.getFailureMessage());
        }
      }
    } catch (IOException e) {
      throw new SearchException("Error indexing", e);
    }
  }
  
  public boolean createIndex(String index) throws SearchException {
    try (Client client = getClient()) {
      if (isIndexExists(client, index)) {
        LOG.info("Index " + index + " already exists, no need to create");
        return false;
      }
      CreateIndexResponse resp = client.admin().indices().prepareCreate(index).get();
      if (resp.isAcknowledged()) {
        LOG.info("Index " + index + " created");
        return true;
      } else {
        throw new SearchException("Error creating index: ack is false");
      }
    } catch (UnknownHostException e) {
      throw new SearchException("Error creating index", e);
    }
  }
  
  public void createMappings(String index, String mappingsFile) throws SearchException {
    try (Client client = getClient()) {
      InputStream is = new FileInputStream(new File(mappingsFile));
      XContentParser parser = XContentFactory.xContent(XContentType.JSON).createParser(is);
      boolean ack = client.admin().indices().preparePutMapping(index).setType("doc")
        .setSource(parser.mapOrdered())
        .execute().actionGet().isAcknowledged();
      LOG.info("Mappings were " + (ack ? "" : "not ") + "created");
    } catch (IOException e) {
      throw new SearchException("Error creating mappings", e);
    }
  }
  
  public void createAlias(String index, String alias) throws SearchException {
    try (Client client = getClient()) {
      IndicesAliasesResponse resp = client.admin().indices().prepareAliases().addAlias(index, alias).get();
      LOG.info("Alias creation ack: " + resp.isAcknowledged());
    } catch (UnknownHostException e) {
      throw new SearchException("Error creating alias", e);
    }
  }
  
  public boolean isIndexExists(String index) throws SearchException {
    try (Client client = getClient()) {
      return isIndexExists(client, index);
    } catch (UnknownHostException e) {
      throw new SearchException("Error getting index", e);
    }
  }
  
  private boolean isIndexExists(Client client, String index) throws SearchException {
    return getMetaData(client).index(index) != null;
  }
  
  
  
  private MetaData getMetaData(Client client) throws SearchException {
    return client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData();
  }
  
  private Client getClient() throws UnknownHostException {
    String host = System.getenv("NOTES_ES_HOST");
    int port = Integer.parseInt(System.getenv("NOTES_ES_TRANSPORT"));
    TransportClient client = TransportClient.builder().build()
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
    return client;
    return null;
  }
  */
}
