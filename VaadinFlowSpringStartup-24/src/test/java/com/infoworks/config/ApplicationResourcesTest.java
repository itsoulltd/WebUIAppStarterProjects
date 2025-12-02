package com.infoworks.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationResourcesTest {

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

    @Test
    public void getPathTest() {
        ApplicationResources resources = new ApplicationResources();
        Path path = resources.getPath("Download");
        System.out.println(path.toAbsolutePath());
    }

}