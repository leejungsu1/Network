package chat7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IConnectImpl implements IConnect{
	
	public Connection con;
	public PreparedStatement psmt;
	public Statement stmt;
	public ResultSet rs;
	
	public IConnectImpl() {
	}
	
	public IConnectImpl(String user, String pass) {
		try {
			Class.forName(ORACLE_DRIVER);
			connect(user, pass);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void connect(String user, String pass) {
		try {
			con = DriverManager.getConnection(ORACLE_URL, user, pass);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void execute() throws Exception {
	}
	@Override
	public void close() {
		try {
			if(con!=null) con.close();
			if(psmt!=null) psmt.close();
			if(stmt!=null) stmt.close();
			if(rs!=null) rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
