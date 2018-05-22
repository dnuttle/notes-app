package net.nuttle.notes.ui;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownRenderer {

  private Parser parser;
  private HtmlRenderer renderer;
  
  public MarkdownRenderer() {
    parser = Parser.builder().build();
    renderer = HtmlRenderer.builder().build();
  }
  public String renderToHTML(String md) {
    Node note = parser.parse(md);
    return renderer.render(note);
  }
}
