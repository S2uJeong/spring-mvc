package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 애노테이션 기반의 스프링 컨트롤러는 다양한 파라미터를 지원한다.
 * HTTP 헤더 정보를 조회하는 방법
 */
@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,  // HTTP 메서드 조회
                          Locale locale, // Locale 정보 조회
                          @RequestHeader MultiValueMap<String, String> headerMap, // 모든 HTTP 헤더를 MultiValueMap(하나의 키에 여러 값) 형식으로 조회
                          @RequestHeader("host") String host, // 특정 HTTP 헤더 조회
                          @CookieValue(value = "myCookie", required = false) String cookie) // 쿠키 조회
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

}
