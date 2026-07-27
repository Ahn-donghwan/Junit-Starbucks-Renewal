package com.team114.starbucks.domain.delivery.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team114.starbucks.domain.delivery.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByMemberUuid(String memberUuid);

    Optional<Delivery> findByDeliveryUuid(String deliveryUuid);

    Optional<Delivery> findByMemberUuidAndDefaultAddressTrue(String memberUuid);

    List<Delivery> findByMemberUuid(String memberUuid);

    Optional<Delivery> findByMemberUuidAndIsSelectedTrue(String memberUuid);

    Optional<Delivery> findByMemberUuidAndDeliveryUuid(String memberUuid, String deliveryUuid);

}