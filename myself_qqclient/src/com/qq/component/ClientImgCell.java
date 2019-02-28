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

//ʵ�����ӽӿڣ�ӵ�и����ӹ���
public class ClientImgCell extends JLabel implements ListCellRenderer {

    private ImageIcon img;
    //���ĸ�list�����ӣ�addElement����Ž�����value(��qquser),���ڲ�����Jlist��ĵڼ�������һ���ܷ�ѡ�У���������ܷ��ý���
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Qquser obj = (Qquser)value;
        //ȡ��ͼƬ·��
        this.img = new ImageIcon(obj.getPlace1());
        //���ñ�����ǰ��ɫ
        if (isSelected) {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        } else {
        	this.setBackground(list.getBackground());
        	this.setForeground(list.getForeground());
        }
        //����ͼƬ����
        this.setText(obj.getName());
        this.setIcon(this.img);
        //����һ�����ܲ�����,label��list����һ��
        this.setEnabled(list.isEnabled());
        //���ø�list�����屣��һ��
        this.setFont(list.getFont());
        //����͸��
        this.setOpaque(true);
        return this;
    }
} 