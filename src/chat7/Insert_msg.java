package chat7;

import java.sql.Date;

public class Insert_msg extends IConnectImpl{
	
	String name;
	String chat;
	String time;

	public Insert_msg(String name, String chat) {
		super("kosmo","1234");
		this.name = name;
		this.chat = chat;
	}
	@Override
	public void execute() { 
		try {
			String sql = "INSERT INTO chating_tb "
					+ " VALUES(seq_chat.nextval,?,?,to_char(sysdate,'HH:MI'))";
			psmt = con.prepareStatement(sql);
			
			psmt.setString(1, name);
			psmt.setString(2, chat);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected+"개가 저장되었습니다.");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close();
		}
	}
}
