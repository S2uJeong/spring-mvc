# 04_스프링 MVC - 기능

## 개요
- @PathVariable 경로변수
    ```java
    @GetMapping("/mapping/{userId}")  // 다중사용도 가능
    public String mappingPath(@PathVariable("userId") String data) {
     log.info("mappingPath userId={}", data);
     return "ok";
    }
    ```

- 회원 관리 API
    - 회원 목록 조회: GET /users
    - 회원 등록: POST /users
    - 회원 조회: GET /users/{userId}
    - 회원 수정: PATCH /users/{userId}
    - 회원 삭제: DELETE /users/{userId}

- 스프링 컨트롤러의 다양한 파라미터 지원
  ```java
    @RequestMapping("/headers")
  public String headers(HttpServletRequest request,
                        HttpServletResponse response,
                        HttpMethod httpMethod,
                        Locale locale,
                        @RequestHeader MultiValueMap<String, String> headerMap,
                        @RequestHeader("host") String host,    // 특정 HTTP 헤더를 조회
                        @CookieValue(value = "myCookie", required = false) String cookie )  // 특정 쿠키를 조회
  {
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";
  }
    ```
## HTTP 요청 메시지 유형별 사용법
### 01_파라미터
- HTTP 요청 파라미터에서 데이터 읽기 @RequestParam
    ```html
          <form action="/request-param-v2" method="post">
          username: <input type="text" name="username" />
          age: <input type="text" name="age" />
          <button type="submit">전송</button>
           </form>
    ```
    ```java
      // @RequestParam 사용 - 파라미터 이름으로 바인딩
          @ResponseBody
          @RequestMapping("/request-param-v2")
          public String requestParamV2(
          @RequestParam("username") String memberName,
          @RequestParam("age") int memberAge) {
          log.info("username={}, age={}", memberName, memberAge);
          return "ok";
          }
    ```
    - 이전에는 Request 객체를 통해 getParameter("") 하였지만, 코드에 적을 필요없이 애노테이션으로 인수로 받아온다.
    - String, int 등의 단순 타입이면  @RequestParam을 생략 가능하다. (권장 안함)
  ```java
        @ResponseBody
        @RequestMapping("/request-param-v2")
        public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
        }
  ``` 
    - 파라미터 필수 여부 지정 가능
        - @RequestParam(required = true/false) 을 지정해 꼭 받아와야 하는 값인지 지정 가능
        - 원시타입은 null값이 들어 갈 수 없어서, int 대신 Integer로 선언한 뒤, required = false를 지정할 수 있다.
        - 기본값은 true
    - 파라미터 기본 값 적용 가능
        - `@RequestParam(defaultValue = "guest") String username`
        - defaultValue 는 빈 문자의 경우에도 설정한 기본 값이 적용된다.
    - 파라미터를 Map 형식의 자료구조로 받을 수 있다.

- @ModelAttribute
    - 아래 과정을 자동화해준다.
    ```java
        @RequestParam String username;
        @RequestParam int age;
        HelloData data = new HelloData();
        data.setUsername(username);
        data.setAge(age);
    ```
    -  @ModelAttribute 사용 시,  model.addAttribute(helloData) 코드도 함께 자동 적용됨
    ```java
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(),helloData.getAge());
    return "ok";
    }
    ```
    - 실행과정
        - HelloData (파라미터를 바인딩 받을 클래스 미리 생성) 객체 생성
        - 요청 파라미터 이름으로 HelloData 객체의 프로퍼티 탐색
        - 해당 프로퍼티의 setter를 호출해서 파라미터 값을 입력
            - 파라미터 이름이 username이면 setUsernama()메서드를 찾아 호출하면서 값을 입력

### 02_메시지 바디
- 메시지 바디를 통해 데이터가 넘어오는 경우에는 @RequestParam , @ModelAttribute 를 사용할 수 없다. (물론 HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정)4
- Input, Output 스트림, Reader
    - 스프링 MVC가 지원하는 파라미터
        - InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
        - OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
      ```java
          @PostMapping("/request-body-string-v2")
          public void requestBodyStringV2(InputStream inputStream, Writer responseWriter)
              throws IOException {
                  String messageBody = StreamUtils.copyToString(inputStream,
                  StandardCharsets.UTF_8);
                  log.info("messageBody={}", messageBody);
                  responseWriter.write("ok");
          }
      ```

- HttpEntity
    - HTTP header, body 정보를 편리하게 조회 및 응답 가능  -  메시지 바디 정보 직접 반환(view 조회X)
  ```java
  @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
    String messageBody = httpEntity.getBody();
    log.info("messageBody={}", messageBody);
    return new HttpEntity<>("ok");
    }
  ```
    - HttpEntity를 상속 받은 ResponseEntity를 사용하면 HTTP 상태코드 설정도 가능함
      `return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)`

- @RequestBody, @ResponseBody
    - 메시지 바디 정보를 직접 조회 및 반환
    - 헤더 정보가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용
      ```java
      @ResponseBody
      @PostMapping("/request-body-string-v4")
      public String requestBodyStringV4(@RequestBody String messageBody) {
      log.info("messageBody={}", messageBody);
      return "ok";
      }
      ```

