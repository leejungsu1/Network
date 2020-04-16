package chat7;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Scanner;


public class Sender extends Thread {
	Socket socket;
	PrintWriter out = null;
	String name;
	

	public Sender(Socket socket, String name) {
		this.socket = socket;
		try {
			out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(),"UTF-8"), true);
			this.name = name;
		}
		catch (Exception e) {
			System.out.println("예외>Sender>생성자:"+e);
		}
	}
	public void run() {
		
		Scanner s = new Scanner(System.in);
		
		try {
			out.println(name);
//			out.println(URLEncoder.encode(name, "UTF-8"));
			String s2;
			while(out!=null) {
				try {
					while(true) {
						s2 = s.nextLine();
						if(!(s2.equals(""))) {
							break;
						}
					}
					if(s2.equalsIgnoreCase("Q")) {
						break;
					}
					else {
						out.println(s2);
//						out.println(URLEncoder.encode(s2, "UTF-8"));
					}
				}
//				catch (UnsupportedEncodingException e) {
//					System.out.println(e.getMessage());
//				}
				catch (Exception e) {
					System.out.println("예외>Sender>run1:"+e);
				}
			}
			out.close();
			socket.close();
		}
//		catch (UnsupportedEncodingException e) {
//			System.out.println(e.getMessage());
//		}
		catch (Exception e) {
			System.out.println("예외>Sender>run2:"+e);
		}
	}
}
