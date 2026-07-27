package com.team114.starbucks.domain.crawling;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawling")
public class CrawledProductController {

    private final CrawledProductService crawledProductService;

    @PostMapping("/upload")
    public ResponseEntity<String> saveCrawledProducts(
            @RequestBody List<CrawledProductDto> crawledProductDto
    ) {
        crawledProductService.saveCrawledProductsInBatch(crawledProductDto);
        return ResponseEntity.ok("총 " + crawledProductDto.size() + "건 저장 완료");
    }
}