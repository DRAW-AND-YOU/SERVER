GITHUB_OWNER = "DRAW-AND-YOU"
GITHUB_REPO = "SERVER"
GITHUB_BRANCH = "dev"

BASE_PATH = "src/main/java/com/drawandyou/drawandyou_server"

# 도메인 → GitHub 파일 경로 매핑
DOMAIN_REGISTRY: dict[str, dict[str, str]] = {
    "user": {
        "controller": f"{BASE_PATH}/domain/user/presentation/",
        "dto": f"{BASE_PATH}/domain/user/presentation/dto/",
    },
    "article": {
        "controller": f"{BASE_PATH}/domain/article/presentation/",
        "dto": f"{BASE_PATH}/domain/article/presentation/dto/",
    },
    "comment": {
        "controller": f"{BASE_PATH}/domain/comment/presesntation/",
        "dto": f"{BASE_PATH}/domain/comment/presesntation/",
    },
    "like": {
        "controller": f"{BASE_PATH}/domain/like/presentation/",
        "dto": f"{BASE_PATH}/domain/like/presentation/dto/",
    },
    "drawing-analysis": {
        "controller": f"{BASE_PATH}/domain/drawinganalysis/presentation/",
        "dto": f"{BASE_PATH}/domain/drawinganalysis/presentation/dto/",
    },
    "therapy-program": {
        "controller": f"{BASE_PATH}/domain/therapyprogram/presentation/",
        "dto": f"{BASE_PATH}/domain/therapyprogram/presentation/dto/",
    },
    "daily-course": {
        "controller": f"{BASE_PATH}/domain/dailycourse/presentation/",
        "dto": f"{BASE_PATH}/domain/dailycourse/presentation/dto/",
    },
    "diary": {
        "controller": f"{BASE_PATH}/domain/diary/presentation/",
        "dto": f"{BASE_PATH}/domain/diary/presentation/dto/",
    },
    "auth": {
        "controller": f"{BASE_PATH}/global/auth/presentation/",
        "dto": f"{BASE_PATH}/global/auth/presentation/dto/",
    },
    "s3": {
        "controller": f"{BASE_PATH}/global/storage/",
        "dto": f"{BASE_PATH}/global/storage/presentation/dto/",
    },
}

VALID_DOMAINS = list(DOMAIN_REGISTRY.keys())


def _format_domain_paths() -> str:
    lines = []
    for domain, paths in DOMAIN_REGISTRY.items():
        lines.append(f"- **{domain}**: Controller=`{paths['controller']}`, DTO=`{paths['dto']}`")
    return "\n".join(lines)


SYSTEM_PROMPT = f"""\
당신은 DRAW&YOU 프로젝트의 API 명세서 자동 업데이트 에이전트입니다.

## 프로젝트 아키텍처

- **Java 17 + Spring Boot 3.x** 기반 REST API 서버
- Domain-Driven Layered Architecture (presentation → application → domain)
- 모든 API 응답은 `ApiResponse<T>` 래퍼로 감싸짐
- 인증이 필요한 엔드포인트는 `@AuthenticationPrincipal Long userId` 사용
- Public 엔드포인트: `/api/auth/**`, `/api/user/signup`, `/api/user/signin`, `/api/s3/presigned-url`

## GitHub 저장소 정보

- Owner: `{GITHUB_OWNER}`
- Repo: `{GITHUB_REPO}`
- Branch: `{GITHUB_BRANCH}`

## 도메인별 파일 위치

{_format_domain_paths()}

## 작업 절차

1. **GitHub에서 코드 읽기**: 해당 도메인의 Controller 파일과 DTO 파일들을 읽습니다.
2. **API 엔드포인트 분석**: HTTP 메서드, 경로, 요청/응답 DTO, 인증 요구사항을 파악합니다.
3. **Notion 명세서 검색**: 기존 API 명세서 페이지를 검색합니다.
4. **명세서 업데이트**: 코드에서 추출한 최신 정보로 Notion 페이지를 업데이트합니다.

## API 명세서 포맷

각 엔드포인트에 대해 다음 정보를 포함하세요:
- **엔드포인트**: HTTP 메서드 + URL 경로
- **설명**: 엔드포인트의 목적
- **인증**: 필요 여부
- **Request**: Path Parameters, Query Parameters, Request Body (JSON 예시)
- **Response**: 응답 DTO 구조 (JSON 예시)

## 주의사항

- 반드시 `{GITHUB_BRANCH}` 브랜치의 최신 코드를 기준으로 작성합니다.
- `@Operation(summary = "...")` 어노테이션이 있으면 설명에 활용합니다.
- `@Valid`가 붙은 요청 DTO의 유효성 검증 조건도 명세에 포함합니다.
- Notion 페이지가 없으면 새로 생성하지 말고, 사용자에게 알려주세요.
"""


def build_user_message(domain: str, endpoint: str | None = None) -> str:
    if domain == "all":
        domain_list = ", ".join(VALID_DOMAINS)
        return (
            f"다음 모든 도메인의 API 명세서를 업데이트해주세요: {domain_list}\n"
            "각 도메인의 Controller와 DTO를 읽고, Notion의 해당 명세서 페이지를 업데이트하세요."
        )
    paths = DOMAIN_REGISTRY.get(domain)
    if not paths:
        return f"'{domain}' 도메인을 찾을 수 없습니다. 유효한 도메인: {', '.join(VALID_DOMAINS)}"

    base_msg = (
        f"'{domain}' 도메인의 API 명세서를 업데이트해주세요.\n"
        f"Controller 경로: `{paths['controller']}`\n"
        f"DTO 경로: `{paths['dto']}`\n"
    )

    if endpoint:
        base_msg += (
            f"\n**특정 엔드포인트만 업데이트하세요**: `{endpoint}`\n"
            "Controller에서 해당 엔드포인트 매핑을 찾고, 관련 DTO만 분석하여 "
            "Notion 명세서에서 해당 엔드포인트 부분만 업데이트하세요. "
            "다른 엔드포인트는 수정하지 마세요."
        )
    else:
        base_msg += "GitHub에서 해당 파일들을 읽고, Notion의 기존 명세서를 최신 코드 기반으로 업데이트하세요."

    return base_msg
