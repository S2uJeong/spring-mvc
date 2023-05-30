package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * 요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 넘어오면
 * @RequestParam, @ModelAttrivbute 를 사용할 수 없다.
 *
 * - ServletRequest의 InputStream 을 사용
 * - 스프링 MVC가 지원하는 파라미터
 * - inputStream(Reader) + responseWriter
 * - HttpEntity<T>
 * - @RequestBody + @RequestBody

 */

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");

    }

    /**
     * 스프링 MVC가 지원하는 파라미터.
     * @param inputStream(Reader) - HTTP 요청 메시지 바디의 내용을 직접 조회
     * @param responseWriter - HTTP 응답 메시지의 바디에 직접 결과 출력
     *
     * 이를 사용함으로써 V1과는 달리 HttpServletRequest/Response를 파라미터로 사용하지 않아도 요청 조회, 출력이 가능하다.
     *
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
       // ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");

    }

    /**
     * httpEntity
     * - HTTP header, body 정보를 편리하게 조회 가능
     * - 메세지 바디 정보 직접 조회
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용 - 스프링 MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달하는 역할.
     * 응답 - 메세지 바디 정보 직접 반환 (view 조회 X), 헤더 정보 포함 가능
     *
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {

        // String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        String messageBody = httpEntity.getBody();

        log.info("messageBody={}", messageBody);

        return new HttpEntity<>("ok");
    }

    /**
     * @RequestBody
     * - 메시지 바디 정보를 직접 조회
     * - HttpMessageConverter 사용
     * @ResponseBody
     * - 메시지 바디 정보 직접 반환 (veiw 조회 X)
     * - HttpMessageConverter 사용
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        // String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return"ok";
    }



}
