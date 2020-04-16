package chat7;

public interface IConnect {
	
	String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	String ORACLE_URL = "jdbc:oracle:thin://@localhost:1521:orcl";
	
	void connect(String user, String pass);
	void execute() throws Exception;
	void close();
}
