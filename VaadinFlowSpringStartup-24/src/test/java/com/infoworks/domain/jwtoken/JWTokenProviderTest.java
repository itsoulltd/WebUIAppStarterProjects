package com.infoworks.domain.jwtoken;

import com.infoworks.utils.jwt.TokenProvider;
import com.infoworks.utils.jwt.impl.JWebToken;
import com.infoworks.utils.jwt.models.JWTHeader;
import com.infoworks.utils.jwt.models.JWTPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class JWTokenProviderTest {

    static Logger LOG = LoggerFactory.getLogger(JWTokenProviderTest.class);

    @Test
    public void test() {
        LOG.info("Hallo info");
        LOG.debug("Hallo debug");
        LOG.error("Hallo error");
        Assertions.assertTrue(true);
    }

    @Test
    public void tokenProviderTest() {
        //FIXME: In JFoundationKit
        String[] roles = {"ADMIN", "ADMIN_TEST"};
        JWTHeader header = new JWTHeader().setAlg("HS256").setTyp("JWT");
        JWTPayload payload = new JWTPayload()
                .setIss("towhid@gmail.com")
                .addData("roles", String.join(",", roles))
                .addData("username", "towhid@gmail.com");
        TokenProvider provider = new JWebToken();
        String token = provider.generateToken(UUID.randomUUID().toString(), header, payload);
        LOG.info("Token: " + token);
        Assertions.assertTrue(token != null || !token.isEmpty());
    }
}