package hello.springmvc.basic.requestmapping;
/**
 * 요청이 왔을 때 어떤 컨트롤러를 매핑해야 하는가
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello-basic") // url 다중 설정 가능, 해당 url로 호출이 오면 이 메서드가 실행되도록 매핑
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    /**
     * HTTP 메서드 매핑
     */
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET) // 해당 url에 대해 post 요청을 하면 405 에러 코드 발생
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    // HTTP 메서드 매핑 축약
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }

    /**
     * PathVariable(경로 변수) 사용
     */

    // 변수명이 같으면 생략 가능
    //   @PathVariable("userId") String userId -> @PathVariable String userId
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    // 다중사용
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 미디어 타입 조건 매핑  - Content-Type
     */
    @PostMapping(value = "/mapping-consume", consumes = "application/json") // 컨텐츠 타입이 json 타입일때만 호출된다.
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * 미디어 타입 조건 매핑  - 헤더 Accept
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html") // 요청 시, accept 부분에 해당 미디어타입이 들어가 있어야 호출 된다.
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }


}
