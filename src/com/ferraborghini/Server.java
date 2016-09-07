package com.ferraborghini;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class Server {

	public void getScreen() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		try {
			BufferedImage screenshot = (new Robot())
					.createScreenCapture(new Rectangle(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight()));
			File file = new File("screen.jpg");
			ImageIO.write(screenshot, "jpg", file);
		} catch (AWTException | IOException e) {
			System.out.println("AWT error");
			e.printStackTrace();
		}
	}

	public void parseString(InputStream is){
		int length = 0;
		byte[] byteArray = new byte[1024];
		try {
			while ((length = is.read(byteArray, 0, byteArray.length)) > 0) {
				System.out.println(new String(byteArray));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void startServer() {
		try {
			int length = 0;
			ServerSocket server = new ServerSocket(9999);
			Socket socket = null;
			OutputStream os = null;
			while (true) {
				socket = server.accept();
				
				InputStream is = socket.getInputStream();
				byte[] byteArray = new byte[1024];
				while ((length = is.read(byteArray, 0, byteArray.length)) > 0) {
//					System.out.println(new String(byteArray));
//					System.out.println("text".equals(new String(byteArray).toString()));
					//因为数组中有空的数据所以需要trim做处理
					if ("text".equals(new String(byteArray).trim().toString())) {
						System.out.println("文本数据");
						os = socket.getOutputStream();
						os.write(new String("OK").getBytes());
						parseString(is);
					}
					}
			}
		} catch (IOException e) {
			System.out.println("Server failed");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Server().startServer();
	}

}
