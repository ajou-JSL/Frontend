# Step 1: Base image
FROM mobiledevops/android-sdk-image

# Step 2: dos2unix 설치
RUN apt-get update && apt-get install -y dos2unix

# Step 3: 작업 디렉토리 설정
WORKDIR /app

# Step 4: 프로젝트 복사
COPY . /app

# Step 5: gradlew 줄바꿈 형식 변환
RUN dos2unix ./gradlew

# Step 6: Gradle 빌드 실행
RUN ./gradlew assembleRelease