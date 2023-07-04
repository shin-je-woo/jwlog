# jwlog

## 댓글 -> 테이블 모델링 (comment) (= comment Entity), 몇 단계로 할지? (댓글 or 대댓글)

## 비공개, 공개 여부 (상태) -> enum

## 카테고리 -> DB or enum

## 로그인 -> spring security

## 패스워드 암호화 -> scrypt, bcrypt
1. 해시
2. 해시 방식 
   1. SHA1
   2. SHA256 
   3. MD5
   4. 왜 이런걸로 비밀번호 암호화 하면 안되는지 
3. BCrypt, SCrypt, Argon2
4. salt 값

# AWS EC2

## ec2 인스턴스에 ssh 접속하기
- ssh -i C:\Users\shinj\.ssh\jwlog.pem ec2-user@3.38.213.211
- 인수는 차례대로 ssh키파일, [목적지 유저이름]@[ip]

## ec2 인스턴스에 파일 올리기 SCP (bootJar)
- scp -i C:\Users\shinj\.ssh\jwlog.pem .\jwlog-0.0.1-SNAPSHOT.jar ec2-user@3.38.213.211:/home/ec2-user
- -i 뒤에 인수는 차례대로 ssh키파일, 업로드할 파일, [목적지 유저이름]@[ip]:[목적지 디렉토리]

## ec2 인스턴스에 jdk17 설치
- wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.rpm
- sudo rpm -ivh jdk-17_linux-x64_bin.rpm
- sudo alternatives --config java (이 과정은 jdk가 여러개 설치되어 있을 때 버전 선택용도로 사용)

## ec2 인스턴스에서 jar파일 실행 및 접속하기
- ssh 접속후  java -jar jwlog-0.0.1-SNAPSHOT.jar
- 포트는 기본 8080
- 브라우저에서 ip:8080 입력하면 접속 안됨(네트워크 설정을 안했기 때문에)

## ec2 네트워킹 및 보안
- 8080포트에 대한 인바운드 규칙 설정해야 함

## 서버관리 명령어
- nohup & << 프로세스 유지하는 명령어
- ps -aux << 프로세스 확인 명령어
- netstat -lntp << 연결가능한 상태(listen)의 tcp연결 포트, 프로세스ID 확인

## 고정IP 할당하기
- AWS에서 탄력적 IP할당하기(보통 기본값 사용)
- 탄력적 IP 주소 연결 기능을 사용해 인스턴스에 연결하기
- 이제 인스턴스를 재시작해도 고정된 IP로 사용할 수 있음
