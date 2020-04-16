package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class Receiver extends Thread{

	Socket socket;
	BufferedReader in = null;
	
	public Receiver(Socket socket) {
		this.socket = socket;
		
		
		try {
			in = new BufferedReader
					(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));
		}
		catch (Exception e) {
			System.out.println("예외>Receiver>생성자:"+e);
		}
	}
	
	public void run() {
		while(in!=null) {
			try {
				String n = in.readLine();
				System.out.println(n);
				if(n.equals("채팅이 종료됩니다.")) {
					break;
				}
			}
			catch (SocketException e) {
//				System.out.println("SocketException발생됨");
				break;
			}
			catch (Exception e) {
				System.out.println("예외>Receiver>run1:"+e);
			}
		}
		try {
			in.close();
		}
		catch (Exception e) {
			System.out.println("예외>Receiver>run2:"+e);
		}
	}
	
}
