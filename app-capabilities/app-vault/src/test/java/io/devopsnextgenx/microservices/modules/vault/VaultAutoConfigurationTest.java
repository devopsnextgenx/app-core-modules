package io.devopsnextgenx.microservices.modules.vault;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.devopsnextgenx.base.modules.config.models.AppxUserList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VaultAutoConfiguration.class)
@TestPropertySource(locations="classpath:application.yml")
@TestPropertySource(properties = { "spring.cloud.vault.enabled = true" })
public class VaultAutoConfigurationTest {
    @Autowired
    private VaultAutoConfiguration vaultAutoConfiguration;

    @Autowired
    private AppxUserList appxUserList;
    
    @Test
    public void testConfigurationProperties() {
        assertEquals("vaultSecret", vaultAutoConfiguration.getJwtSecret());
        assertEquals(1, appxUserList.getUserList().size());
        assertEquals("admin@devopsnextgenx.io", appxUserList.getUserList().get(0).getEmail());
        assertEquals("p@ssw0rd", appxUserList.getUserList().get(0).getPassword());
        assertEquals("admin", appxUserList.getUserList().get(0).getUsername());
        assertEquals("eureka-vault", appxUserList.getUserList().get(0).getFirstName());
        assertEquals("config-vault", appxUserList.getUserList().get(0).getLastName());
        assertEquals(5, appxUserList.getUserList().get(0).getRoles().size());
    }
}