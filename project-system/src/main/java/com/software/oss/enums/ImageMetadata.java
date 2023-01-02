package com.software.oss.enums;

import lombok.Getter;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:45
 */
@Getter
public enum ImageMetadata {

    /**
     * GPS 版本
     */
    GPSVersionID("GPS Version ID", "GPS 版本"),

    /**
     * 南北纬
     */
    GPSLatitudeRef("GPS Latitude Ref", "南北纬"),

    /**
     * 纬度
     */
    GPSLatitude("GPS Latitude", "纬度"),

    /**
     * 东西经
     */
    GPSLongitudeRef("GPS Longitude Ref", "东西经"),

    /**
     * 经度
     */
    GPSLongitude("GPS Longitude", "经度"),

    /**
     * 海拔参照值
     */
    GPSAltitudeRef("GPS Altitude Ref", "海拔参照值"),

    /**
     * 海拔
     */
    GPSAltitude("GPS Altitude", "海拔"),

    /**
     * GPS 时间戳
     */
    GPSTimeStamp("GPS Time-Stamp", "GPS 时间戳"),

    /**
     * 测量的卫星
     */
    GPSSatellites("GPS Satellites", "测量的卫星"),

    /**
     * 接收器状态
     */
    GPSStatus("GPS Status", "接收器状态"),

    /**
     * 测量模式
     */
    GPSMeasureMode("GPS Measure Mode", "测量模式"),

    /**
     * 测量精度
     */
    GPSDOP("GPS DOP", "测量精度"),

    /**
     * 速度单位
     */
    GPSSpeedRef("GPS Speed Ref", "速度单位"),

    /**
     * GPS 接收器速度
     */
    GPSSpeed("GPS Speed", "GPS 接收器速度"),

    /**
     * 移动方位参照
     */
    GPSTrackRef("GPS Track Ref", "移动方位参照"),

    /**
     * 移动方位
     */
    GPSTrack("GPS Track", "移动方位"),

    /**
     * 图像方位参照
     */
    GPSImgDirectionRef("GPS Img Direction Ref", "图像方位参照"),

    /**
     * 图像方位
     */
    GPSImgDirection("GPS Img Direction", "图像方位"),

    /**
     * 地理测量资料
     */
    GPSMapDatum("GPS Map Datum", "地理测量资料"),

    /**
     * 目标纬度参照
     */
    GPSDestLatitudeRef("GPS DestLatitude Ref", "目标纬度参照"),

    /**
     * 目标纬度
     */
    GPSDestLatitude("GPS Dest Latitude", "目标纬度"),

    /**
     * 目标经度参照
     */
    GPSDestLongitudeRef("GPS Dest Longitude Ref", "目标经度参照"),

    /**
     * 目标经度
     */
    GPSDestLongitude("GPS Dest Longitude", "目标经度"),

    /**
     * 目标方位参照
     */
    GPSDestBearingRef("GPS Dest Bearing Ref", "目标方位参照"),

    /**
     * 目标方位
     */
    GPSDestBearing("GPS Dest Bearing", "目标方位"),

    /**
     * 目标距离参照
     */
    GPSDestDistanceRef("GPS Dest DistanceRef", "目标距离参照"),

    /**
     * 目标距离
     */
    GPSDestDistance("GPS Dest Distance", "目标距离"),

    /**
     * GPS 处理方法名
     */
    GPSProcessingMethod("GPS Processing Method", "GPS 处理方法名"),

    /**
     * GPS 区功能变数名
     */
    GPSAreaInformation("GPS AreaInformation", "GPS 区功能变数名"),

    /**
     * GPS 日期
     */
    GPSDateStamp("GPS Date Stamp", "GPS 日期"),

    /**
     * GPS 修正
     */
    GPSDifferential("GPS Differential", "GPS 修正");

    private String code;

    private String displayName;

    ImageMetadata(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * 枚举转化
     */
    public static ImageMetadata parseOf(String value) {
        for (ImageMetadata item : values()) {
            if (item.getCode().equals(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("状态[" + value + "]不匹配!");
    }
}
