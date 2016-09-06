package com.ferraborghini;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public void sendMessage(OutputStream os) {
		Scanner scanner = new Scanner(System.in);
		String word = null;
		try {
			while (true) {
				word = scanner.nextLine();
				if ("q".equals(word)) {
					break;
				}
				os.write(word.getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startClient() {
		OutputStream os = null;
		Socket socket = null;
		InputStream is = null;
		int length = 0;
		try {
			socket = new Socket("127.0.0.1", 9999);
			os = socket.getOutputStream();
			String string = "hello world";
			String type = "text";
			byte[] byteArray = new byte[1024];

			os.write(type.getBytes());
			is = socket.getInputStream();
			if ((length = is.read(byteArray, 0, byteArray.length)) > 0
					&& "OK".equals(new String(byteArray).trim().toString())) {
				System.out.println("start type in");
				sendMessage(os);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		new Client().startClient();

	}

}
