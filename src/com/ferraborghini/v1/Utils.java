package com.ferraborghini.v1;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.awt.image.BytePackedRaster;

public class Utils {
	
	/**
	 * int到byte[]
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] intToBytes(int value)   
	{   
	    byte[] src = new byte[1024];  
	    src[1020] = (byte) ((value>>24) & 0xFF);  
	    src[1021] = (byte) ((value>>16) & 0xFF);  
	    src[1022] = (byte) ((value>>8) & 0xFF);    
	    src[1023] = (byte) (value & 0xFF);       
	    return src;  
	}  

	/**
	 * byte[]转int
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byteArrayToInt(byte[] bytes) {
		int value;  
		int offset = 1020;
	    value = (int) ( ((bytes[offset] & 0xFF)<<24)  
	            |((bytes[offset+1] & 0xFF)<<16)  
	            |((bytes[offset+2] & 0xFF)<<8)  
	            |(bytes[offset+3] & 0xFF));  
	    return value;
	}
	
	/**
	 * 将图片转化为byte数组
	 * @param image
	 * @return
	 */
	public static byte[] BufferedImageToByte(BufferedImage image){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", baos);
		} catch (IOException e) {
			System.out.println("ByteArrayOutputStream 写入失败");
			e.printStackTrace();
		}
		byte[] datas = baos.toByteArray();
		System.out.println("数组长度："+datas.length);
		return datas;
	}
	/**
	 * 将byte数组转化为image
	 * @param image
	 * @return
	 */
	public static BufferedImage ByteToBufferedImage(byte[] datas){
		ByteArrayInputStream bais = new ByteArrayInputStream(datas);
		BufferedImage image = null;
		try {
			image = ImageIO.read(bais);
		} catch (IOException e) {
			System.out.println("转化为图片失败");
			e.printStackTrace();
		}
		return image;
	}
}
