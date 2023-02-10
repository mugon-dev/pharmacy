package com.fastcampus.pharmacy.api.service;

import com.fastcampus.pharmacy.api.dto.KakaoApiResponseDto;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAddressSearchService {

  private final RestTemplate restTemplate;
  private final KakaoUriBuilderService kakaoUriBuilderService;
  @Value("${kakao.rest.api.key}")
  private String kakaoRestApiKey;

  @Retryable(
      // 어떤 exception에서 재처리
      value = {RuntimeException.class},
      maxAttempts = 2,
      backoff = @Backoff(delay = 2)
  )
  public KakaoApiResponseDto requestAddressSearch(String address) {

    if (ObjectUtils.isEmpty(address)) {
      return null;
    }
    URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
    HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

    // kakao api 호출
    return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class)
                       .getBody();
  }

  // 원래 메서드의 리턴타입 동일
  @Recover
  public KakaoApiResponseDto recover(RuntimeException e, String address) {
    log.error("All the retries failed. address: {}, error: {}", address, e.getMessage());
    return null;
  }

}
