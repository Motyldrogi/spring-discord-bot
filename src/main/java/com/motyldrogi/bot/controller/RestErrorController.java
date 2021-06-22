package com.motyldrogi.bot.controller;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.motyldrogi.bot.util.ErrorJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class RestErrorController implements ErrorController {

  private final ErrorAttributes errorAttributes;

  @Autowired
  public RestErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
  }

  @GetMapping("/error")
  public ErrorJson error(WebRequest request, HttpServletResponse response) {
    return new ErrorJson(response.getStatus(), getErrorAttributes(request));
  }

  private Map<String, Object> getErrorAttributes(WebRequest request) {
    return errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
  }

  public String getErrorPath() {
    return "/error";
  }

}