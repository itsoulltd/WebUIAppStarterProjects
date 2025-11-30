package com.infoworks.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationResources {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationResources.class.getSimpleName());

    public Path getPath(String fileOrFolder) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            //Simulate: ../resources/fileOrFolder
            URL resource = classLoader.getResource(fileOrFolder);
            Path directory = Paths.get(resource.toURI());
            return directory;
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
        }
        //By Default /src/main/resources/<fileOrFolder>
        return Paths.get("src","main","resources",fileOrFolder);
    }

    public List<String> filenames(String folder, boolean fullWithExt) {
        try {
            Path directory = getPath(folder);
            if (!Files.isDirectory(directory))
                throw new Exception("folder: " + folder + " not a directory");
            //
            List<String> names = Files.list(directory)
                    .filter(path -> Files.isDirectory(path) == false)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> (fullWithExt)
                            ? folder + "/" + name
                            : name.substring(0, name.lastIndexOf(".")))
                    .collect(Collectors.toList());
            //names.forEach(name -> System.out.println(name));
            names.sort(Comparator.comparing(String::toString));
            return names;
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
        }
        return new ArrayList<>();
    }

    public static String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}
