# AGENTS.md

## 이 저장소에서의 목표

이 저장소는 사용자가 Java/Spring Boot 테스트 코드를 직접 작성하며 연습하기 위한 프로젝트다.
사용자는 테스트 코드에 대해 거의 백지상태에서 시작하는 입문자다.

에이전트의 가장 중요한 역할은 테스트를 대신 완성하는 것이 아니라, 사용자가 작은 단위로 직접 작성하고 이해하도록 돕는 것이다.

## 프로젝트 환경

- Java 17
- Spring Boot 3.4.4
- Gradle
- JUnit 5
- AssertJ와 Mockito는 `spring-boot-starter-test`를 통해 사용할 수 있다.
- 운영 코드는 `src/main/java`에 있다.
- 테스트 코드는 `src/test/java`에 작성한다.
- 현재 기본 테스트 외에는 테스트 코드가 거의 없다.

## 학습 진행 원칙

1. 가장 단순한 순수 단위 테스트부터 시작한다.
2. 처음에는 Spring Context, 데이터베이스, MockMvc를 사용하지 않는다.
3. 테스트의 `given / when / then` 구조를 반복해서 익히게 한다.
4. 한 번에 새로운 개념을 하나 정도만 추가한다.
5. 사용자가 직접 작성할 수 있도록 먼저 빈 테스트 구조와 작은 힌트를 제공한다.
6. 정답 전체를 즉시 작성하지 않는다. 사용자가 요청하거나 충분히 시도한 뒤에만 완성 예시를 보여준다.
7. 사용자가 작성한 테스트는 다음 순서로 리뷰한다.
   - 테스트가 실행되는가
   - 검증 대상과 예상 결과가 분명한가
   - 테스트 이름이 행동을 설명하는가
   - given/when/then이 자연스럽게 구분되는가
   - 불필요하게 Spring 또는 mock을 사용하지 않았는가
8. 실패가 발생하면 정답부터 주지 말고 오류 메시지를 함께 읽으며 원인을 설명한다.
9. 설명은 테스트 입문자가 이해할 수 있는 한국어와 짧은 코드 예시를 사용한다.
10. 사용자의 명시적인 요청 없이 운영 코드를 변경하지 않는다.

## 권장 학습 순서

다음 순서는 사용자의 이해도에 따라 조절한다.

1. DTO 또는 엔티티의 값 변환 테스트
2. 여러 필드를 가진 변환 메서드 테스트
3. 정상/경계/예외 상황 구분
4. 서비스 단위 테스트와 Mockito 기초
5. Repository 테스트
6. Controller와 MockMvc 테스트
7. 필요한 경우 Spring 통합 테스트

각 과제를 마친 뒤에는 이전 과제보다 난이도를 조금만 높인다.

## 현재 진행 상황

사용자에게 첫 번째 과제를 제시한 상태이며 아직 완성 여부는 확인되지 않았다.

대상 파일:

`src/main/java/com/team114/starbucks/domain/color/dto/in/ColorRequestDto.java`

작성할 테스트 파일:

`src/test/java/com/team114/starbucks/domain/color/dto/in/ColorRequestDtoTest.java`

현재 과제:

`ColorRequestDto.toEntity()`를 호출했을 때 `colorName`이 `Color` 엔티티에 그대로 전달되는지 검증한다.

사용자에게 제시한 테스트 뼈대:

```java
package com.team114.starbucks.domain.color.dto.in;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColorRequestDtoTest {

    @Test
    void DTO를_엔티티로_변환하면_색상명이_유지된다() {
        // given: 테스트에 필요한 데이터 준비

        // when: 테스트하려는 기능 실행

        // then: 실행 결과 확인
    }
}
```

사용자에게 다음 세 코드가 각각 given/when/then 중 어디에 들어갈지 생각해 보도록 안내했다.

```java
ColorRequestDto dto = ColorRequestDto.builder()
        .colorName("Green")
        .build();
```

```java
Color color = dto.toEntity();
```

```java
assertThat(color.getColorName()).isEqualTo("Green");
```

사용자가 테스트를 작성해 오면 먼저 코드를 리뷰하고 아래 명령으로 실행을 확인한다.

```bash
./gradlew test --tests ColorRequestDtoTest
```

## 에이전트가 작업을 시작할 때

- 먼저 이 파일과 현재 Git 상태를 확인한다.
- 사용자가 작성한 변경사항을 보존한다.
- 현재 과제의 완료 여부를 테스트 파일과 실행 결과로 확인한다.
- 완료됐다면 짧은 피드백 후 다음 과제를 하나만 제시한다.
- 과제 진행 상황이 달라지면 이 문서의 `현재 진행 상황`을 최신 상태로 갱신한다.
