package com.brunoandradesa.api.controller;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import io.swagger.v3.oas.annotations.Hidden;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/changelog")
public class ChangelogController {

  private final Parser parser = Parser.builder().build();
  private final HtmlRenderer renderer = HtmlRenderer.builder().build();

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> getChangelog() {
    try (InputStream is =
        getClass().getClassLoader().getResourceAsStream("changelog/CHANGELOG.md")) {

      if (is == null) {
        return ResponseEntity.notFound().build();
      }

      String markdown = new String(is.readAllBytes(), StandardCharsets.UTF_8);

      Node document = parser.parse(markdown);
      String htmlBody = renderer.render(document);

      String html =
          """
          <!DOCTYPE html>
          <html lang="pt-BR">
          <head>
              <meta charset="UTF-8">
              <title>Changelog</title>
              <style>
                  :root {
                      --bg: #0d1117;
                      --card: #161b22;
                      --text: #c9d1d9;
                      --muted: #8b949e;
                      --border: #30363d;
                      --accent: #58a6ff;
                      --code-bg: #161b22;
                  }

                  * {
                      box-sizing: border-box;
                  }

                  body {
                      margin: 0;
                      padding: 40px 20px;
                      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, sans-serif;
                      background: var(--bg);
                      color: var(--text);
                      line-height: 1.7;
                  }

                  .container {
                      max-width: 900px;
                      margin: auto;
                      background: var(--card);
                      padding: 40px;
                      border-radius: 12px;
                      border: 1px solid var(--border);
                      box-shadow: 0 10px 30px rgba(0,0,0,0.4);
                  }

                  h1 {
                      font-size: 2.2rem;
                      margin-bottom: 20px;
                  }

                  h2 {
                      margin-top: 40px;
                      padding-bottom: 8px;
                      border-bottom: 1px solid var(--border);
                      color: var(--accent);
                  }

                  h3 {
                      margin-top: 25px;
                      color: var(--text);
                  }

                  p {
                      color: var(--text);
                  }

                  ul {
                      padding-left: 20px;
                  }

                  li {
                      margin: 6px 0;
                  }

                  strong {
                      color: white;
                  }

                  code {
                      background: var(--code-bg);
                      padding: 3px 6px;
                      border-radius: 6px;
                      font-size: 0.9em;
                      border: 1px solid var(--border);
                  }

                  pre {
                      background: var(--code-bg);
                      padding: 16px;
                      border-radius: 10px;
                      overflow-x: auto;
                      border: 1px solid var(--border);
                  }

                  pre code {
                      background: none;
                      border: none;
                      padding: 0;
                  }

                  hr {
                      border: none;
                      border-top: 1px solid var(--border);
                      margin: 30px 0;
                  }

                  a {
                      color: var(--accent);
                      text-decoration: none;
                  }

                  a:hover {
                      text-decoration: underline;
                  }

                  h2::before {
                      content: "📦 ";
                  }

                  li::marker {
                      color: var(--accent);
                  }

                  ::-webkit-scrollbar {
                      height: 8px;
                  }

                  ::-webkit-scrollbar-thumb {
                      background: #30363d;
                      border-radius: 10px;
                  }

                  ::-webkit-scrollbar-thumb:hover {
                      background: #484f58;
                  }
              </style>
          </head>
          <body>
              <div class="container">
                  %s
              </div>
          </body>
          </html>
          """
              .formatted(htmlBody);

      return ResponseEntity.ok().body(html);

    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Erro ao carregar changelog");
    }
  }
}
