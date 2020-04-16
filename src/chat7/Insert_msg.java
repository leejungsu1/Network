package chat7;

public class Insert_msg extends IConnectImpl{
	
	String name;
	String you;
	String chat;
	String time;

//	public Insert_msg(String name, String you, String chat) {
//		super("kosmo","1234");
//		this.name = name;
//		this.you = you;
//		this.chat = chat;
//	}
	public Insert_msg(String name, String chat) {
		super("kosmo","1234");
		this.name = name;
		this.chat = chat;
	}
	public Insert_msg(String name) {
		super("kosmo","1234");
		this.name = name;
	}
	@Override
	public void execute() throws Exception{ 
		try {
//			if(you!=null) {
//				String sql = "INSERT INTO chating_tb "
//						+ "VALUES(seq_chat.nextval, concat(?,*,?),?,to_char(sysdate,'HH:MI'))";
//				psmt = con.prepareStatement(sql);
//				
//				psmt.setString(1, name);
//				psmt.setString(2, you);
//				psmt.setString(3, chat);
//			}
			if(chat!=null) {
				String sql = "INSERT INTO chating_tb VALUES(seq_chat.nextval,?,?,to_char(sysdate,'HH:MI'))";
				psmt = con.prepareStatement(sql);
				
				psmt.setString(1, name);
				psmt.setString(2, chat);
			}
			else {
				String query = "INSERT INTO chat_id_tb VALUES(?, '0')";
				psmt = con.prepareStatement(query);
				psmt.setString(1, name);
			}
			int affected = psmt.executeUpdate();
			System.out.println(affected+"개가 저장되었습니다.");
		}
		finally {
			close();
		}
	}
}
