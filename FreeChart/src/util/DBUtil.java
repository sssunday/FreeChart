package util;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 数据库连接类
 * 使用HttpServlet将连接参数传递给DBUtil
 * 提供获取数据库连接和关闭连接功能
 * 
 * @author Snail
 * 
 */
public class DBUtil extends HttpServlet{
	
	private static String DB_DRIVER = "";	//数据库驱动
	private static String DB_URL = "";		//数据库地址
	private static String DB_USER = "";		//数据库用户名
	private static String DB_PASSWD = "";	//数据库密码
	
	/**
	 * 使用HttpServlet将连接参数传递给DBUtil
	 */
	public void init() throws ServletException {
		super.init();
		DB_DRIVER = this.getInitParameter("DB_DRIVER");	//设置驱动
		DB_URL = this.getInitParameter("DB_URL");		//设置连接
		DB_USER = this.getInitParameter("DB_USER");		//设置用户名
		DB_PASSWD = this.getInitParameter("DB_PASSWD");	//设置密码
	}
	
	/**
	 * 提供数据库连接的方法
	 * 返回一个数据库的连接
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
	 * 关闭数据库连接
	 * 需要提供连接对象
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
