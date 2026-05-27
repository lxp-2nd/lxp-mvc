# LXP MVC

Spring MVC 기반의 학습 경험 플랫폼(LXP) 예제 애플리케이션입니다. 강의 탐색, 강의 상세 조회, 장바구니, 수강 신청, 수강 목록, 회원 로그인/가입/프로필 관리를 Thymeleaf 화면으로 제공합니다.

## 기술 스택

- Java 17
- Spring Boot 3.5.14
- Spring MVC, Thymeleaf
- Spring Security
- Spring Data JPA, Hibernate
- H2 Database(local), MySQL Driver
- Gradle
- Lombok
- Checkstyle(Naver convention)

## 실행 방법

```bash
./gradlew bootRun
```

기본 프로필은 `local`입니다. `application-local.yml` 기준으로 H2 인메모리 DB를 사용하며, 애플리케이션 시작 시 로컬 개발용 회원/강의/수강 데이터가 초기화됩니다.

```bash
./gradlew test
```

테스트와 컴파일 검증은 위 명령으로 실행합니다.

## 로컬 접속 정보

- 애플리케이션: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:test`
- Username: `sa`
- Password: 비어 있음

로컬 초기 계정 예시:

| 역할 | 이메일 | 비밀번호 |
| --- | --- | --- |
| 일반 회원 | `user@email.com` | `qwer1234!@#$` |
| 일반 회원 | `test@test.test` | `test1!` |
| 강사 | `instructor1@email.com` | `qwer1234!@#$` |

## 주요 기능

### 강의

- `/courses`에서 신청 가능한 강의 목록을 조회합니다.
- 비로그인 사용자는 삭제되지 않은 전체 강의를 볼 수 있습니다.
- 로그인 사용자는 이미 수강 중인 강의를 제외한 목록을 볼 수 있습니다.
- 강의명 검색을 지원합니다.
- 강의 카드는 썸네일, 제목, 강사명, 수강 인원을 표시합니다.
- 카드의 `상세 보기` 버튼뿐 아니라 카드 본문 영역을 클릭해도 상세 페이지로 이동합니다.
- `/courses/{courseId}`에서 강의 상세, 커리큘럼, 강사 정보를 확인합니다.
- 이미 수강 중인 강의 상세 화면에서는 `바로 신청`, `장바구니 담기` 대신 비활성화된 `수강 중` 버튼을 표시합니다.

### 장바구니

- 로그인 회원은 강의 상세에서 강의를 장바구니에 담을 수 있습니다.
- 장바구니가 없으면 최초 담기 시 자동 생성합니다.
- 동일 강의가 이미 삭제된 장바구니 항목으로 존재하면 새로 만들지 않고 복구합니다.
- 장바구니 화면은 삭제되지 않은 항목만 최신 수정순으로 보여줍니다.
- 선택한 장바구니 항목을 삭제할 수 있습니다.
- 장바구니에서 여러 강의를 선택해 한 번에 수강 신청할 수 있습니다.
- 수강 신청이 완료된 강의는 장바구니에서 soft delete 처리됩니다.

### 수강 신청과 수강 목록

- 단일 수강 신청은 회원/강의 존재 여부와 soft delete 상태를 검증합니다.
- 이미 정상 수강 중인 강의는 중복 신청을 막습니다.
- 과거에 soft delete된 수강 이력이 있으면 새로 만들지 않고 복구합니다.
- DB unique 제약 충돌은 중복 수강 예외로 처리합니다.
- 수강 신청 성공 후 해당 강의가 장바구니에 있으면 장바구니 항목을 제거합니다.
- `/enrollment`에서 로그인 회원의 활성 수강 목록을 조회합니다.
- 수강 목록은 강의 썸네일, 제목, 강사명, 신청일을 표시합니다.
- `/learn/{courseId}` 수강 화면은 현재 `MockLxpData` 기반으로 커리큘럼과 선택 강의자료 유형/제목을 표시합니다.

