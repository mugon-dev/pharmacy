package com.fastcampus.pharmacy.direction.controller;

import com.fastcampus.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

  private final DirectionService directionService;

  @GetMapping("/dir/{encodedId}")
  public String searchDirection(@PathVariable("encodedId") String encodeId) {
    String result = directionService.findDirectUrlById(encodeId);
    log.info("[DirectionController searchDirection] direction url: {}", result);
    return "redirect:" + result;
  }

}
