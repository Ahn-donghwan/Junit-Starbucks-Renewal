package com.team114.starbucks.domain.product.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team114.starbucks.domain.product.entity.ProductDescription;

public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {

    Optional<ProductDescription> findByProductUuid(String productUuid);

    void deleteByProductUuid(String productUuid);

}