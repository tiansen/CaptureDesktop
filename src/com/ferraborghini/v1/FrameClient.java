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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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

public class FrameClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int HEIGHT = 0;
	private int WIDTH = 0;	
	private JTextPane welcome = null;
	
	
	
	
	
	
	public FrameClient() {
		this.setTitle("Server");
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
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
	public void showScreenShot(BufferedImage screenshot){
		if (screenshot == null) {
			System.out.println("获取失败");
		}
		if (screenshot != null) {
			getContentPane().setLayout(new BorderLayout(1, 1));
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
			
			@Override
			public void actionPerformed(ActionEvent e) {
				welcome.setText("hello world");
				welcome.validate();
			}
		});
		
		
		
		capture.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				OutputStream os = null;
				Socket socket = null;
				InputStream is = null;
				int length = 0;
				try {
					socket = new Socket("127.0.0.1", 9999);
					os = socket.getOutputStream();
//					String string = "hello world";
//					String type = "text";
//					os.write(type.getBytes());
					is = socket.getInputStream();
					if (is== null) {
						System.out.println("null");
					}
					FileInputStream fis = new FileputStream("screenshot.jpg");
					byte[] data = new byte[1024];
//					fos = new FileOutputStream(new File("./cc.jpg"));
//	                inputByte = new byte[1024];
	                System.out.println("开始接收数据...");
	                while ((length = is.read(data, 0, data.length)) > 0) {
	                    System.out.println(length);
	                    fis.write(data, 0, length);
	                    fis.flush();
	                }
	                
	                System.out.println("完成接收");
//					byte[] datas = new byte[is.read()];
//					is.read(datas); new ByteArrayInputStream(datas);  
//			        BufferedImage image = ImageIO.read(bin);
//			        showScreenShot(image);
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						os.close();
						is.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}

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
