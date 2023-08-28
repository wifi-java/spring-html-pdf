package com.example.pdf.controller;

import com.example.pdf.service.SampleService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SampleController {
  private final SampleService sampleService;

  @GetMapping("/")
  @ResponseBody
  public void downloadPDF(HttpServletResponse response) {
    try {
      Date date = new Date(Calendar.getInstance(Locale.KOREA).getTimeInMillis());
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
      String name = formatter.format(date) + ".pdf";
      response.addHeader("Content-disposition", "attachment; filename=" + "abcd_" + name);
      response.setContentType("application/pdf");

      Map<String, Object> map = new HashMap<>();

      OutputStream outputStream = response.getOutputStream();
      sampleService.generatePdfFromHtml("sample", map, outputStream);
    } catch (Exception e) {
      log.error("downloadPDF", e);
      response.setHeader("Content-disposition", "");
      response.setContentType("application/json");
    }
  }
}
