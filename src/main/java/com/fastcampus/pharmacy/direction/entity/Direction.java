package com.fastcampus.pharmacy.direction.entity;

import com.fastcampus.pharmacy.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "direction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Direction extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String inputAddress;
  private double inputLatitude;
  private double inputLongitude;

  private String targetPharmacyName;
  private String targetAddress;
  private double targetLatitude;
  private double targetLongitude;

  // 고객 주소와 약국 주소 사이의 거리
  private double distance;
}
