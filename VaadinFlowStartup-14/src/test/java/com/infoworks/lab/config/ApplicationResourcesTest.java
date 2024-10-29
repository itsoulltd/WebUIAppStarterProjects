package com.infoworks.lab.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ApplicationResourcesTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void filenames() {
        ApplicationResources resources = new ApplicationResources();
        List<String> names = resources.filenames("Download", true);
        names.forEach(name-> System.out.println(name));
    }

    @Test
    public void filenamesV2() {
        ApplicationResources resources = new ApplicationResources();
        List<String> names = resources.filenames("Download", false);
        names.forEach(name-> System.out.println(name));
    }
}