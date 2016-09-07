package com.ferraborghini.v1;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Capture extends JFrame {
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	public Capture() {
		//初始化数据
		
		int FULL_HEIGHT = (int) dimension.getHeight();
		int FULL_WIDTH = (int) dimension.getWidth();
		this.setTitle("抓取桌面");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e){  
                System.exit(0);  
            }  
		});
		this.setLayout(null);
		this.setSize(FULL_WIDTH,FULL_HEIGHT);
		this.setLocation(0,0);
	}
	
	public void getBackgroudImage(){
		try {
			BufferedImage screenshot = (new Robot()).createScreenCapture(new Rectangle(0,  
			        0,(int)dimension.getWidth(),(int)dimension.getHeight()));
            getContentPane().setLayout(new BorderLayout(1, 1));
            JLabel bgLb = new JLabel(new ImageIcon(screenshot));
            getContentPane().add(bgLb, BorderLayout.CENTER);
            pack();
//            ImageIcon imageIcon = new ImageIcon("screen.jpg");
//            imageIcon.setImage(screenshot);
//            bgLb.setIcon(imageIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}  
	}
	
}
