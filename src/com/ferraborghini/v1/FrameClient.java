package com.ferraborghini.v1;
/**
 * 客户端也就是可以进行远程控制的一端
 * 
 * 
 */

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
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
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import com.sun.jdi.event.EventQueue;

public class FrameClient extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int HEIGHT = 0;
	private int WIDTH = 0;
	private JTextPane welcome = null;
	private Dimension dimension = null;
	FullScreenFrame jf = null;
	BufferedImage image = null;

	public FrameClient() {
		this.setTitle("Server");
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		HEIGHT = (int) dimension.getHeight() / 2;
		WIDTH = (int) dimension.getWidth() / 2;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((int) dimension.getWidth() / 4, (int) dimension.getHeight() / 4);
		this.setJMenuBar(this.getMenu());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
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
	}

	/**
	 * 使用多线程来处理显示图片
	 * 
	 * @author t81019503
	 *
	 */
	public class ShowScreenShot extends Thread {
		private BufferedImage screenshot = null;

		public ShowScreenShot(BufferedImage screenshot) {
			screenshot = this.screenshot;
		}

		public void run() {
			super.run();
			showScreenShot();
		}

		public void showScreenShot() {
			System.out.println("show");
			if (screenshot != null) {
				getContentPane().setLayout(new BorderLayout(1, 1));
				FrameClient.this.setLocation(0, 0);
				JLabel bgLb = new JLabel(new ImageIcon(screenshot));
				getContentPane().add(bgLb, BorderLayout.CENTER);
				pack();
				FrameClient.this.repaint();
			}

		}
	}

	public void showScreenShot(BufferedImage screenshot) {
		if (screenshot == null) {
			System.out.println("获取图片数据为空");
		} else {
			if (jf == null) {
				jf = new FullScreenFrame();
			}
			JLabel bgLb = new JLabel(new ImageIcon(screenshot));
			jf.getContentPane().removeAll();
			jf.getContentPane().add(bgLb, BorderLayout.CENTER);
			jf.pack();

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

		capture.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				java.awt.EventQueue.invokeLater(new Runnable() {
					
                    @Override
                    public void run() {
                    	 startSerializableSocket();
                    }
                });
				
			}
		});
		return menu;
	}

	/**
	 * 传统的传输方式，直接用byte数组获取
	 */
	public void startSocket() {
		OutputStream os = null;
		Socket socket = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null; // 使用baos缓冲所有socket的数据
		ByteArrayInputStream bais = null; // 再讲baos转换成bios写入BufferedImage中
		int length = 0;
		try {
			
			socket = new Socket("127.0.0.1", 9999);
			os = socket.getOutputStream();
			is = socket.getInputStream();
			byte[] data = new byte[1024];
			System.out.println("开始接收数据...");
			while (true) {
				int len_image = 0;
				int data_length = 0;
				baos = new ByteArrayOutputStream();
				boolean first_data = true;
				try {
					while ((length = is.read(data, 0, data.length)) > 0) {
						System.out.println("读入数据");
						if (first_data) {
							data_length = Utils.byteArrayToInt(data);
							first_data = false;
						} else {
							len_image += length;
							baos.write(data);
							baos.flush();
							if (Math.abs((len_image - data_length)) < 100) {
								first_data = true;
								break;
							}
						}
					}
				} catch (SocketTimeoutException e2) {

				}

				System.out.println("完成接收: " + len_image);
				bais = new ByteArrayInputStream(baos.toByteArray()); // socket获取的数据先用baos，再将其转换为bais，使用ImageIO写入BufferedImage
				BufferedImage image = ImageIO.read(bais);
				showScreenShot(image);
				// new ShowScreenShot(image).start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
		}
	}

	/**
	 * 使用序列化的传输方式，精简步骤，增强代码的重用能力
	 */
	public void startSerializableSocket() {
		//不能阻塞UI线程
		new Thread(new Runnable() {
			public void run() {
				try {
					DataBean db = new DataBean();
					OutputStream os = null;
					Socket socket = null;
					InputStream is = null;
					ObjectInputStream ois = null;
					socket = new Socket("127.0.0.1", 9999);
					os = socket.getOutputStream();
					is = socket.getInputStream();
					System.out.println("开始接收数据...");
					while (true) {
						ois = new ObjectInputStream(is);
						db = (DataBean) ois.readObject();
						if (DataBean.MESSAGE.equals(db.getDataType())) {
							System.out.println(db.getData());
						} else if (DataBean.IMAGE.equals(db.getDataType())) {
							System.out.println(((byte[]) db.getData()).length);
							image = Utils.ByteToBufferedImage((byte[]) db.getData());
						} else if (DataBean.POSITION.equals(db.getDataType())) {

						}
						System.out.println("完成接收");
						
						if (image != null) {
//							SwingUtilities.invokeLater(new Runnable() {       
//							       public void run() {
							    	   showScreenShot(image);
//							       }
//							      });      
//							java.awt.EventQueue.invokeLater(new Runnable() {
//		                        @Override
//		                        public void run() {
//		                        	
//		                        }
//		                    });
							
						} else {

						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		FrameClient client = new FrameClient();
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.setVisible(true);
	}

}
