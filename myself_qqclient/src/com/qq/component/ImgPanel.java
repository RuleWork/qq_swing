package com.qq.component;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.qq.pub.CommonUse;

public class ImgPanel extends JPanel {
	private ImageIcon img = null;
	
	public ImgPanel() {
		// TODO Auto-generated constructor stub
	}
	public ImgPanel(String imgPath) {
		//this.img = new ImageIcon(imgPath);
		this.img = new ImageIcon(CommonUse.getSystempath() + imgPath );
		//System.out.println(CommonUse.getSystempath() + "\\register.jpg");
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(this.img.getImage(), 0, 0, this);
	}
	
}
