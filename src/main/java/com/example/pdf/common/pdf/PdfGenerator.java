package com.example.pdf.common.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
public class PdfGenerator {

  public String parseHtmlFileToString(String templateCode, Map<String, Object> map) {
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/");
    resolver.setSuffix(".html");
    resolver.setCharacterEncoding("UTF-8");
    resolver.setForceTemplateMode(true);
    resolver.setTemplateMode(TemplateMode.HTML);

    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setTemplateResolver(resolver);

    Context context = new Context();
    context.setVariables(map);

    return engine.process(templateCode, context);
  }

  /**
   * outputStream에 pdf를 쓴다.
   */
  public void generateFromHtml(OutputStream outputStream, String html)
      throws DocumentException, IOException {
    ITextRenderer renderer = getRenderer(html);
    renderer.createPDF(outputStream);
  }

  /**
   * 해당 path에 pdf를 파일로 쓴다.
   */
  public void generateFromHtml(String path, String html)
      throws IOException, DocumentException {
    FileOutputStream fos = new FileOutputStream(path);
    ITextRenderer renderer = getRenderer(html);
    renderer.createPDF(fos);
  }

  private ITextRenderer getRenderer(String html) throws IOException, DocumentException {
    ITextRenderer renderer = new ITextRenderer();
    ImageReplacedElementFactory imageReplacedElementFactory = new ImageReplacedElementFactory(
        renderer.getSharedContext()
                .getReplacedElementFactory());

    renderer.getSharedContext()
            .setReplacedElementFactory(imageReplacedElementFactory);

    renderer.setDocumentFromString(html);

    // 한글이 포함된 폰트 추가
    renderer.getFontResolver()
            .addFont(new ClassPathResource("/static/font/NanumBarunGothic.ttf").getURL()
                                                                               .toString(),
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED);

    renderer.layout();

    return renderer;
  }
}
