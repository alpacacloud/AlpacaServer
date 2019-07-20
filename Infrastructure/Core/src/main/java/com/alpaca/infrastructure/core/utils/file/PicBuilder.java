package com.alpaca.infrastructure.core.utils.file;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图像合成
 *
 * @author Lichenw
 * Created on 2019/6/15
 */
public class PicBuilder {

    Graphics2D graphics = null;
    BufferedImage baseMapImage = null;
    int baseMapHeight = 0, baseMapWidth = 0;

    public void init(File baseMapFile) throws IOException {

        baseMapImage = ImageIO.read(baseMapFile);
        baseMapHeight = baseMapImage.getHeight();
        baseMapWidth = baseMapImage.getWidth();
        graphics = baseMapImage.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1));

    }

    public void appendImage(File appendImg) throws IOException {
        BufferedImage appendImage = ImageIO.read(appendImg);
        graphics.drawImage(appendImage, 0, 0, baseMapWidth, baseMapHeight, null);
    }

    public void build(File buildFile) throws IOException {
        graphics.dispose();

        int tmp = buildFile.getName().lastIndexOf(".") + 1;
        String ext = buildFile.getName().substring(tmp);

        ImageIO.write(baseMapImage, ext, buildFile);
    }


    /**
     * 生成压缩图片
     * @param buildFile
     * @param width
     * @param height
     * @throws IOException
     */
    public void build(File buildFile, double width, double height) throws IOException {
        graphics.dispose();

        int tmp = buildFile.getName().lastIndexOf(".") + 1;
        String ext = buildFile.getName().substring(tmp);

        double sx = width / baseMapWidth;
        double sy = height / baseMapHeight;
        AffineTransformOp affineTransformOp = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), null);
        BufferedImage bufferedImage = affineTransformOp.filter(baseMapImage, null);
        ImageIO.write(bufferedImage, ext, buildFile);

    }

    public static void main(String[] args) {
        try {
            PicBuilder builder = new PicBuilder();
            builder.init(new File("D:\\lichenw\\picbuild\\1.png"));
            builder.appendImage(new File("D:\\lichenw\\picbuild\\3.png"));
            builder.appendImage(new File("D:\\lichenw\\picbuild\\2.png"));

            builder.build(new File("D:\\lichenw\\picbuild\\build.png"), 80, 150);
            int i = 0;
        } catch (Exception e) {
            int i = 0;
        }
    }
}
