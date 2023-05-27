package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

// http://localhost:8080/request-param-v4?username=hello&age=20

@Slf4j
@Controller
public class RequestParamController {
    /**
     * HttpServletRequest가 제공하는 방식으로 요청 파라미터 조회
     */
    // @Controller여도, 반환 타입이 없으면서 응답에 직접 값을 넣으면 View 조회 하지 않음
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("v1");
    }

    /**
     * @RequestParam 사용 - 파라미터 이름으로 바인딩한다.
     * @ResponseBody 추가 - view 조회를 무시하고, HTTP message body에 직접 해당 내용 입력 (@RestController 와 비슷한 기능)
     */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParaV2(@RequestParam("username") String memberName, @RequestParam("age") int memberAge) {
                                // == request.getParameter("username")
        log.info("username={}, age={}", memberName, memberAge);
        return "v2";
    }

    // HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username, @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return "v3";
    }

    // String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "v4";
    }

    /**
     *  @RequestParam.required  - 파라미터 필수 여부
     *      /request-param-required -> username이 없으므로 에러
     *      /request-param-required?username= -> 빈문자로 통과
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam.defaultValue - 파라미터에 값이 없는 경우 기본값 적용
     * required와 달리 공백이 들어가지 않음.
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터 Map으로 조회
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
}
