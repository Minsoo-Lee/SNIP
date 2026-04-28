# 1. 베이스 이미지
FROM gradle:8-jdk21
WORKDIR /app

# 2. 의존성만 먼저 복사 (캐시 핵심)
COPY build.gradle settings.gradle ./
COPY gradle gradle

# 3. 의존성 다운로드 (이 레이어가 캐시됨)
RUN gradle dependencies --no-daemon

# 4. 나머지 소스 복사
COPY . .

# 5. 실행
CMD ["./gradlew", "bootRun", "--no-daemon"]