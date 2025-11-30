package com.infoworks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VaadinApplicationTest {

    static Logger LOG = LoggerFactory.getLogger(VaadinApplicationTest.class);

    @Test
    public void test() {
        LOG.info("Hallo info");
        LOG.debug("Hallo debug");
        LOG.error("Hallo error");
        Assertions.assertTrue(true);
    }
}
