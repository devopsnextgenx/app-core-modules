package io.devopsnextgenx.microservices.modules.vault;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.devopsnextgenx.base.modules.credentials.models.AppxUserList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VaultAutoConfiguration.class)
@TestPropertySource(locations="classpath:application.yml")
@TestPropertySource(properties = { "spring.cloud.vault.enabled = false" })
public class NoVaultAutoConfigurationTest {
    @Autowired
    private VaultAutoConfiguration vaultAutoConfiguration;

    @Autowired
    private AppxUserList appxUserList;
    
    @Test
    public void testConfigurationProperties() {
        assertEquals("secret00-0000-0000-0000-000000000yml", vaultAutoConfiguration.getJwtSecret());
        assertEquals(1, appxUserList.getUserList().size());
        assertEquals("yadmin@devopsnextgenx.io", appxUserList.getUserList().get(0).getEmail());
        assertEquals("yp@ssw0rd", appxUserList.getUserList().get(0).getPassword());
        assertEquals("yadmin", appxUserList.getUserList().get(0).getUsername());
        assertEquals("yeureka", appxUserList.getUserList().get(0).getFirstName());
        assertEquals("yconfig", appxUserList.getUserList().get(0).getLastName());
        assertEquals(5, appxUserList.getUserList().get(0).getRoles().size());
    }
}