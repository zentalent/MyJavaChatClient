package com.zen.myjavachatclient.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.zen.myjavaclient.view.MainWindow;

public class ChatManager {
	
	private ChatManager(){
		
	}
	private static final ChatManager instance = new ChatManager();
	public static ChatManager getCM(){
		return instance;
	}
	
	MainWindow Window;
	Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	
	public void setWindow(MainWindow window){
		this.Window = window;
		window.appendText("文本框已经和ChatManager绑定了");
	}
	
	public void connect(String ip){
		new Thread(){

			@Override
			public void run() {
				try {
					socket = new Socket(ip,12345);
					writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					String line;
					while((line=reader.readLine())!=null){
						Window.appendText("Receive:"+line);
					}
					writer.close();
					reader.close();
					writer=null;
					reader=null;
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
	}
	
	public void send(String out){
		if (writer != null){
			writer.write(out+"\n");
			writer.flush();
		}else {
			Window.appendText("Disconnected");
		}
	}
}
