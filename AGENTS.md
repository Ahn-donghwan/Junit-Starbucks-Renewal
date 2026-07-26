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
11. 사용자는 은행권 환경을 준비하고 있으며 AssertJ 사용 여부가 불분명하므로, 당분간 검증문은 JUnit Jupiter `Assertions`로 연습한다.

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

첫 번째 과제를 완료하고 테스트 통과까지 확인한 상태다.

완료한 테스트:

`src/test/java/com/team114/starbucks/domain/color/dto/in/ColorRequestDtoTest.java`

검증한 동작:

`ColorRequestDto.toEntity()`를 호출했을 때 `colorName`이 `Color` 엔티티에 그대로 전달되는지 검증했다.

실행 환경 참고:

시스템 기본 Java 24에서는 Gradle 8.13의 테스트 태스크 생성 오류가 발생한다. 설치된 Java 17을 지정하면 테스트가 통과한다.

```bash
JAVA_HOME=$(/usr/libexec/java_home -v 17) bash gradlew test
```

두 번째 완료 과제 대상:

`src/main/java/com/team114/starbucks/domain/maincategory/dto/in/CreateMainCategoryReqDto.java`

완료한 테스트:

`src/test/java/com/team114/starbucks/domain/maincategory/dto/in/CreateMainCategoryReqDtoTest.java`

검증한 동작:

`CreateMainCategoryReqDto.toEntity(mainCategoryUuid)`가 UUID, 카테고리 이름, 이미지 값을 `MainCategory` 엔티티에 모두 전달하는지 검증한다.

사용자가 직접 작성했으며, UUID 기대값 불일치로 한 번 실패한 뒤 스스로 수정했다. Java 17 환경에서 테스트 통과를 확인했다.

완료한 테스트의 기본 뼈대:

```java
package com.team114.starbucks.domain.maincategory.dto.in;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateMainCategoryReqDtoTest {

    @Test
    void DTO를_엔티티로_변환하면_카테고리_정보가_유지된다() {
        // given

        // when

        // then
    }
}
```

두 번째 테스트를 확인한 명령:

```bash
JAVA_HOME=$(/usr/libexec/java_home -v 17) bash gradlew test --tests CreateMainCategoryReqDtoTest
```

세 번째 완료 과제:

`src/test/java/com/team114/starbucks/domain/auth/dto/in/CreateSignUpReqDtoTest.java`

`CreateSignUpReqDto.toEntity(password)`가 회원 정보, 메서드 인자로 받은 비밀번호, `ROLE_USER`를 옮기고 UUID를 생성하는지 JUnit Assertions로 검증했다. 생일은 `Instant.now()` 대신 고정된 `2000-01-01T00:00:00Z`를 사용했다.

```bash
JAVA_HOME=$(/usr/libexec/java_home -v 17) bash gradlew test --tests CreateSignUpReqDtoTest
```

네 번째 완료 과제:

`src/test/java/com/team114/starbucks/domain/member/enums/GenderTest.java`

`Gender.fromString()`의 정상 입력은 `assertEquals()`로, 알 수 없는 입력은 `assertThrows()`로 `IllegalArgumentException`과 예외 메시지를 검증했다. JUnit에 기대 예외 타입 정보를 넘기는 `IllegalArgumentException.class`와 람다식의 역할도 학습했다.

```bash
JAVA_HOME=$(/usr/libexec/java_home -v 17) bash gradlew test --tests GenderTest
```

전체 테스트 6개 중 작성한 단위 테스트 5개는 통과했다. 기존 `StarbucksApplicationTests.contextLoads()`는 테스트 환경에서 MySQL 호스트에 연결하지 못해 실패한다. 현재 단위 테스트의 실패로 판단하지 말 것.

## 에이전트가 작업을 시작할 때

- 먼저 이 파일과 현재 Git 상태를 확인한다.
- 사용자가 작성한 변경사항을 보존한다.
- 현재 과제의 완료 여부를 테스트 파일과 실행 결과로 확인한다.
- 완료됐다면 짧은 피드백 후 다음 과제를 하나만 제시한다.
- 과제 진행 상황이 달라지면 이 문서의 `현재 진행 상황`을 최신 상태로 갱신한다.
