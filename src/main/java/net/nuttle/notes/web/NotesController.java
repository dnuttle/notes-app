package net.nuttle.notes.web;

import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.nuttle.notes.Note;
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
  
  @RequestMapping(value="/indexTestNotes") 
  @ResponseBody
  public String indexTestNotes() {
    try {
      esUtil.indexTestNotes("notes");
      return "Indexed test notes";
    } catch (Exception e) {
      LOG.error("Error indexing test notes", e);
      return "Error indexing test notes";
    }
  }
  
  @RequestMapping(value="/index", method=RequestMethod.PUT)
  @ResponseBody
  public String index(@RequestBody Note note) {
    try {
      esUtil.index("notes", note);
      return "Indexing note";
    } catch (Exception e) {
      LOG.error("Error indexing note", e);
      return "Error indexing note";
    }
  }
  
  @RequestMapping(value="/update/{id}", method=RequestMethod.POST)
  @ResponseBody
  public String update(@PathVariable("id") String id, @RequestBody String note) {
    try {
      LOG.info(note.toString());
      esUtil.update("notes", id, note);
      return "Updating note";
    } catch (Exception e) {
      LOG.error("Error updating note", e);
      return "Error updating note";
    }
  }

  @RequestMapping(value="/search")
  @ResponseBody
  public SearchResponse search() {
    try {
      return esUtil.search("notes", "*:*");
    } catch (Exception e) {
      LOG.error("Error searching", e);
      return null;
    }
  }
  
  @RequestMapping(value="/fetch/{id}", method=RequestMethod.GET)
  @ResponseBody
  public String fetch(@PathVariable("id") String id) {
    try {
      return esUtil.fetch("notes", id);
    } catch (Exception e) {
      LOG.error("Error fetching", e);
      return "Error fetching";
    }
  }
  
  //TODO index endpoint should persist note to JSON file
}
