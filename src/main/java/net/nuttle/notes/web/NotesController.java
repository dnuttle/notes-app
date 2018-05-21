package net.nuttle.notes.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.nuttle.notes.es.ESUtil;


@Controller
public class NotesController {
  
  private static final Logger LOG = LoggerFactory.getLogger(NotesController.class);
  
  @Autowired
  ESUtil esUtil;

  @RequestMapping(value="/test")
  @ResponseBody
  public String test() {
    return "<html><body>Notes App</body></html>";
  }
  
  @RequestMapping(value="/create")
  @ResponseBody
  public String create() {
    try {
      boolean created = esUtil.createIndex("notes");
      if (created) {
        esUtil.createMappings("notes", System.getProperty("user.dir") + "/src/main/resources/mappings_6_1_1.json");
        return "Created notes index";
      } else {
        return "Notes index already exists";
      }
    } catch (Exception e) {
      LOG.error("Error creating index", e);
      return "ERROR CREATING INDEX";
    }
  }
  
  @RequestMapping(value="/index") 
  @ResponseBody
  public String index() {
    try {
      esUtil.indexTestDocs("notes");
      return "Indexed test docs";
    } catch (Exception e) {
      LOG.error("Error creating index", e);
      return "Error indexing test docs";
    }
  }
  
  @RequestMapping(value="/search")
  @ResponseBody
  public String search() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
      return mapper.writeValueAsString(esUtil.search("notes", "*:*").getHits());
      //return esUtil.search("notes", "*:*");
    } catch (Exception e) {
      LOG.error("Error searching", e);
      return null;
    }
  }
}