### 회원

- 이메일/비밀번호 기반 로그인과 회원가입을 제공합니다.
- 회원가입 후 자동 로그인됩니다.
- 프로필 닉네임 수정과 회원 탈퇴를 지원합니다.
- 인증 정보는 `AuthInfo`를 Spring Security principal로 사용하고, 세션에 SecurityContext를 직접 동기화합니다.

## 주요 URL

| Method | URL | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/` | 강의 목록으로 리다이렉트 | 선택 |
| GET | `/courses` | 강의 목록 | 선택 |
| GET | `/courses/{courseId}` | 강의 상세 | 선택 |
| POST | `/courses/{courseId}/cart` | 장바구니 담기 | 필요 |
| GET | `/cart` | 장바구니 조회 | 필요 |
| POST | `/cart/items/delete` | 장바구니 항목 선택 삭제 | 필요 |
| POST | `/courses/{courseId}/enroll` | 단일 수강 신청 | 필요 |
| POST | `/cart/enroll` | 장바구니 선택 강의 수강 신청 | 필요 |
| GET | `/enroll/complete` | 수강 신청 완료 화면 | 필요 |
| GET | `/enrollment` | 내 수강 목록 | 필요 |
| GET | `/learn/{courseId}` | 수강 화면 | 필요 |
| GET/POST | `/login` | 로그인 화면/처리 | 비인증 |
| POST | `/logout` | 로그아웃 | 필요 |
| GET/POST | `/signup` | 회원가입 화면/처리 | 비인증 |
| GET/POST/DELETE | `/profile` | 프로필 조회/수정/탈퇴 | 필요 |

## 도메인 구조

```text
Member
  └─ Cart
       └─ CartItem ── Course

Member ── Enrollment ── Course

Course
  └─ Section
       └─ Material
```

- `BaseEntity`는 생성일, 수정일, 삭제일을 공통으로 관리합니다.
- 삭제 처리는 대부분 `deletedAt` 기반 soft delete로 동작합니다.
- `Enrollment`는 회원과 강의 조합에 unique 제약을 둡니다.
- `Course`는 강사 정보를 `CourseInstructor` 값 타입으로 내장합니다.
- `Material`은 현재 제목, 자료 유형, 콘텐츠 URL, 순서를 가집니다.

## 패키지 구조

```text
src/main/java/wanted/jjsbd/lxpmvc
├── cart          # 장바구니 도메인, 컨트롤러, 서비스, 저장소
├── common        # 공통 도메인, 예외, 로컬 초기 데이터
├── config        # JPA Auditing, Security 설정
├── course        # 강의, 섹션, 자료 도메인과 강의 조회
├── enrollment    # 수강 신청, 수강 목록, 학습 화면
└── member        # 회원, 인증 principal, 로그인/가입/프로필
```

## 화면과 정적 리소스

- 화면 템플릿은 `src/main/resources/templates` 아래에 있습니다.
- 공통 헤더와 head는 `templates/fragments`에서 관리합니다.
- CSS는 `src/main/resources/static/css/app.css`에 있습니다.
- 강의 썸네일은 `src/main/resources/static/images` 아래 이미지를 사용합니다.
- 정적 리소스는 `Cache-Control: public, max-age=365d` 정책으로 캐싱됩니다.
- CSS는 캐시 무효화를 위해 `head.html`에서 버전 쿼리 파라미터를 붙여 로드합니다.

## 검증

```bash
./gradlew test
```

현재 기본 테스트 구성은 Spring context 로딩과 일부 repository/domain 테스트를 포함합니다. 기능 변경 후에는 최소한 위 명령으로 컴파일과 애플리케이션 컨텍스트 로딩을 확인합니다.

## 발표 자료

[발표자료](https://www.miricanvas.com/v2/ko/design2/07c3dae4-acbe-4574-acb2-904e681ae98c?location=design&type=copy_link&access=direct)
