package com.drawandyou.drawandyou_server.image.application.service.enums;

import java.util.Set;
public class AllowedMimeType {

    private AllowedMimeType(){
    }

    public static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/png"
    );

}
