# 블로그 체험단 일정 관리 API

블로거가 자신의 체험단 일정을 등록·조회·수정·삭제하고, 월별 비용 합계를 확인할 수 있는 백엔드 API입니다.
기존에 노션으로 관리하던 데이터를 1인 사용자형 SaaS API로 옮기는 것을 목표로 합니다.

## Tech Stack
* **Language:** Java 17
* **Framework:** Spring Boot 3.4.3
* **Persistence:** Spring Data JPA / Hibernate
* **Database:** MySQL 8.0 (Docker)
* **Security:** Spring Security + JWT (jjwt 0.12.6)
* **Validation:** Jakarta Bean Validation
* **Build Tool:** Gradle 8.10
* **Lombok**

## Architecture
계층형(Layered) 구조 — `controller → service → repository → domain`

```
com.commerce.project
├── config/         SecurityConfig (JWT 필터 등록, 인증 정책)
├── controller/     MemberController, ScheduleController
├── domain/         Member, Schedule, ScheduleStatus, Platform, RewardType
├── dto/            ApiResponse, *Request / *Response
├── exception/      GlobalExceptionHandler (검증/도메인 예외 → 400)
├── jwt/            JwtUtil, JwtFilter
├── repository/     MemberRepository, ScheduleRepository
└── service/        MemberService, ScheduleService
```

* 모든 응답은 `ApiResponse<T> { success, data, message }` 포맷
* 인증은 `Authorization: Bearer <token>` 헤더로 전달, 필터가 SecurityContext에 email을 주입
* 일정 조회/수정/삭제는 항상 owner-scoped (`findByIdAndMember`)로 다른 사용자 데이터 격리

## Domain
### Member
회원 (이메일/이름/비밀번호, BCrypt 해시 저장)

### Schedule (체험단 일정)
| 필드 | 타입 | 설명 |
|---|---|---|
| campaignName | String (필수) | 캠페인명 |
| companyName | String | 업체명 |
| category | String | 카테고리 (맛집/뷰티 등) |
| visitDate | LocalDateTime (필수) | 방문일시 (시:분:초 포함, 월별 조회 기준) |
| manuscriptDeadline | LocalDate | 원고마감일 |
| status | enum (필수) | APPLIED / SELECTED / IN_PROGRESS / POSTED / CLOSED |
| address | String | 방문주소 |
| contactNumber | String | 연락처 |
| cost | Long | 비용 (원) |
| memo | TEXT | 메모 |
| applyUrl / postUrl | String | 신청/포스팅 링크 |
| platform | enum | BLOG / INSTAGRAM / YOUTUBE / ETC |
| rewardType | enum | CASH / PRODUCT / SERVICE / ETC |

## API
### 인증 (공개)
| Method | Path | 설명 |
|---|---|---|
| POST | `/members/join` | 회원가입 |
| POST | `/members/login` | 로그인 → JWT 토큰 반환 |

### 일정 (인증 필요)
| Method | Path | 설명 |
|---|---|---|
| POST | `/schedules` | 일정 생성 |
| GET | `/schedules` | 본인 일정 목록 (`?year`, `?month`, `?status` 옵션) |
| GET | `/schedules/{id}` | 일정 상세 |
| PUT | `/schedules/{id}` | 일정 전체 수정 |
| PATCH | `/schedules/{id}/status` | 상태만 변경 |
| DELETE | `/schedules/{id}` | 일정 삭제 |
| GET | `/schedules/stats/monthly?year=YYYY` | 월별 비용 합계 (1~12월) |

## How to Run
### 1. MySQL 컨테이너 실행
```bash
docker-compose up -d
```
DB: `shop_db`, root 비밀번호: `password`, 포트 3306.

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```
서버: `http://localhost:8080`. 첫 실행 시 `schedule` 테이블이 자동 생성됩니다 (`spring.jpa.hibernate.ddl-auto=update`).

### 3. 빠른 동작 확인
```bash
# 회원가입
curl -X POST localhost:8080/members/join -H 'Content-Type: application/json' \
  -d '{"email":"a@a.com","name":"Jihyeon","password":"pw12345"}'

# 로그인 → 토큰
TOKEN=$(curl -s -X POST localhost:8080/members/login -H 'Content-Type: application/json' \
  -d '{"email":"a@a.com","password":"pw12345"}' | sed -n 's/.*"data":"\([^"]*\)".*/\1/p')

# 일정 생성
curl -X POST localhost:8080/schedules -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' -d '{
    "campaignName":"강남 맛집 체험","visitDate":"2026-05-20T19:30:00",
    "status":"APPLIED","cost":50000,"platform":"BLOG","rewardType":"SERVICE"
  }'

# 월별 비용 통계
curl "localhost:8080/schedules/stats/monthly?year=2026" -H "Authorization: Bearer $TOKEN"
```

## Postman
프로젝트 루트의 `postman_collection.json`을 Postman에서 Import하면 바로 사용 가능합니다.

* 컬렉션 변수 `{{baseUrl}}`, `{{token}}`, `{{scheduleId}}` 사전 설정됨
* `0. 인증 → 로그인` 실행 시 응답의 토큰이 자동으로 `{{token}}`에 저장 (Test 스크립트)
* 이후 모든 요청은 컬렉션 레벨 Bearer 인증으로 토큰 자동 적용
* 폴더: `0. 인증` / `1. 일정 CRUD` / `2. 통계` / `3. 검증 시나리오 (격리, 인증 누락, 검증 에러)`

## Trouble Shooting
### Gradle 8.12+ 와 Spring Boot 플러그인 호환성
초기 설정 시 `NoClassDefFoundError: org/gradle/util/VersionNumber` 발생.
- **원인:** 최신 Gradle에서 삭제된 클래스를 Spring Boot 플러그인이 참조
- **해결:** `gradle-wrapper.properties`에서 Gradle 버전을 안정적인 **8.10**으로 고정

## Configuration
`src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shop_db?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=commerce-project-secret-key-must-be-at-least-256-bits-long
jwt.expiration-ms=86400000   # 24시간
```
