package com.team114.starbucks.domain.maincategory.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.team114.starbucks.domain.maincategory.entity.MainCategory;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {

    Optional<MainCategory> findByMainCategoryUuid(String mainCategoryUuid);

    Optional<MainCategory> deleteByMainCategoryUuid(String mainCategoryUuid);

    Optional<MainCategory> findByMainCategoryName(String mainCategoryName);

}
