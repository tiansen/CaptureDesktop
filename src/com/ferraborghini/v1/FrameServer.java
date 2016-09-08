package com.ferraborghini.v1;
/**
 * 服务端，进行图片截取的一端
 */
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class FrameServer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int HEIGHT = 0;
	private int WIDTH = 0;
	private JTextPane welcome = null;
	private String IP = "";
	private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

	public FrameServer() {
		this.setTitle("Server");
		HEIGHT = (int) dimension.getHeight() / 4;
		WIDTH = (int) dimension.getWidth() / 4;
		this.setSize(WIDTH, HEIGHT);
		this.setLocation((int) dimension.getWidth() / 4,
				(int) dimension.getHeight() / 4);
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

	public void parseString(InputStream is) {
		int length = 0;
		byte[] byteArray = new byte[1024];
		try {
			while ((length = is.read(byteArray, 0, byteArray.length)) > 0) {
				System.out.println(new String(byteArray).trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showConnect() {
		JOptionPane.showConfirmDialog(this, "是否允许" + IP + "的接入？", "提示",
				JOptionPane.YES_NO_OPTION);

	}

	public void startTCPServer() {
		try {
			int length = 0;
			ServerSocket server = new ServerSocket(9999);
			Socket socket = null;
			OutputStream os = null;
			while (true) {
				socket = server.accept();
				IP = socket.getInetAddress().getHostAddress();
				System.out.println(IP + "已接入");
				showConnect();

				os = socket.getOutputStream();
				// 循环发送截图
				while (true) {
					try {
						sendImage(os);
						Thread.sleep(3000);
					} catch (SocketException e) {
						break;
					}
				}

			}
		} catch (IOException e) {
			System.out.println("Server failed");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 传输获取到的图像信息
	 */
	public void sendImage(OutputStream os) throws SocketException {
		BufferedImage image = getScreenShot();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", baos);
			baos = new ByteArrayOutputStream();
			byte[] datas = baos.toByteArray();
			baos.write(Utils.intToBytes(datas.length)); // 将数据长度写入头2014个字节
			baos.write(datas);
			System.out.println(baos.toByteArray().length);
			os.write(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取屏幕截图
	 * 
	 * @param args
	 */
	public BufferedImage getScreenShot() {
		BufferedImage screenshot = null;
		try {
			screenshot = (new Robot()).createScreenCapture(new Rectangle(0, 0,
					(int) dimension.getWidth(), (int) dimension.getHeight()));
			return screenshot;
		} catch (AWTException e) {
			e.printStackTrace();
			return screenshot;
		}

	}

	public static void main(String[] args) {
		FrameServer server = new FrameServer();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.setVisible(true);
		server.startTCPServer();
	}

}
