package com.software.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Wang Hao
 * @date 2023/1/1 21:11
 */
@Getter
@AllArgsConstructor
public enum ContentType {

    HTML("HTML", "text/html"),
    TXT("TXT", "text/plain"),
    XML("XML", "application/xml"),
    XHTML("XHTML", "application/xhtml+xml"),
    JSON("JSON", "application/json"),
    OCTET_STREAM("OCTET_STREAM", "application/octet-stream"),
    X_WWW_FORM_URLENCODED("X_WWW_FORM_URLENCODED", "application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("MULTIPART_FORM_DATA", "multipart/form-data"),
    // IMAGE
    GIF("GIF", "image/gif"),
    JFIF("JFIF", "image/jpeg"),
    JPE("JPE", "image/jpeg"),
    JPEG("JPEG", "image/jpeg"),
    JPG("JPG", "image/jpeg"),
    PNG("PNG", "image/png"),
    DWG("DWG", "image/vnd.dwg"),
    // MS-OFFICE
    DOC("DOC", "application/msword"),
    DOT("DOT", "application/msword"),
    DOCX("DOCX", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    DOTX("DOTX", "application/vnd.openxmlformats-officedocument.wordprocessingml.template"),
    DOCM("DOCM", "application/vnd.ms-word.document.macroEnabled.12"),
    DOTM("DOTM", "application/vnd.ms-word.template.macroEnabled.12"),
    XLS("XLS", "application/vnd.ms-excel"),
    XLT("XLT", "application/vnd.ms-excel"),
    XLA("XLA", "application/vnd.ms-excel"),
    XLSX("XLSX", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLTX("XLTX", "application/vnd.openxmlformats-officedocument.spreadsheetml.template"),
    XLSM("XLSM", "application/vnd.ms-excel.sheet.macroEnabled.12"),
    XLTM("XLTM", "application/vnd.ms-excel.template.macroEnabled.12"),
    XLAM("XLAM", "application/vnd.ms-excel.addin.macroEnabled.12"),
    XLSB("XLSB", "application/vnd.ms-excel.sheet.binary.macroEnabled.12"),
    PPT("PPT", "application/vnd.ms-powerpoint"),
    POT("POT", "application/vnd.ms-powerpoint"),
    PPS("PPS", "application/vnd.ms-powerpoint"),
    PPA("PPA", "application/vnd.ms-powerpoint"),
    PPTX("PPTX", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    POTX("POTX", "application/vnd.openxmlformats-officedocument.presentationml.template"),
    PPSX("PPSX", "application/vnd.openxmlformats-officedocument.presentationml.slideshow"),
    PPAM("PPAM", "application/vnd.ms-powerpoint.addin.macroEnabled.12"),
    PPTM("PPTM", "application/vnd.ms-powerpoint.presentation.macroEnabled.12"),
    POTM("POTM", "application/vnd.ms-powerpoint.presentation.macroEnabled.12"),
    PPSM("PPSM", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12"),
    PDF("PDF", "application/pdf"),
    ZIP("ZIP", "application/zip"),
    MP4("MP4", "video/mp4");

    private String suffix;

    private String mimeType;

    /**
     * 枚举转化
     */
    public static ContentType parseOf(String code) {
        for (ContentType item : values()) {
            if (item.getSuffix().equalsIgnoreCase(code)) {
                return item;
            }
        }
        throw new IllegalArgumentException("代号[" + code + "]不匹配!");
    }
}
