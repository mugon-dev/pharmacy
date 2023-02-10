package com.fastcampus.pharmacy.direction.service;

import com.fastcampus.pharmacy.api.dto.DocumentDto;
import com.fastcampus.pharmacy.direction.entity.Direction;
import com.fastcampus.pharmacy.direction.repository.DirectionRepository;
import com.fastcampus.pharmacy.pharmacy.service.PharmacySearchService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

  // 약국 최대 검색 갯수
  private static final int MAX_SEARCH_COUNT = 3;
  // 반경 10 km
  private static final double RADIUS_KM = 10.0;

  private final PharmacySearchService pharmacySearchService;
  private final DirectionRepository directionRepository;

  @Transactional
  public List<Direction> saveAll(List<Direction> directionList) {
    if (CollectionUtils.isEmpty(directionList))
      return Collections.emptyList();
    return directionRepository.saveAll(directionList);
  }

  public List<Direction> buildDirectionList(DocumentDto documentDto) {
    if (Objects.isNull(documentDto)) {
      return Collections.emptyList();
    }
    // 약국 데이터 조회
    // 거리계산 알고리즘을 이용하여, 고객과 약국 사이의 거리를 계산하고 sort
    return pharmacySearchService.searchPharmacyDtoList().stream().map(pharmacyDto ->
                                    Direction.builder()
                                             .inputAddress(documentDto.getAddressName())
                                             .inputLatitude(documentDto.getLatitude())
                                             .inputLongitude(documentDto.getLongitude())
                                             .targetPharmacyName(pharmacyDto.getPharmacyName())
                                             .targetAddress(pharmacyDto.getPharmacyAddress())
                                             .targetLatitude(pharmacyDto.getLatitude())
                                             .targetLongitude(pharmacyDto.getLongitude())
                                             .distance(calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                                 pharmacyDto.getLatitude(), pharmacyDto.getLongitude()))
                                             .build())
                                .filter(
                                    direction -> direction.getDistance() <= RADIUS_KM) // 10KM 이하
                                .sorted(Comparator.comparing(Direction::getDistance)) // 거리순
                                .limit(MAX_SEARCH_COUNT) // 상위 3개
                                .collect(Collectors.toList());
  }

  // Haversine formula
  private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    lat1 = Math.toRadians(lat1);
    lon1 = Math.toRadians(lon1);
    lat2 = Math.toRadians(lat2);
    lon2 = Math.toRadians(lon2);

    double earthRadius = 6371; //Kilometers
    return earthRadius * Math.acos(
        Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
  }
}
