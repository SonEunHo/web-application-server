# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* 
- 힘들게 File을 열고 스트림을 따내고, 내용을 읽어내도 되지만, Files 이라는 유틸성 클래스를 이용하면 간편하게 byte[] 를 얻어낼 수 있다.
- 크롬에서 localhost:<port> 혹은 localhost:<port>/index.html 을 치니까 총 3번의 요청을 보낸다.
default html, default css, dafault icon(favicon.ico) 를 요청하는 것이었다.
-그리고 html에 기재된 <script> , <css> 등의 리소스도 각 요청으로 요청한다.

### 요구사항 2 - get 방식으로 회원가입
* HttpRequestUtils의 존재를 알게 됐다. 적절한 파싱 방법을 골라 쓰면 될 듯 
* 이 HttpRequestUtils, IOUtils는 미리 만들어둔 utils구나
* routin table의 절실함을 알았다.
* html은 공식적으로 GET, POST 요청만 지원한다. 이유는 form태그의 method 프로퍼티 설정이 get, post만 가능하기 때문에.

### 요구사항 3 - post 방식으로 회원가입
* 라우팅이고 자시고, 요청의 내용을 알아야하니 제일 먼저 HTTP 패킷을 파싱해내는 것이 최우선이라는 걸 알았다.

### 요구사항 4 - redirect 방식으로 이동
* 

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
*

### 요구사항 7 - css 적용
* css는 content-Type이 "text/css" 이다.
* js 파일은 content-Type이 "application/javascript"이다. 

### etc
* local개발시 사용한 path를 그대로 박아두니 당연히 서버에서 에러가 난다.
* 상대경로로 하던가 혹은 phase별로 경로를 설정해야할 것 같다.


### heroku 서버에 배포 후
* 
