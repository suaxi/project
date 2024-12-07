package com.software.oss.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.software.oss.enums.ImageMetadata;
import com.software.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:45
 */
@Slf4j
public class ImageUtil {

    /**
     * 遮罩层高度
     */
    private static final int COVER_HEIGHT = 25;

    /**
     * 压缩率
     */
    private static final float IMAGE_RATIO = 0.9f;

    /**
     * 压缩最大宽度
     */
    private static final int IMAGE_WIDTH = 800;

    /**
     * 水印透明度
     */
    private static final float ALPHA = .3f;

    /**
     * 水印文字字体
     */
    private static final Font FONT = new Font("Microsoft Yahei", Font.PLAIN, 12);

    /**
     * 水印文字颜色
     */
    private final static Color COLOR = Color.WHITE;

    /**
     * 压缩图像
     *
     * @param image
     * @return
     * @throws IOException
     */
    public static BufferedImage compress(BufferedImage image) throws IOException {
        Assert.notNull(image, "图片信息不能为空");
        Thumbnails.Builder<BufferedImage> imageBuilder = Thumbnails.of(image).outputQuality(IMAGE_RATIO);
        if (image.getWidth() > IMAGE_WIDTH) {
            return imageBuilder.width(IMAGE_WIDTH).asBufferedImage();
        }
        return imageBuilder.scale(1).asBufferedImage();
    }

    public static Map<String, String> readPicInfo(final File file) {
        log.info("开始读取图片信息...");

        Map<String, String> result = new HashMap<>(16);
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);

