package sxt.net10_04.chat04;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 使用多线程封装:发送端 1、发送消息 2、从控制台获取消息 3、释放资源 4、重写run
 * 
 * @author 裴新 QQ:3401997271
 *
 */
// **flow:
// **1. get from console
// **2. send by client
public class Send implements Runnable {
	private BufferedReader console;
	private DataOutputStream dos;
	private Socket client;
	private boolean isRunning;
	private String name;

	public Send(Socket client, String name) {
		this.client = client;
		console = new BufferedReader(new InputStreamReader(System.in));
		this.isRunning = true;
		this.name = name;
		try {
			dos = new DataOutputStream(client.getOutputStream());
			// 发送名称
			send(name);
		} catch (IOException e) {
			System.out.println("==1==");
			this.release();
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			String msg = getStrFromConsole();
			if (!msg.equals("")) {
				send(msg);
			}
		}
	}

	// 发送消息
	private void send(String msg) {
		try {
			dos.writeUTF(msg);
			dos.flush();
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("===3==");
			release();
		}
	}

	/**
	 * 从控制台获取消息
	 * 
	 * @return
	 */
	private String getStrFromConsole() {
		try {
			return console.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	// 释放资源
	private void release() {
		this.isRunning = false;
		SxtUtils.close(dos, client);
	}

}