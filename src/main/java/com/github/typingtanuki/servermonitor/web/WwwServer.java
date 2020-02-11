package com.github.typingtanuki.servermonitor.web;

import org.apache.http.entity.ContentType;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class WwwServer {
    private static final Path ROOT_PATH = Paths.get("www").toAbsolutePath();

    public Response serve(String path) throws IOException {
        Path target = ROOT_PATH.resolve(path);
        if (Files.exists(target) && target.toAbsolutePath().startsWith(ROOT_PATH)) {
            Response.ResponseBuilder builder = Response.ok();
            builder.type(typeFromExtension(path.toLowerCase(Locale.ENGLISH)));
            builder.entity(Files.readString(target, StandardCharsets.UTF_8));
            return builder.build();
        } else {
            return Response.ok().status(HttpStatus.NOT_FOUND_404).build();
        }
    }

    private String typeFromExtension(String path) {
        String[] parts = path.split("\\.");
        String ext = parts[parts.length - 1];
        switch (ext) {
            case "html":
                return ContentType.TEXT_HTML.getMimeType();
            case "js":
                return ContentType.TEXT_PLAIN.getMimeType();
            case "css":
                return ContentType.TEXT_PLAIN.getMimeType();
        }
        return ContentType.TEXT_PLAIN.getMimeType();
    }
}
