package org.sentiment.analyzer.endpoints.article;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Rule;
import org.junit.Test;
import org.sentiment.analyzer.framework.AnalyzerIntegrationBaseTest;
import org.sentiment.analyzer.framework.TestHttpService;
import org.sentiment.analyzer.services.articles.AnalyzedArticle;
import org.sentiment.analyzer.services.articles.AnalyzedArticleRequest;
import org.sentiment.analyzer.services.articles.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;

public class ArticleControllerDocsTest extends AnalyzerIntegrationBaseTest {

  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation(AnalyzerIntegrationBaseTest.ASCIIDOCTOR_SNIPPETS_DIR);

  @Autowired
  private TestHttpService http;
  @Autowired
  private ArticleService articleService;

  @Test
  public void articleExample() throws Exception {
    String title = "Raiders Leaving Oakland";
    String body = "Schaaf said that Davis had refused to meet with her over the past year.";
    AnalyzedArticleRequest request = new AnalyzedArticleRequest(title, body);
    http.mvcDocs(restDocumentation)
        .perform(post("/api/article/analyze").contentType(MediaType.APPLICATION_JSON)
            .content(http.serialize(request)))
        .andExpect(status().isOk())
        .andDo(http.document(
            responseFields(
                fieldWithPath("id").description("The id for the article resource"),
                fieldWithPath("title").description("The title of the article"),
                fieldWithPath("titleScore").description("The score for the article title"),
                fieldWithPath("bodyScore").description("The score for the article body"))));
  }

  @Test
  public void testGet() throws Exception {
    String title = "Raiders Leaving Oakland";
    String body = "Schaaf said that Davis had refused to meet with her over the past year.";
    AnalyzedArticleRequest request = new AnalyzedArticleRequest(title, body);
    AnalyzedArticle article = articleService.analyze(request);
    http.mvcDocs(restDocumentation)
        .perform(get("/api/article/" + article.getId()))
        .andExpect(status().isOk())
        .andDo(http.document(
            responseFields(
                fieldWithPath("id").description("The id for the article resource"),
                fieldWithPath("title").description("The title of the article"),
                fieldWithPath("titleScore").description("The score for the article title"),
                fieldWithPath("bodyScore").description("The score for the article body"))));
  }

}
