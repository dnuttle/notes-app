package net.nuttle.notes.ui;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MarkdownRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(MarkdownRenderer.class);
  private Parser parser;
  private HtmlRenderer renderer;
  
  public MarkdownRenderer() {
    parser = Parser.builder().build();
    renderer = HtmlRenderer.builder().build();
  }
  public String renderToHTML(String md) {
    Node note = parser.parse(md);
    String body = renderer.render(note);
    return body;
    /*
    StringBuffer sb = new StringBuffer();
    sb.append("<DOCTYPE html><html><head>")
      .append("<link rel='stylesheet' href='/bootstrap.css'>")
      .append("</head><body>")
      .append("<div class='container'>")
      .append("<nav class='navbar navbar-dark bg-primary'><span class='navbar-brand'>Notes</span></nav>")
      .append(body)
      .append("</div></body></html>");
    LOG.info(sb.toString());
    return sb.toString();
    */
  }
}
