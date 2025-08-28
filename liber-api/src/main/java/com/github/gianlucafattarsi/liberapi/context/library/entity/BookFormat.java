package com.github.gianlucafattarsi.liberapi.context.library.entity;

public enum BookFormat {

    EPUB,
    PDF,
    MOBI,
    AZW3,
    FB2,
    TXT,
    HTML,
    RTF,
    DJVU,
    CBR,
    CBZ;

    public static BookFormat fromString(String value) {
        for (BookFormat bookFormat : BookFormat.values()) {
            if (bookFormat.name()
                          .equalsIgnoreCase(value)) {
                return bookFormat;
            }
        }
        return null;
    }
}
