package com.qq.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.qq.bean.Qquser;

public class RecordFrame extends JFrame{
	private Qquser receiver = null;
	
	private Label userLabel = null;
	private JTextArea textArea = null;
	private JScrollPane scrollPane = null;
	
	
	private void init() {
		JPanel bodyPanel = (JPanel)this.getContentPane();
		bodyPanel.setLayout(new BorderLayout());
		
		userLabel = new Label("与" + this.receiver.getName() + "的聊天记录");
		this.textArea = new JTextArea();
		this.scrollPane = new JScrollPane(this.textArea);
		this.scrollPane.setPreferredSize(new Dimension(400, 600));
		this.scrollPane.setViewportView(this.textArea);
		
		bodyPanel.add(this.userLabel,BorderLayout.NORTH);
		bodyPanel.add(this.scrollPane,BorderLayout.CENTER);
		
		this.setTitle("聊天记录");
		this.setSize(500, 800);
		this.setLocationRelativeTo(null);
	}
	
	
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}



	public RecordFrame() {}
	public RecordFrame(Qquser receiver) {
		this.receiver = receiver;
		this.init();
	}
}
