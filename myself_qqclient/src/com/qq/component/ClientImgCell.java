/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qq.component;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.qq.bean.Qquser;

//实现章子接口，拥有盖章子功能
public class ClientImgCell extends JLabel implements ListCellRenderer {

    private ImageIcon img;
    //给哪个list盖章子，addElement里面放进来的value(即qquser),正在操作的Jlist里的第几个，这一项能否被选中，这个东西能否获得焦点
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Qquser obj = (Qquser)value;
        //取得图片路径
        this.img = new ImageIcon(obj.getPlace1());
        //设置背景和前景色
        if (isSelected) {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        } else {
        	this.setBackground(list.getBackground());
        	this.setForeground(list.getForeground());
        }
        //设置图片文字
        this.setText(obj.getName());
        this.setIcon(this.img);
        //设置一下你能不能用,label和list保持一致
        this.setEnabled(list.isEnabled());
        //设置跟list的字体保持一致
        this.setFont(list.getFont());
        //设置透明
        this.setOpaque(true);
        return this;
    }
} 