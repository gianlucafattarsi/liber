package com.github.gianlucafattarsi.liberapi.context.library.utils;

import com.github.gianlucafattarsi.liberapi.context.library.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
public class CoverUtils {

    private static final int THUMB_WITDH = 200;

    public static String fetchCover(Book book) {
        if (book == null) {
            return null;
        }

        final Path bookFullPath = Paths.get(book.getLibrary()
                                                .getPath())
                                       .resolve(book.getPath())
                                       .resolve("cover.jpg");

        String encodedCover;
        try {
            byte[] byteArray = Files.readAllBytes(bookFullPath);
            encodedCover = Base64.getEncoder()
                                 .encodeToString(byteArray);
        } catch (Exception e) {
            encodedCover = null;
        }

        return encodedCover;
    }

    public static String fetchThumbnail(Book book) {
        if (book == null) {
            return null;
        }

        final Path bookFullPath = Paths.get(book.getLibrary()
                                                .getPath())
                                       .resolve(book.getPath())
                                       .resolve("cover.jpg");

        String encodedCover;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            final BufferedImage bufferedImage = ImageIO.read(bookFullPath.toFile());
            final BufferedImage resizedImage = Scalr.resize(bufferedImage,
                    Scalr.Method.QUALITY,
                    Scalr.Mode.FIT_TO_WIDTH,
                    THUMB_WITDH,
                    600);

            ImageIO.write(resizedImage, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();

            encodedCover = Base64.getEncoder()
                                 .encodeToString(imageBytes);
        } catch (Exception e) {
            encodedCover = null;
        }

        return encodedCover;
    }
}