            for (Directory exif : metadata.getDirectories()) {
                for (Tag tag : exif.getTags()) {
                    try {
                        ImageMetadata imageMetadata = ImageMetadata.parseOf(tag.getTagName());
                        log.info(imageMetadata.getDisplayName() + ": \t" + tag.getDescription());
                        result.put(imageMetadata.getCode(), tag.getDescription());
                    } catch (IllegalArgumentException e) {

                    }
                }
            }
        } catch (ImageProcessingException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 图像添加水印
     *
     * @param
     * @return
     */
    public static BufferedImage setWatermark(BufferedImage image) throws IOException {
        //BufferedImage mask = createMask(image.getWidth(), image.getHeight());
        BufferedImage watermark = createWatermark(new DateUtils().formatToDateTime(), image.getWidth(), image.getHeight());
        return Thumbnails.of(image)
                .outputQuality(IMAGE_RATIO)
                .scale(1)
                //.watermark(Positions.BOTTOM_CENTER, mask, .5f)
                .watermark(Positions.BOTTOM_RIGHT, watermark, 0.8f)
                .asBufferedImage();
    }


    public static BufferedImage setWatermark(BufferedImage image, Map<String, String> metadata) throws IOException {
        //BufferedImage mask = createMask(metadata);
        BufferedImage watermark = createWatermark(metadata, image.getWidth(), image.getHeight());
        return Thumbnails.of(image)
                .outputQuality(IMAGE_RATIO)
                .scale(1)
                //.watermark(Positions.BOTTOM_RIGHT, mask, .5f)
                .watermark(Positions.BOTTOM_RIGHT, watermark, 0.8f)
                .asBufferedImage();
    }

    /**
     * 根据文件扩展名判断文件是否图片格式
     *
     * @return
     */
    public static boolean isImage(String fileName) {
        for (String e : new String[]{"jpeg", "jpg", "gif", "bmp", "png", "webp", "tif", "tiff"}) {
            if (getFileExtention(fileName).toLowerCase().equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件后缀名称
     *
     * @param fileName
     * @return
     */
    public static String getFileExtention(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 根据图片对象获取对应InputStream
     *
     * @param image
     * @param readImageFormat
     * @return
     * @throws IOException
     */
    public static ByteArrayInputStream getInputStream(BufferedImage image, String readImageFormat) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, readImageFormat, os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return is;
    }

    /**
     * 遮罩层
     *
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createMask(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 创建黑色遮罩层
        BufferedImage maskImage = new BufferedImage(image.getWidth(), COVER_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D maskGraphics2D = (Graphics2D) maskImage.getGraphics();
        maskGraphics2D.setColor(Color.BLACK);
        maskGraphics2D.fillRect(0, 0, image.getWidth(), COVER_HEIGHT);
        maskGraphics2D.dispose();
        return maskImage;
    }

    public static BufferedImage createMask(Map<String, String> metadata) {
        int markWidth = 190;
        int markHeight = 0;
        if (metadata == null || metadata.size() == 0) {
            markHeight = 90;
        } else {
            markHeight = metadata.size() * 10;
        }
        // 创建黑色遮罩层
        BufferedImage maskImage = new BufferedImage(markWidth, markHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D maskGraphics2D = (Graphics2D) maskImage.getGraphics();
        maskGraphics2D.setColor(Color.BLACK);
        maskGraphics2D.fillRect(0, 0, markWidth, markHeight);
        maskGraphics2D.dispose();
        return maskImage;
    }

    /**
     * 创建水印图片
     *
     * @param text   水印文字
     * @param width  图片宽
     * @param height 图片高
     * @return
     */
    public static BufferedImage createWatermark(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 1.获取图片画笔
        Graphics2D graphics2D = image.createGraphics();
        // 2.背景透明
        image = graphics2D.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        graphics2D.dispose();
        graphics2D = image.createGraphics();
        // 3.处理文字
        //AttributedString ats = new AttributedString(text);
        //ats.addAttribute(TextAttribute.FONT, font, 0, text.length());
        //AttributedCharacterIterator iter = ats.getIterator();
        // 4.设置对线段的锯齿状边缘处理
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 5.设置水印旋转
        graphics2D.rotate(Math.toRadians(-30));
        // 6.设置水印文字颜色
        graphics2D.setColor(COLOR);
        // 7.设置水印文字Font
        graphics2D.setFont(FONT);
        // 8.设置水印文字透明度
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        graphics2D.drawString(text, (width - 300) / 2, height - 5);
        // 9.释放资源
        graphics2D.dispose();
        return image;
    }


    public static BufferedImage createWatermark(Map<String, String> metadata, int width, int height) {
        int markWidth = width - 180;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 1.获取图片画笔
        Graphics2D graphics2D = image.createGraphics();
        // 2.背景透明
        image = graphics2D.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        graphics2D.dispose();
        graphics2D = image.createGraphics();
        // 3.设置对线段的锯齿状边缘处理
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 4.设置水印旋转
        graphics2D.rotate(Math.toRadians(-30));
        // 5.设置水印文字颜色
        graphics2D.setColor(COLOR);
        // 6.设置水印文字Font
        graphics2D.setFont(FONT);
        // 7.设置水印文字透明度
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        // 8.经/纬度、海拔
        String longitudeRef = metadata.get(ImageMetadata.GPSLatitudeRef.getCode());
        longitudeRef = longitudeRef != null ? longitudeRef : "";
        String longitude = metadata.get(ImageMetadata.GPSLatitude.getCode());
        longitude = longitude != null ? longitude : "";
        graphics2D.drawString("纬度：       " + longitudeRef + longitude, markWidth, height - 70);

        String latitudeRef = metadata.get(ImageMetadata.GPSLongitudeRef.getCode());
        latitudeRef = latitudeRef != null ? latitudeRef : "";
        String latitude = metadata.get(ImageMetadata.GPSLongitude.getCode());
        latitude = latitude != null ? latitude : "";
        graphics2D.drawString("经度：       " + latitudeRef + latitude, markWidth, height - 50);

        String altitude = metadata.get(ImageMetadata.GPSAltitude.getCode());
        graphics2D.drawString("海拔：       " + (altitude != null ? altitude.replaceAll("metres", "米") : ""), markWidth, height - 30);

        String dateStep = metadata.get(ImageMetadata.GPSDateStamp.getCode());
        dateStep = dateStep != null ? dateStep.replaceAll(":", "-") : "";

        String timeStep = metadata.get(ImageMetadata.GPSTimeStamp.getCode());
        timeStep = timeStep != null ? timeStep.substring(0, 8) : "";
        // 9.拍摄时间
        graphics2D.drawString("拍摄时间：" + dateStep + " " + timeStep, markWidth, height - 10);
        // 10.释放资源
        graphics2D.dispose();
        return image;
    }

    /**
     * 计算字体宽度及高度
     *
     * @param text 文本内容
     * @param font 字体信息
     * @return
     */
    private static int[] getWidthAndHeight(String text, Font font) {
        Rectangle2D r = font.getStringBounds(text, new FontRenderContext(
                AffineTransform.getScaleInstance(1, 1), false, false));
        int unitHeight = (int) Math.floor(r.getHeight());
        // 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        // 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        int height = unitHeight + 3;
        return new int[]{width, height};
    }

}