### 03_JSON
- 기존 서블릿 방식에서 로직
    1. HttpServletRequest를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어
    2. 문자로 변환 `.getInputStream()` -> `StreamUtils.copyToString`
    3. objectMapper을 통해 자바 객체로 변환
    - 문자로 변환하고 다시 json으로 변환하는 과정이 불편하다. @ModelAttribute처럼 한번에 객체로 변환할 수는 없을까.
- @RequestBody 에 직접 만든 객체를 지정할 수 있음을 이용
    - HttpEntity , @RequestBody 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.
  ```java
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
    log.info("username={}, age={}", data.getUsername(), data.getAge());
    return data;
    }
   ```
    - @RequestBody 요청
        - JSON 요청 -> HTTP 메시지 컨버터 -> 객체
    - @ResponseBody 응답
        - 객체 -> HTTP 메시지 컨버터 -> JSON 응답

### 결론
- 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
- HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
- JSON으로 요청 및 응답 : @RequestBody 객체 + @ResponseBody


## HTTP 응답
- 정적 리소스 : HTML, css, js
- 뷰 템플릿 : 동적인 HTML
- HTTP 메시지

### 뷰 템플릿
src/main/resources/templates/response/hello.html
```html
<body>
<p th:text="${data}">empty</p>
</body>
```
```java
// ModelAndView
@RequestMapping("/response-view-v1")
 public ModelAndView responseViewV1() {
 ModelAndView mav = new ModelAndView("response/hello")
 .addObject("data", "hello!");
 return mav;
 }
 
 // Model
@RequestMapping("/response-view-v2")
public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!!");
        return "response/hello"; // 실행: templates/response/hello.html
        }

@RequestMapping("/response/hello")
public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!!");
        }
```
- 논리값으로 반환해도 뷰 리졸버가 해당하는 물리적 경로에서 뷰를 찾을 수 있는 이유는
- 스프링부트가 자동으로 properties를 설정하기 때문이다.
  ```properties
  spring.thymeleaf.prefix=classpath:/templates/
  spring.thymeleaf.suffix=.html
  ```

### HTTP 메시지
- HTML이나 뷰 템플릿을 사용해도 HTTP 응답 메시지 바디에 HTML 데이터가 담겨서 전달된다.
- 해당 내용은 정적 리소스나 뷰 템플릿을 거치지 않고, 직접 HTTP 응답 메시지를 전달하는 경우이다.
```java
// ============= return String Message
// HttpServletResponse.getWriter().write - 메시지 바디에 직접 전달 
 @GetMapping("/response-body-string-v1")
 public void responseBodyV1(HttpServletResponse response) throws IOException
{
 response.getWriter().write("ok");
 }
 

// HttpEntity, ResponseEntity(Http Status 추가)
 @GetMapping("/response-body-string-v2")
 public ResponseEntity<String> responseBodyV2() {
 return new ResponseEntity<>("ok", HttpStatus.OK);
 }
 
 // @ResponseBody 
 @ResponseBody
 @GetMapping("/response-body-string-v3")
 public String responseBodyV3() {
 return "ok";
 }
 
 // ==================== return Json Message 
 // ResponseEntity - HTTP 메시지 컨버터를 통해서 JSON 형식으로 변환되어서 반환
 @GetMapping("/response-body-json-v1")
 public ResponseEntity<HelloData> responseBodyJsonV1() {
 HelloData helloData = new HelloData();
 helloData.setUsername("userA");
 helloData.setAge(20);
 return new ResponseEntity<>(helloData, HttpStatus.OK);
 }
 
 // @ResponseBody + @ResponseStatus 
// 응답 상태를 프로그램 조건에 따라서 동적으로 변경하려면 ResponseEntity 를 사용
 @ResponseStatus(HttpStatus.OK)
 @ResponseBody
 @GetMapping("/response-body-json-v2")
 public HelloData responseBodyJsonV2() {
 HelloData helloData = new HelloData();
 helloData.setUsername("userA");
 helloData.setAge(20);
 return helloData;
 }

```

## HTTP 메시지 컨버터
- JSON 데이터를 HTTP 메시지 바디에서 읽거나 쓰는 경우 사용하면 편리함
- @ResponseBody, HttpEntity를 사용하게 되면
    - viewResolver 대신 HttpMessageConverter가 동작하며
    - 문자는 StringHttpMessageConverter
    - 객체는 MappingJackson2HttpMessageConverter
    - 등 여러 HttpMessageConverter가 기본으로 등록되어 있다

### 요청 매핑 핸들러 어댑터 구조
- HTTP 메시지 컨버터는 스프링 MVC 어디쯤에서 사용되나
- @RequestMapping을 처리하는 핸들러 어댑터인 RequestMappingHandlerAdapter 동작방식을 자세히 보자.
    - image/RequestMappingHandlerAdapter 동작방식.png
    - 핸들러 어댑터와 핸들러(컨트롤러) 중간에 ArgumentResolver와 ReturnValueHandler을 호출한다.
        - ArgumentResolver : 컨트롤러의 파라미터, 애노테이션 정보를 기반으로 전달 데이터 생성
        - ReturnValueHandler : 컨트롤러의 반환 값을 변환