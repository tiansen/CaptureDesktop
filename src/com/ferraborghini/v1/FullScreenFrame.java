package com.ferraborghini.v1;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FullScreenFrame extends JFrame implements KeyListener {
	private JLabel bgLb = new JLabel();
	
	
	public FullScreenFrame() {
		this.setTitle("屏幕捕获");
		this.setUndecorated(true);
		this.getContentPane().setLayout(new BorderLayout(1, 1));
		this.setLocation(0, 0);
		this.setVisible(true);
		this.requestFocus();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(this);		
		this.getContentPane().add(bgLb, BorderLayout.CENTER);
		this.pack();
	}

	public JLabel getBgLb() {
		return bgLb;
	}

	public void setBgLb(JLabel bgLb) {
		this.bgLb = bgLb;
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		
	}

	
}
