package com.fastcampus.pharmacy.direction.controller;

import com.fastcampus.pharmacy.direction.entity.Direction;
import com.fastcampus.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

  private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";
  private final DirectionService directionService;

  @GetMapping("/dir/{encodedId}")
  public String searchDirection(@PathVariable("encodedId") String encodeId) {
    Direction resultDirection = directionService.findById(encodeId);
    String params = String.join(",", resultDirection.getTargetPharmacyName(),
        String.valueOf(resultDirection.getTargetLatitude()),
        String.valueOf(resultDirection.getTargetLongitude()));

    // 한글 처리
    String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params).toUriString();
    log.info("direction params: {}, uri: {}", params, result);
    return "redirect:" + result;
  }

}
