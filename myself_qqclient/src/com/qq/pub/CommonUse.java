package com.qq.pub;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author jzyqd_
 */
public class CommonUse implements Serializable{
	
	public static final String FLAG="FLAG";//鏍囧織
	
	public static final String REGISTER="REGISTER";//娉ㄥ唽鏍囧織
	
	public static final String LOGIN="LOGIN";//鐧诲綍鏍囧織
	
	public static final String ONLINE="ONLINE";//涓婄嚎鏍囧織
	
	public static final String OFFLINE="OFFLINE";//涓嬬嚎鏍囧織
	
	public static final String FIND="FIND";//鏌ヨ鏍囧織
	
	public static final String QQ_USER="QQ_USER";//鏌ヨ鏍囧織
	
	public static final String SUCCESSFUL="SUCCESSFUL";//鎴愬姛鏍囧織
	
	public static final String FAILURE="FAILURE";//澶辫触鏍囧織
	
	public static final String FRIENDS_INFO="FRIENDS_INFO";//濂藉弸淇℃伅
	
	public static final String SERVER_IP = "127.0.0.1";//鏈嶅姟鍣↖P
	
	public static final int SERVER_PORT = 9999;//鏈嶅姟鍣ㄧ鍙ｅ彿
	
	public static final String FIND_FRIEND="FIND_FRIEND";//鏌ヨ鏍囧織
	
    public static final String UDP_PACKET_SYMBOL = "\n\r\n\r"; //鍒嗗壊绗﹀彿
	
	public static final String MESSAGE = "MESSAGE";
	
	public static final String Record = "Record";
	
	public static final String Chated = "Chated";
	
	public static final String Object_SRM = "Object_SRM";

    /**
     * 鎻愮ず淇℃伅
     */
    public static void showMessage(JComponent j, String message, String title, int i) {
        JOptionPane.showMessageDialog(j, message, title, i);
    }

    /**
     * 鑾峰彇绯荤粺锟�?锟斤拷鐨勮矾锟�?
     */
    public static String getSystempath() {
        File f = new File("");
        return f.getAbsolutePath();
    }

    /**
     * 鑾峰彇绯荤粺鏃ユ湡鏃堕棿
     */
    public static String getSystemTime() {
        //java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("HH:mm:ss");
        String time = f.format(new java.util.Date());
        return time;
    }
    
    /**
     * 璁剧疆绐椾綋灞呬腑
     * @param frame 琚缃殑绐椾綋
     * @param width 琚缃獥浣撳锟�?
     * @param high 琚缃獥浣撻珮锟�?
     */
    public static void setComponentBounts(JFrame frame, int width, int high) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setLocation((screenWidth - width) / 2, (screenHeight - high) / 2);
    }
}
