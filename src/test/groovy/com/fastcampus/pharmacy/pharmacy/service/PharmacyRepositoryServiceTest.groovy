package com.fastcampus.pharmacy.pharmacy.service

import com.fastcampus.pharmacy.AbstractIntegrationContainerBaseTest
import com.fastcampus.pharmacy.pharmacy.entity.Pharmacy
import com.fastcampus.pharmacy.pharmacy.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired

class PharmacyRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {
    @Autowired
    private PharmacyRepositoryService pharmacyRepositoryService
    @Autowired
    private PharmacyRepository pharmacyRepository

    def setup() {
        pharmacyRepository.deleteAll()
    }

    def "PharmacyRepository update - dirty checking success"() {
        given:
        String inputAddress = "서울 특별시 성복구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"
        double latitude = 36.11
        double longitude = 128.11
        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()
        when:
        def entity = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddress(entity.getId(), modifiedAddress)
        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getPharmacyAddress() == modifiedAddress

    }
}
