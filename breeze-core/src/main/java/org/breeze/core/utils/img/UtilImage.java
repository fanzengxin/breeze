package org.breeze.core.utils.img;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @description: 图片工具类
 * @auther: 黑面阿呆
 * @date: 2020-03-17 10:43
 * @version: 1.0.0
 */
public class UtilImage {

    /**
     * 干扰线数量
     */
    private static final int CODE_LINE_NUMBER = 15;

    /**
     * 生成图片验证码
     *
     * @param width
     * @param height
     * @param word
     * @return
     * @throws IOException
     */
    public static String createImageWithVerifyCode(int width, int height, String word) throws IOException {
        Random random = new Random();
        // 创建绘制对象
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 得到画图对象
        Graphics graphics = bufferedImage.getGraphics();
        // 绘制图片前指定一个颜色
        graphics.setColor(new Color(180 + random.nextInt(20), 180 + random.nextInt(20), 180 + random.nextInt(20)));
        graphics.fillRect(0, 0, width, height);
        // 绘制边框
        graphics.setColor(Color.white);
        graphics.drawRect(0, 0, width - 1, height - 1);
        // 步骤四 四个随机数字
        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setFont(new Font("黑体", Font.BOLD, 52));
        // 定义x坐标
        int x = 30;
        for (int i = 0; i < word.length(); i++) {
            // 随机颜色
            graphics2d.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 旋转 -30 --- 30度
            int jiaodu = random.nextInt(60) - 30;
            // 换算弧度
            double theta = jiaodu * Math.PI / 180;
            // 获得字母数字
            char c = word.charAt(i);
            //将c 输出到图片
            graphics2d.rotate(theta, x, 50);
            graphics2d.drawString(String.valueOf(c), x, 50);
            graphics2d.rotate(-theta, x, 50);
            x += 40;
        }
        // 绘制干扰线
        graphics.setColor(new Color(80 + random.nextInt(20), 80 + random.nextInt(20), 80 + random.nextInt(20)));
        int x1, x2, y1, y2;
        for (int i = 0; i < CODE_LINE_NUMBER; i++) {
            x1 = random.nextInt(width / 2);
            x2 = random.nextInt(width / 2) + width / 2;
            y1 = random.nextInt(height);
            y2 = random.nextInt(height);
            graphics.drawLine(x1, y1, x2, y2);
        }
        // 释放资源
        graphics.dispose();
        // 创建流，生成base64编码
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] bytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");
        return png_base64;
    }

    /**
     * 生成随机颜色
     *
     * @param min
     * @param max
     * @return
     */
    private static Color getRandColor(int min, int max) {
        Random random = new Random();
        int value = random.nextInt(max - min) + min;
        return new Color(value);
    }
}
