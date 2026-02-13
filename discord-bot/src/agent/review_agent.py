import asyncio
from asyncio.subprocess import PIPE
from pathlib import Path

REVIEW_PROMPT = """아래 git diff를 코드 리뷰해줘. 다음 관점에서 검토하고 한국어로 답변해줘:

1. **버그 및 논리 오류**: 잠재적 버그, null 체크 누락, 경계 조건 문제
2. **보안 취약점**: SQL injection, XSS, 인증/인가 문제, 민감 정보 노출
3. **성능**: N+1 쿼리, 불필요한 연산, 메모리 누수 가능성
4. **코드 품질**: 네이밍, 중복 코드, SOLID 원칙 위반
5. **Spring Boot 관련**: 트랜잭션 관리, Bean 스코프, DI 패턴

각 이슈에 대해 심각도(Critical/Warning/Info)와 구체적인 개선 방안을 제시해줘."""

MAX_RUNTIME_SEC = 600


def _build_diff_cmd(target: str | None) -> list[str]:
    if target is None or target == "" or target == "working":
        return ["git", "diff"]
    if target == "staged":
        return ["git", "diff", "--cached"]
    return ["git", "diff", f"{target}...HEAD"]


async def _run_command(
    cmd: list[str],
    *,
    stdin_text: str | None = None,
    cwd: Path | None = None,
) -> tuple[int, str, str]:
    proc = await asyncio.create_subprocess_exec(
        *cmd,
        stdin=PIPE,
        stdout=PIPE,
        stderr=PIPE,
        cwd=str(cwd) if cwd else None,
    )

    input_data = stdin_text.encode("utf-8") if stdin_text is not None else None
    stdout, stderr = await asyncio.wait_for(proc.communicate(input_data), timeout=MAX_RUNTIME_SEC)
    return proc.returncode, stdout.decode("utf-8", errors="replace"), stderr.decode("utf-8", errors="replace")


async def run_codex_review(target: str | None = None, repo_path: Path | None = None) -> str:
    """
    현재 Git 변경사항을 Codex CLI로 리뷰하여 텍스트 결과를 반환한다.

    Args:
        target: working | staged | 브랜치명
        repo_path: git diff를 수행할 저장소 루트 경로. 기본값은 현재 작업 디렉토리.
    """
    diff_cmd = _build_diff_cmd(target)
    rc, diff_text, diff_err = await _run_command(diff_cmd, cwd=repo_path)
    if rc != 0:
        err = diff_err.strip() or "git diff 실행 실패"
        raise RuntimeError(f"`{' '.join(diff_cmd)}` 실패: {err}")

    if not diff_text.strip():
        return "리뷰할 변경사항이 없습니다."

    codex_cmd = ["codex", "-q", "--full-context", REVIEW_PROMPT]
    rc, review_text, review_err = await _run_command(codex_cmd, stdin_text=diff_text, cwd=repo_path)
    if rc != 0:
        err = review_err.strip() or "codex CLI 실행 실패"
        raise RuntimeError(f"Codex 리뷰 실행 실패: {err}")

    result = review_text.strip()
    return result or "Codex가 리뷰 결과를 반환하지 않았습니다."

