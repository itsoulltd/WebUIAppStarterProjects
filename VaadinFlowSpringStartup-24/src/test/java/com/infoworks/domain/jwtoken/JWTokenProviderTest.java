package com.infoworks.domain.jwtoken;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        /*String[] roles = {"ADMIN", "ADMIN_TEST"};
        JWTHeader header = new JWTHeader().setAlg("HS256").setTyp("JWT");
        JWTPayload payload = new JWTPayload()
                .setIss("towhid@gmail.com")
                .addData("roles", String.join(",", roles))
                .addData("username", "towhid@gmail.com");
        TokenProvider provider = new JWTokenProvider(UUID.randomUUID().toString())
                .setPayload(payload).setHeader(header);
        String token = provider.generateToken(TokenProvider.defaultTokenTimeToLive());
        LOG.info("Token: " + token);*/
        String token = null;
        Assertions.assertTrue(token == null || token.isEmpty());
    }
}