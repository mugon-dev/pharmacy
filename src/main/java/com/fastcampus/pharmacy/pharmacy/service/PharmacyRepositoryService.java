package com.fastcampus.pharmacy.pharmacy.service;

import com.fastcampus.pharmacy.pharmacy.entity.Pharmacy;
import com.fastcampus.pharmacy.pharmacy.repository.PharmacyRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRepositoryService {

  private final PharmacyRepository pharmacyRepository;

  @Transactional
  public void updateAddress(Long id, String address) {
    Pharmacy entity = pharmacyRepository.findById(id).orElse(null);
    if (Objects.isNull(entity)) {
      log.error("[PharmacyRepositoryService updateAddress] not found id: {}", id);
      return;
    }
    entity.changePharmacyAddress(address);
  }
}
