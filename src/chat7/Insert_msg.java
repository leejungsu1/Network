package chat7;

import java.sql.Date;

public class Insert_msg extends IConnectImpl{
	
	String name;
	String chat;
	Date time;

	public Insert_msg(String name, String chat) {
		super("kosmo","1234");
		this.name = name;
		this.chat = chat;
	}
	@Override
	public void execute() { 
		try {
			String sql = "insert into chating_tb values(seq_chat.nextval,?,?,?)";
			psmt = con.prepareStatement(sql);
			
			psmt.setString(1, name);
			psmt.setString(2, chat);
			psmt.setDate(3, time);
			
			System.out.println("저장되었습니다.");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close();
		}
	}
}
