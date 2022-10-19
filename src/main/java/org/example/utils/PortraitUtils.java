package org.example.utils;

import lombok.experimental.UtilityClass;
import org.example.exception.NotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;


@UtilityClass
public class PortraitUtils {

    public static void saveImage(String path, String login, byte[] image) throws IOException {
        Path output = Paths.get(String.format("/%s/%s.png", path, login));
        Files.createDirectories(Paths.get(path));
        if(Files.exists(output)) {
            Files.delete(output);
        }
        Files.createFile(output);
        try(FileOutputStream stream = new FileOutputStream(output.toFile())) {
            stream.write(image);
        }

    }

    public static byte[] getImage(String path, String login) throws IOException, NotFoundException {
        try {
            Path input = Paths.get(String.format("/%s/%s.png", path, login));
            return Files.readAllBytes(input);
        } catch (NoSuchFileException ex) {
            throw new NotFoundException("file not exist");
        }
    }

    public static Boolean isPresent(String path, String login) {
        Path file = Paths.get(String.format("/%s/%s.png", path, login));
        return Files.exists(file);
    }

    public static void delete(String path, String login) throws IOException {
        Path file = Paths.get(String.format("/%s/%s.png", path, login));
        Files.deleteIfExists(file);
    }
}
