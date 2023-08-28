package com.example.pdf.common.pdf;

import com.lowagie.text.Image;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * resources 경로에 있는 이미지 파일을 PDF로 변환하기 위한 factory
 */
public class ImageReplacedElementFactory implements ReplacedElementFactory {

  private final ReplacedElementFactory replacedElementFactory;

  public ImageReplacedElementFactory(ReplacedElementFactory replacedElementFactory) {
    this.replacedElementFactory = replacedElementFactory;
  }

  @Override
  public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox,
      UserAgentCallback userAgentCallback, int cssWidth, int cssHeight) {
    Element element = blockBox.getElement();
    if (element == null) {
      return null;
    }

    String nodeName = element.getNodeName();
    String srcPath = element.getAttribute("src");

    if ("img".equalsIgnoreCase(nodeName) && srcPath.startsWith("/image")) {
      try {
        FSImage fsImage = new ITextFSImage(
                Image.getInstance(
                        Files.readAllBytes(
                                FileSystems.getDefault().provider().getPath(new ClassPathResource("static" + element.getAttribute("src")).getURI())
                        )
                )
        );

        if ((cssWidth != -1) || (cssHeight != -1)) {
          fsImage.scale(cssWidth, cssHeight);
        }

        return new ITextImageElement(fsImage);
      } catch (Exception e) {
        return null;
      }
    } else {
      return replacedElementFactory.createReplacedElement(
          layoutContext,
          blockBox,
          userAgentCallback,
          cssWidth,
          cssHeight
      );
    }
  }

  @Override
  public void reset() {
    replacedElementFactory.reset();
  }

  @Override
  public void remove(Element element) {
    replacedElementFactory.remove(element);
  }

  @Override
  public void setFormSubmissionListener(FormSubmissionListener formSubmissionListener) {
    replacedElementFactory.setFormSubmissionListener(formSubmissionListener);
  }
}
