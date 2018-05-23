package net.nuttle.notes.io;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.nuttle.notes.Note;

@Service
public class FileUtil {

  public static final String FILE_ARCHIVE_PATH_ENV = "NOTES_FILE_ARCHIVE_PATH";
  
  private String fileArchivePath;
  
  public FileUtil() {
    fileArchivePath = System.getenv(FILE_ARCHIVE_PATH_ENV) + "/";
  }
  
  public void archive(Note note) throws IOException {
    String fileName = note.getNoteid() + ".json";
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(new File(fileArchivePath, fileName), note);
  }
}
