# 📚 도서관 관리 시스템 (Library Management System)

Java Swing을 기반으로 한 도서관 관리 시스템입니다. 도서 관리, 사용자 관리, 대출/반납 관리 등의 기능을 제공합니다.

## 🎯 프로젝트 개요

이 시스템은 도서관의 효율적인 운영을 위해 개발된 데스크톱 애플리케이션입니다. 관리자와 일반 사용자로 구분된 권한 시스템을 통해 도서관 업무를 체계적으로 관리할 수 있습니다.

## ✨ 주요 기능

### 👤 사용자 관리
- 회원가입 및 로그인
- 관리자/일반 사용자 권한 구분
- 사용자 정보 관리

### 📖 도서 관리
- 도서 등록/수정/삭제
- 도서 검색 (제목, 저자, 카테고리 등)
- 바코드 자동 생성
- 청구기호 자동 생성
- 도서 상태 관리

### 🔄 대출/반납 관리
- 도서 대출 처리
- 도서 반납 처리
- 대출 이력 관리
- 연체 도서 관리

### 📊 통계 및 관리
- 대출 현황 조회
- 연체 도서 목록
- 도서 재고 관리

## 🏗️ 시스템 아키텍처

### MVC 패턴 구조
```
src/
├── Main.java                 # 메인 클래스
├── controller/              # 컨트롤러 레이어
│   ├── BookController.java
│   ├── LoanController.java
│   ├── LoginController.java
│   ├── MainController.java
│   ├── SignupController.java
│   └── UserController.java
├── model/                   # 모델 레이어
│   ├── Admin.java
│   ├── Book.java
│   ├── CsvInOut.java
│   ├── Library.java
│   ├── Loan.java
│   ├── Member.java
│   ├── User.java
│   └── Users.java
└── view/                    # 뷰 레이어
    ├── AdminAddUserDialog.java
    ├── BookDetail.java
    ├── BookEdit.java
    ├── BookManage.java
    ├── BookSearch.java
    ├── LoanView.java
    ├── LoginView.java
    ├── MainMenu.java
    ├── MyPage.java
    ├── SignView.java
    └── UserManage.java
```

### 데이터 저장
```
data/
├── books.csv    # 도서 정보
├── loans.csv    # 대출 정보
└── users.csv    # 사용자 정보
```

## 🚀 실행 방법

### 필요 조건
- Java 8 이상
- Java Swing 지원 환경

### 설치 및 실행
1. 저장소 클론
```bash
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system
```

2. 컴파일
```bash
javac -d . src/**/*.java
```

3. 실행
```bash
java Main
```

### 기본 관리자 계정
- **아이디**: admin
- **비밀번호**: admin123

## 💡 사용법

### 일반 사용자
1. 회원가입 후 로그인
2. 도서 검색 및 조회
3. 도서 대출 신청
4. 마이페이지에서 대출 이력 확인

### 관리자
1. 관리자 계정으로 로그인
2. 도서 등록/수정/삭제
3. 사용자 관리
4. 대출/반납 처리
5. 통계 조회

## 🔧 기술 스택

- **언어**: Java
- **GUI**: Java Swing
- **데이터 저장**: CSV 파일
- **아키텍처**: MVC 패턴

## 📋 주요 클래스 설명

### Model 클래스
- **Library**: 도서관 전체 관리 로직
- **Book**: 도서 정보 모델
- **User**: 사용자 정보 모델
- **Loan**: 대출 정보 모델
- **CsvInOut**: 파일 입출력 처리

### Controller 클래스
- **MainController**: 메인 화면 제어
- **BookController**: 도서 관련 비즈니스 로직
- **LoanController**: 대출/반납 처리
- **UserController**: 사용자 관리

### View 클래스
- **LoginView**: 로그인 화면
- **MainMenu**: 메인 메뉴 화면
- **BookManage**: 도서 관리 화면
- **BookSearch**: 도서 검색 화면

## 🎨 주요 기능 설명

### 도서 관리
- 바코드 자동 생성 (1000001부터 순차 생성)
- 청구기호 자동 생성 (카테고리별 분류)
- 도서 상태 관리 (정상, 손상, 분실 등)

### 대출 시스템
- 14일 대출 기간
- 연체 도서 자동 계산
- 대출 ID 자동 생성 (L0001 형식)

### 검색 기능
- 다중 조건 검색 지원
- 실시간 검색 결과 표시
- 대소문자 구분 없는 검색

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스
이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

## 📞 문의

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.

---

⭐ 이 프로젝트가 도움이 되었다면 스타를 눌러주세요!