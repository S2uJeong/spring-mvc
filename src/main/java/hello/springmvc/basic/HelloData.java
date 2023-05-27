package hello.springmvc.basic;

import lombok.Data;

/**
 * @Data
 * - @Getter, @Setter, @ToString,@EqualsAndHashCode , @RequiredArgsConstructor 를 자동 적용 해줌.
 */
@Data
public class HelloData {
    private String username;
    private int age;

}
