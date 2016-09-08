package com.ferraborghini.v1;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class FrameClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int HEIGHT = 0;
	private int WIDTH = 0;	
	private JTextPane welcome = null;
	private Dimension dimension = null;
	
	
	
	
	
	public FrameClient() {
		this.setTitle("Server");
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		HEIGHT = (int) dimension.getHeight()/2;
		WIDTH = (int) dimension.getWidth()/2;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((int) dimension.getWidth()/4, (int) dimension.getHeight()/4);
		this.setJMenuBar(this.getMenu());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e){  
                System.exit(0);  
            }  
		});
		welcome = new JTextPane();
		Container contentPane = this.getContentPane();
		contentPane.setSize(this.getWidth(), this.getHeight());
		welcome.setSize(contentPane.getWidth(), contentPane.getHeight());
		contentPane.setLayout(new FlowLayout());
		contentPane.add(welcome);
		contentPane.setSize(this.getWidth(), this.getHeight());
		welcome.setEditable(false);
        welcome.setText("\n\n    welcome\n by ferraborghini");
//        this.add(welcome);
        
	}
	
	
	public class ShowScreenShot extends Thread{
		private BufferedImage screenshot = null;
		
		public ShowScreenShot(BufferedImage screenshot) {
			screenshot = this.screenshot;
		}
		
		public void run() {
			super.run();
			showScreenShot();
		}
		public void showScreenShot(){
			System.out.println("show");
			if (screenshot != null) {
				getContentPane().setLayout(new BorderLayout(1, 1));
				FrameClient.this.setLocation(0,0);
		        JLabel bgLb = new JLabel(new ImageIcon(screenshot));
		        getContentPane().add(bgLb, BorderLayout.CENTER);
		        pack();
			}
			
		}
	}
	
	public void showScreenShot(BufferedImage screenshot){
		if (screenshot == null) {
			System.out.println("获取失败");
		}
		if (screenshot != null) {
			getContentPane().setLayout(new BorderLayout(1, 1));
			this.setLocation(0,0);
	        JLabel bgLb = new JLabel(new ImageIcon(screenshot));
	        getContentPane().add(bgLb, BorderLayout.CENTER);
	        pack();
		}
		
	}
	
	public JMenuBar getMenu() {
		JMenuBar menu = new JMenuBar();
		JMenu function = new JMenu("功能");
		JMenuItem capture = new JMenuItem("捕获屏幕");
		function.add(capture);
		menu.add(function);
		JMenu help = new JMenu("帮助");
		JMenuItem aboutMe = new JMenuItem("关于我");
		help.add(aboutMe);
		menu.add(help);
		
		aboutMe.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				welcome.setText("hello world");
				welcome.validate();
			}
		});
		
		
		
		capture.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
//				FileOutputStream fis = null;
//				String string = "hello world";
//				String type = "text";
//				os.write(type.getBytes());
				OutputStream os = null;
				Socket socket = null;
				InputStream is = null;
				ByteArrayOutputStream baos = null;      //使用baos缓冲所有socket的数据
				ByteArrayInputStream bais = null;		//再讲baos转换成bios写入BufferedImage中
				int length = 0;
				try {
					socket = new Socket("127.0.0.1", 9999);
					socket.setSoTimeout(2000);
					os = socket.getOutputStream();
//					
					is = socket.getInputStream();
					if (is== null) {
						System.out.println("null");
					}
//					fis = new FileOutputStream("screenshot.jpg");
//					fos = new FileOutputStream(new File("./cc.jpg"));
//	                inputByte = new byte[1024];
					byte[] data = new byte[1024];
					
	                System.out.println("开始接收数据...");
	                int len_image = 0;
	                while (true) {
	                	baos = new ByteArrayOutputStream();
	                	try {
		                	while ((length = is.read(data, 0, data.length)) > 0) {
			                	len_image+=length;
			                    System.out.println(length);
			                    baos.write(data);
			                    baos.flush();
//			                    fis.write(data, 0, length);
//			                    fis.flush();
//			                    if (length < 1000) {
//									break;
//								}
			                }
						} catch (SocketTimeoutException e2) {
							
						}
		                
		                System.out.println("完成接收: "+len_image);
//						byte[] datas = new byte[is.read()];
//						is.read(datas); 
//		                BufferedImage image = ImageIO.read(new File("screenshot.jpg"));
		                bais = new ByteArrayInputStream(baos.toByteArray());  
				        BufferedImage image = ImageIO.read(bais);
				        showScreenShot(image);
//				        new ShowScreenShot(image).start();
					}
	                
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
//					try {
//						os.close();
//						is.close();
////		                fis.flush();
////		                fis.close();
//					} catch (IOException e2) {
//						e2.printStackTrace();
//					}

				}
				
			}
		});
		return menu; 
	}
	

	
	public static void main(String[] args) {
		FrameClient client = new FrameClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		client.setVisible(true);
	}
	
}
