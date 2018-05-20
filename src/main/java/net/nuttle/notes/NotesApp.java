package net.nuttle.notes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"net.nuttle.notes"})
public class NotesApp {

  private static final Logger LOG = LoggerFactory.getLogger(NotesApp.class);
  public static void main(String[] args) {
    LOG.info("Starting notes app");
    SpringApplication.run(NotesApp.class, args);
  }
}
