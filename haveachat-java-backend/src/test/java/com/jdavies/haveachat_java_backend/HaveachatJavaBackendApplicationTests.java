package com.jdavies.haveachat_java_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestcontainersConfiguration.class) 
@SpringBootTest
class HaveachatJavaBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}