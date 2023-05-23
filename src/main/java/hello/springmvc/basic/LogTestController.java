package hello.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그 사용시 장점
 * 로그 레벨에 따라 상황에 필요한 로그를 분류해서 볼 수 있다.
 * 콘솔 뿐 아니라 파일이나, 네트워크 등 로그를 별도의 위치에 남길 수 있다.
 */

//@Slf4j
@RestController
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        /**
         * LEVEL: TRACE > DEBUG > INFO > WARN > ERROR
         */
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info(" info log={}", name);
        log.warn(" warn log={}", name);
        log.error("error log={}", name);

        // 로그 출력 레벨을 info로 설정해도 해당 코드의 더하기 연산이 강제 실행됨,
        // info 단계면 해당 log는 출력되지 않아도 되는데, 의미없는 연산이 일어나게 되는것.  -> 이런 방식으로 사용하면 X
        log.debug("String concat log=" + name);
        // log.debug("data={}", data) 이렇게 쓸것.

        return "ok"; // RestController라서 view가 아니라 화면에 ok란 String 자체가 뜬다. (HTTP 메시지 바디에 바로 입력)
    }
}
