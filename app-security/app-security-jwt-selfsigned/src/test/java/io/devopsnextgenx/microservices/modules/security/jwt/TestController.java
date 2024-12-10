package io.devopsnextgenx.microservices.modules.security.jwt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TestController {

    public static final String TEST_REPLY_FROM_CONTROLLER = "test-reply-from-controller";

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(TEST_REPLY_FROM_CONTROLLER);
    }
}
