package util;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * ���ݿ�������
 * ʹ��HttpServlet�����Ӳ������ݸ�DBUtil
 * �ṩ��ȡ���ݿ����Ӻ͹ر����ӹ���
 * 
 * @author Snail
 * 
 */
public class DBUtil extends HttpServlet{
	
	private static String DB_DRIVER = "";	//���ݿ�����
	private static String DB_URL = "";		//���ݿ��ַ
	private static String DB_USER = "";		//���ݿ��û���
	private static String DB_PASSWD = "";	//���ݿ�����
	
	/**
	 * ʹ��HttpServlet�����Ӳ������ݸ�DBUtil
	 */
	public void init() throws ServletException {
		super.init();
		DB_DRIVER = this.getInitParameter("DB_DRIVER");	//��������
		DB_URL = this.getInitParameter("DB_URL");		//��������
		DB_USER = this.getInitParameter("DB_USER");		//�����û���
		DB_PASSWD = this.getInitParameter("DB_PASSWD");	//��������
	}
	
	/**
	 * �ṩ���ݿ����ӵķ���
	 * ����һ�����ݿ������
	 * @return
	 * @throws Exception 
	 */
	public static Connection getConnection() throws Exception{	
		Connection conn = null;
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return conn;
	}
	
	/**
	 * �ر����ݿ�����
	 * ��Ҫ�ṩ���Ӷ���
	 * @param conn
	 * @return
	 */
	public static void close(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
