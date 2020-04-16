package chat7;

import java.sql.SQLException;

public class delete_msg extends IConnectImpl{
	String delete_name;
	public delete_msg(String delete_name) {
		super("kosmo","1234");
		this.delete_name = delete_name;
	}
	
	@Override
	public void execute(){
		try {
			String sql = "delete from chat_id_tb where name=? and black= '0'";
			psmt = con.prepareStatement(sql);
			
			psmt.setString(1, delete_name);
			
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
