package com.myfutech.common.util.http;

import okhttp3.MediaType;

public enum MediaTypes {
    /**
     * APPLICATION_JSON
     */
    APPLICATION_JSON(MediaType.get("application/json")),
    /**
     * APPLICATION_JSON_UTF8
     */
    APPLICATION_JSON_UTF8(MediaType.get("application/json;charset=UTF-8")),
    /**
     * APPLICATION_XML
     */
    APPLICATION_XML(MediaType.get("application/xml")),
    /**
     * APPLICATION_XML_UTF8
     */
    APPLICATION_XML_UTF8(MediaType.get("application/xml;charset=UTF-8"));

    private MediaType mediaType;

    MediaTypes(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
