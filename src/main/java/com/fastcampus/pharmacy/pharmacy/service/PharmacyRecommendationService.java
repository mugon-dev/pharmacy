package com.fastcampus.pharmacy.pharmacy.service;

import com.fastcampus.pharmacy.api.dto.DocumentDto;
import com.fastcampus.pharmacy.api.dto.KakaoApiResponseDto;
import com.fastcampus.pharmacy.api.service.KakaoAddressSearchService;
import com.fastcampus.pharmacy.direction.dto.OutputDto;
import com.fastcampus.pharmacy.direction.entity.Direction;
import com.fastcampus.pharmacy.direction.service.Base62Service;
import com.fastcampus.pharmacy.direction.service.DirectionService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

  private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
  private final KakaoAddressSearchService kakaoAddressSearchService;
  private final DirectionService directionService;
  private final Base62Service base62Service;
  @Value("${pharmacy.recommendation.base.url}")
  private String baseUrl;

  public List<OutputDto> recommendPharmacyList(String address) {
    KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(
        address);
    if (Objects.isNull(kakaoApiResponseDto) ||
        CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
      log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}",
          address);
      return Collections.emptyList();
    }
    DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);
    // 가까운 약국 리스트
    List<Direction> directionList = directionService.buildDirectionList(documentDto);
    // kakao api를 통해 검색
//    List<Direction> directions = directionService.buildDirectionListByCategoryApi(documentDto);
    return directionService.saveAll(directionList).stream().map(this::convertToOutputDto).collect(
        Collectors.toList());
  }

  private OutputDto convertToOutputDto(Direction direction) {
    return OutputDto.builder()
                    .pharmacyName(direction.getTargetPharmacyName())
                    .pharmacyAddress(direction.getTargetAddress())
                    .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                    .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + ","
                        + direction.getTargetLongitude())
                    .distance(String.format("%.2f km", direction.getDistance()))
                    .build();
  }

}
