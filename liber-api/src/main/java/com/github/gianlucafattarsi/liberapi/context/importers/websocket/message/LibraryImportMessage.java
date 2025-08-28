package com.github.gianlucafattarsi.liberapi.context.importers.websocket.message;

public record LibraryImportMessage(

        boolean error,

        String message,

        String username
) {
}
