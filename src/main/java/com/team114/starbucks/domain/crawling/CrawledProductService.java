package com.team114.starbucks.domain.crawling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team114.starbucks.domain.color.entity.Color;
import com.team114.starbucks.domain.color.infrastructure.ColorRepository;
import com.team114.starbucks.domain.event.infrastructure.EventRepository;
import com.team114.starbucks.domain.maincategory.entity.MainCategory;
import com.team114.starbucks.domain.maincategory.infrastructure.MainCategoryRepository;
import com.team114.starbucks.domain.option.entity.Option;
import com.team114.starbucks.domain.option.infrastructure.OptionRepository;
import com.team114.starbucks.domain.product.entity.Product;
import com.team114.starbucks.domain.product.entity.ProductDescription;
import com.team114.starbucks.domain.product.entity.ProductThumbnail;
import com.team114.starbucks.domain.product.enums.Brand;
import com.team114.starbucks.domain.product.enums.ProductStatus;
import com.team114.starbucks.domain.product.infrastructure.ProductDescriptionRepository;
import com.team114.starbucks.domain.product.infrastructure.ProductRepository;
import com.team114.starbucks.domain.product.infrastructure.ProductThumbnailRepository;
import com.team114.starbucks.domain.productcategory.entity.ProductCategory;
import com.team114.starbucks.domain.productcategory.infrastructure.ProductCategoryRepository;
import com.team114.starbucks.domain.size.entity.Size;
import com.team114.starbucks.domain.size.infrastructure.SizeRepository;
import com.team114.starbucks.domain.subcategory.entity.SubCategory;
import com.team114.starbucks.domain.subcategory.infrastructure.SubCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawledProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final EventRepository eventRepository;
    private final OptionRepository optionRepository;
    private final ProductThumbnailRepository productThumbnailRepository;
    private final ProductDescriptionRepository productDescriptionRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    private final Random random = new Random();

    @Transactional
    public void saveCrawledProductsInBatch(List<CrawledProductDto> crawledProductDto) {
        List<List<CrawledProductDto>> batches = partition(crawledProductDto, 500);

        for (List<CrawledProductDto> batch : batches) {
            for (CrawledProductDto dto : batch) {
                try {
                    saveCrawledProduct(dto);
                } catch (Exception e) {
                    log.warn("[저장 실패] 제품명: {}", dto.getProductName(), e);
                }
            }
        }
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            result.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return result;
    }

    @Transactional
    public void saveCrawledProduct(CrawledProductDto dto) {

        String mainCategoryName = dto.getCategory().get(0); // 대분류
        String subCategoryName = dto.getCategory().get(1);  // 소분류

        MainCategory mainCategory = mainCategoryRepository.findByMainCategoryName(mainCategoryName)
                .orElseGet(() -> mainCategoryRepository.save(
                        MainCategory.builder()
                                .mainCategoryUuid(UUID.randomUUID().toString())
                                .mainCategoryName(mainCategoryName)
                                .build()));

        SubCategory subCategory = subCategoryRepository.findBySubCategoryName(subCategoryName)
                .orElseGet(() -> subCategoryRepository.save(
                        SubCategory.builder()
                                .subCategoryUuid(UUID.randomUUID().toString())
                                .subCategoryName(subCategoryName)
                                .mainCategoryUuid(mainCategory.getMainCategoryUuid())
                                .build()));

        Optional<Product> existingProduct = productRepository.findByProductUuid(dto.getProductName());

        String productUuid;
        Product product;

        if(existingProduct.isPresent()) {
            product = existingProduct.get();
            productUuid = product.getProductUuid();
        } else {
            productUuid = UUID.randomUUID().toString();
            product = Product.builder()
                .productUuid(productUuid)
                .productName(dto.getProductName())
                .productPrice(dto.getProductPrice())
                .brand(Brand.BRAND_STARBUCKS.getBrand())
                .productStatus(ProductStatus.For_Sale)
                .shippingFee(2500)
                .build();
            product = productRepository.save(product);
        }

        List<String> eventUuids = eventRepository.findAllEventUuids();
        if(eventUuids.isEmpty()) {
            throw new IllegalStateException("등록된 이벤트가 없습니다.");
        }

        String randomEventUuid = eventUuids.get(random.nextInt(eventUuids.size()));

        boolean exists = productCategoryRepository.existsByProductUuidAndMainCategoryUuidAndSubCategoryUuid(
                productUuid, mainCategory.getMainCategoryUuid(), subCategory.getSubCategoryUuid());

        if (!exists) {
            ProductCategory pc = ProductCategory.builder()
                    .productUuid(productUuid)
                    .mainCategoryUuid(mainCategory.getMainCategoryUuid())
                    .subCategoryUuid(subCategory.getSubCategoryUuid())
                    .eventUuid(randomEventUuid)
                    .build();
            productCategoryRepository.save(pc);
        }

        Color color = colorRepository.findByColorId(5L)
                .orElseThrow(() -> new IllegalStateException("기본 색상(ID=5)이 존재하지 않습니다."));

        Size size = sizeRepository.findBySizeId(5L)
                .orElseThrow(() -> new IllegalStateException("기본 사이즈(ID=5)이 존재하지 않습니다."));

        Option option = Option.builder()
                .productUuid(productUuid)
                .color(color)
                .size(size)
                .optionPrice(dto.getOptionPrice())
                .stock(10)
                .discountRate(0)
                .build();
        optionRepository.save(option);


        for (CrawledProductDto.ThumbnailDto thumb : dto.getThumbnails()) {
            int thumbnailIndex = parseIndex(thumb.getThumbnailIndex());
            ProductThumbnail image = ProductThumbnail.builder()
                    .product(product)
                    .productUuid(product.getProductUuid())
                    .thumbnailIndex(thumbnailIndex)
                    .thumbnailUrl(thumb.getThumbnailUrl())
                    .isThumbnail(thumbnailIndex == 0)
                    .uploadedBy("CRAWLER")
                    .build();
            productThumbnailRepository.save(image);
        }

        ProductDescription description = ProductDescription.builder()
                .productUuid(productUuid)
                .productDescription(dto.getProductDescription())
                .build();
        productDescriptionRepository.save(description);
    }

    private int parseIndex(String altText) {
        // 예: "상품 이미지3" → 3
        try {
            return Integer.parseInt(altText.replaceAll("[^0-9]", "")) - 1;
        } catch (Exception e) {
            return 0;
        }
    }
}