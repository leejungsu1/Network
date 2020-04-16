package chat7;

import java.sql.SQLException;

public class update_msg1 extends IConnectImpl{
	String black;
	
	public update_msg1(String black) {
		super("kosmo","1234");
		this.black = black;
	}
	@Override
	public void execute(){
		try {
			String query = "UPDATE chat_id_tb SET black='black' where name=?";
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, black);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected+"행이 업데이트 되었습니다.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}
}
