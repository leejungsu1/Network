package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiServer {

	static ServerSocket serverSocket = null;
	static Socket socket = null;
	// 클라이언트 정보 저장을 위한 Map컬렉션 정의
	Map<String, PrintWriter> clientMap;

	// 생성자
	public MultiServer() {
		// 클라이언트의 이름과 출력스트림을 저장할 HashMap생성
		clientMap = new HashMap<String, PrintWriter>();
		// HahMap동기화 설정. 쓰레드가 사용자정보에 동시에 접근하는 것을 차단한다.
		Collections.synchronizedMap(clientMap);
	}

	// 서버 초기화
	public void init() {
		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");

			while (true) {
				socket = serverSocket.accept();
				/*
				 * 클라이언트의 메세지를 모든 클라이언트에게 전달하기 위한 쓰레드 생성 및 start.
				 */
				Thread mst = new MultiServerT(socket);
				mst.start();
			}
		} catch (Exception e) {
			// System.out.println("예외1:"+ e);
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				// System.out.println("예외2:"+ e);
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init();
	}

	// 접속된 모든 클라이언트에게 메세지를 전달하는 역할의 메소드
	public void sendAllMsg(String name, String msg) {
		// Map에 저장된 객체의 키값(이름)을 먼저 얻어온다.
		Iterator<String> it = clientMap.keySet().iterator();

		// 저장된 객체(클라이언트)의 갯수만큼 반복한다.
		while (it.hasNext()) {
			try {
				// 각 클라이언트의 PrintWriter객체를 얻어온다.
				PrintWriter it_out = (PrintWriter) clientMap.get(it.next());
				// 클라이언트에게 메세지를 전달한다.
				/*
				 * 매개변수 name이 있는 경우에는 이름+메세지 없는경우에는 메세지만 클라이언트로 전송한다.
				 */
				if (name.equals("")) {
					it_out.println(msg);
				} else {
					it_out.println("[" + name + "]:" + msg);
				}
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}
	}

	public boolean wisper(String name, String you, String msg) {
		Iterator<String> it = clientMap.keySet().iterator();

		while (it.hasNext()) {
			String n = it.next();
			try {
				PrintWriter it_out = (PrintWriter) clientMap.get(n);
				if (you.equalsIgnoreCase(n)) {
					it_out.println("[" + name + "]:" + msg);
					return true;
				}
			} catch (Exception e) {
				System.out.println("예외:" + e);
			}
		}
		return false;
	}

// 내부클래스
class MultiServerT extends Thread {

	// 멤버변수
	Socket socket;
	PrintWriter out = null;
	BufferedReader in = null;

	// 생성자 : Socket을 기반으로 입출력 스트림을 생성한다.
	public MultiServerT(Socket socket) {
		this.socket = socket;
		try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
		} catch (Exception e) {
			System.out.println("예외:" + e);
		}
	}

	public void run() {
			
			//클라이언트로부터 전송된 "대화명"을 저장할 변수
			String name = "";
			//메세지 저장용 변수
			String s = "";
			
			try {
				//클라이언트의 이름을 읽어와서 저장
				while(true) {
					try {
						name = in.readLine();
//						name = URLDecoder.decode(name, "UTF-8");
						new Insert_msg(name).execute(); 
						break;
					}
					catch (Exception e) {
						out.println("접속이 불가합니다. 새로운 이름을 입력하세요.");
					}
				}
				
				//접속한 클라이언트에게 새로운 사용자의 입장을 알림.
				//접속자를 제외한 나머지 클라이언트만 입장메세지를 받는다.
				sendAllMsg("", name +"님이 입장하셨습니다.");
				
				//현재 접속한 클라이언트를 HashMap에 저장한다.
				clientMap.put(name, out);
				
				//HashMap에 저장된 객체의 수로 접속자수를 파악할수있다.
				System.out.println(name +" 접속");
				System.out.println("현재 접속자"+ clientMap.size() +"명 입니다.");
				
				//입력한 메세지는 모든 클라이언트에게 Echo된다.
				while(in != null) {
					s = in.readLine();
//					s = URLDecoder.decode(s,"UTF-8");
					if(s == null) {
						break;
					}
					if(s.charAt(0)=='/') {
						if(s.equalsIgnoreCase("/list")) {
							Iterator<String> it = clientMap.keySet().iterator();
							out.println("현재 접속자 ");
							while(it.hasNext()) {
								String list = it.next();
								out.println(list);
								System.out.println("현재 접속자 : "+ list);
							}
						}
						else if(s.substring(1, 3).equalsIgnoreCase("to")) {
							int index1 = s.indexOf(" ");
							int index2 = s.indexOf(" ", index1+ 1);
							if(index1==-1) {}
							else if(index1+1 == s.length()) {}
							else if(index2==-1) {
								String you = s.substring(index1+ 1);
								while(true) {
									out.println("메세지를 입력하세요.귓속말 종료(x)");
									String answer = in.readLine();
//									answer = URLDecoder.decode(answer, "UTF-8");
									if(answer.equalsIgnoreCase("x")) {
										out.println("귓속말이 종료되었습니다.");
										break;
									}
									else if(answer.equals("x")==false){
										if(wisper(name, you, answer)) {
											new Insert_msg(name +"*"+ you, answer).execute();
										}
										else {
											out.println("대상이 없습니다. 이름을 다시 입력하세요.");
											break;
										}
									}
								}
							}
							else {
								String you = s.substring(index1+ 1, index2);
								String talk = s.substring(index2+ 1);
								if(wisper(name, you, talk)) {
									new Insert_msg(name +"*"+ you, talk).execute();
								}
								else {
									out.println("대상이 없습니다. 이름을 다시 입력하세요.");
									break;
								}
							}
						}
						else if(s.substring(1, 6).equalsIgnoreCase("black")) {
							out.println("블랙리스트를 추가하려면 /in ");
							out.println("블랙리스트를 해제하려면 /out 뒤에 이름을 입력하세요");
							String answer = in.readLine();
							int index = answer.indexOf(" ");
							if(index==-1) {}
							else if(answer.substring(1, 3).equalsIgnoreCase("in")) {
								String black = answer.substring(index+1);
								new update_msg1(black).execute();
								out.println("추가 되었습니다.");
								Iterator<String> it = clientMap.keySet().iterator();
								while(it.hasNext()) {
									String n = it.next();
									try {
										PrintWriter it_out = (PrintWriter) clientMap.get(n);
										if (black.equalsIgnoreCase(n)) {
											it_out.println("채팅이 종료됩니다.");
											
										}
									} catch (Exception e) {
										System.out.println("예외:" + e);
									}
								}
							}
							else if(answer.substring(1, 4).equalsIgnoreCase("out")) {
//								int index = answer.indexOf(" ");
								String no_black = answer.substring(index+1);
								new update_msg2(no_black).execute();
								new delete_msg(no_black).execute();
								out.println("해제 되었습니다.");
							}
						}
					}
					else {
						System.out.println(name +" >> "+ s);
						new Insert_msg(name, s).execute();
						sendAllMsg(name, s);
					}
				}
			}
			catch (Exception e) {
//				System.out.println("예외:111"+ e);
				e.printStackTrace();
			}
			finally {
				/*
				클라이언트가 접속을 종료하면 예외가 발생하게 되어 finally로
				넘어오게 된다. 이때 "대화명"을 통해 remove()시켜준다.
				 */
				clientMap.remove(name);
				sendAllMsg("", name +"님이 퇴장하셨습니다.");
				new delete_msg(name).execute();
				//퇴장하는 클라이언트의 쓰레드명을 보여준다.
				System.out.println(name +"["+Thread.currentThread().getName() +"] 퇴장");
				System.out.println("현재 접속자 수는"+ clientMap.size() +"명 입니다.");
				try{
					in.close();
					out.close();
					socket.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
