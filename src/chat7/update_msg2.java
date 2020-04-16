package chat7;

import java.sql.SQLException;

public class update_msg2 extends IConnectImpl{
	String no_black;
	
	public update_msg2(String no_black) {
		super("kosmo","1234");
		this.no_black = no_black;
	}
	@Override
	public void execute(){
		try {
			String query = "UPDATE chat_id_tb SET black='0' WHERE name=?";
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, no_black);
			
			int affected = psmt.executeUpdate();
			System.out.println(affected+"행이 업데이트 되었습니다.");
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close();
		}
	}
}
