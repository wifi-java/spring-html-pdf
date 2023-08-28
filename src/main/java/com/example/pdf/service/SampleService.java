package com.example.pdf.service;

import com.example.pdf.common.pdf.PdfGenerator;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SampleService {

  /**
   * outputStream에 pdf를 쓴다.
   */
  public void generatePdfFromHtml(String templateName, Map<String, Object> map, OutputStream outputStream)
          throws DocumentException, IOException {
    PdfGenerator pdfGenerator = new PdfGenerator();
    String html = pdfGenerator.parseHtmlFileToString(templateName, map);
    pdfGenerator.generateFromHtml(outputStream, html);
  }

  /**
   * 해당 path에 pdf를 파일로 쓴다.
   */
  public void generatePdfFromHtml(String templateName, Map<String, Object> map,
                                  String path)
          throws DocumentException, IOException {
    PdfGenerator pdfGenerator = new PdfGenerator();
    String html = pdfGenerator.parseHtmlFileToString(templateName, map);
    pdfGenerator.generateFromHtml(path, html);
  }
}
