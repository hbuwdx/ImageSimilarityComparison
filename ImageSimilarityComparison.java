package com.jd.oa.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by wangdongxing on 14-2-13.
 * Email : wangdongxing@jd.com
 */
public class ImageSimilarityComparison {
	/**
	 * 对比相似度
	 * @param srcImagePath  源图片
	 * @param targetImagePath  目标图片
	 * @return 相似度值
	 */
	public static  int comparison(String srcImagePath,String targetImagePath) throws  Exception{
		int scaleSize = 8; //缩放大小
		int similarity = 0;

		String srcTempPath="D:\\src_temp.jpg";
		String targetTempPath="D:\\target_temp.jpg";

		//进行缩放处理
		scaleImage(srcImagePath,srcTempPath,scaleSize,scaleSize);
		scaleImage(targetImagePath, targetTempPath, scaleSize, scaleSize);
		BufferedImage srcImage = ImageIO.read(new File(srcTempPath));
		BufferedImage targetImage = ImageIO.read(new File(targetTempPath));

		//进行灰度处理
		grayImage(srcImage);
		grayImage(targetImage);

		//提取图片特征指纹
		String srcCode = imageCode(srcImage);
		String targetCode = imageCode(targetImage);
		//计算汉明距离
		System.out.println(srcCode);
		System.out.println(targetCode);
		similarity = hanming(srcCode,targetCode);
		return similarity;
	}

	/**
	 * 计算汉明距离
	 * @param str1 字串1
	 * @param str2 字串2
	 * @return
	 */
	public static  int hanming(String str1,String str2){
		int distance = 0;
		for(int i=0;i<str1.length();i++){
				if(str1.charAt(i)  != str2.charAt(i)){
					distance++;
				}
		}
		return distance;
	}
	/**
	 * 计算图片的识别指纹用于对比
	 * @param bufferedImage 需要计算的图片
	 * @return 图片的指纹
	 */
	public static  String  imageCode(BufferedImage bufferedImage){
		int width = bufferedImage.getWidth(null);
		int height = bufferedImage.getHeight(null);

		int[][] pixels=new int[width][height];
		int total = 0;
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				pixels[i][j] = bufferedImage.getRGB(i,j) & 0xFFFFFF;
				total += pixels[i][j];
			}
		}

		int avg = total / (width * height);
		StringBuilder stringBuilder=new StringBuilder();
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				if(pixels[i][j] < avg){
					stringBuilder.append("0");
				}else{
					stringBuilder.append("1");
				}
			}
		}

		return stringBuilder.toString();
	}
	/**
	 * 对图片进行灰度处理
	 * @param bufferedImage 需要处理的图片
	 */
	public static void grayImage(BufferedImage bufferedImage){
		ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		colorConvert.filter(bufferedImage,bufferedImage);
	}
	/**
	 * 缩放图片到新位置
	 * @param srcImagePath 源地址
	 * @param outImagePath 输入地址
	 * @param width 缩放宽度
	 * @param height 缩放高度
	 * @throws Exception
	 */
	public static void  scaleImage(String srcImagePath,String outImagePath,int width,int height) throws  Exception{
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.drawImage( ImageIO.read(new File(srcImagePath)),0,0,width,height,null);
		g.dispose();
		ImageIO.write(bi, "JPG", new FileOutputStream(outImagePath));
	}

	public static  void main(String[] args){
		String srcImage = "d:\\src.jpg";
		String targetImage = "d:\\target.jpg";
		int similarityNum;
		try {
			similarityNum = ImageSimilarityComparison.comparison(srcImage, targetImage);
			System.out.println(similarityNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
