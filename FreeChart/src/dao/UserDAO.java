package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil;
import entity.User;

/**
 * 提供用户信息查询、修改、增加的方法
 * 1.查询所有用户的信息
 * 2.根据uname查询用户信息
 * 3.根据uid查询用户信息
 * 4.修改用户密码
 * 5.增加新用户
 * @author Snail
 *
 */
public class UserDAO {
	/**
	 * 查询所有用户信息
	 * @return
	 * @throws Exception
	 */
	public List<User> getAll() throws Exception{
		List<User> users = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			users = new ArrayList<User>();
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("SELECT * FROM userinfo");
			rs = ps.executeQuery();
			while(rs.next()){
				User user = new User();
				user.setUid(rs.getInt("uid"));
				user.setUname(rs.getString("uname"));
				user.setPassword(rs.getString("password"));
				user.setAccess(rs.getInt("access"));
				users.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return users;
	}
	
	/**
	 * 根据uname查询用户信息
	 * @param uname
	 * @return
	 * @throws Exception
	 */
	public User getByUname(String uname) throws Exception{
		User user = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("SELECT * FROM userinfo WHERE uname=?");
			ps.setString(1, uname);
			rs = ps.executeQuery();
			if(rs.next()){
				user = new User();
				user.setUid(rs.getInt("uid"));
				user.setUname(rs.getString("uname"));
				user.setPassword(rs.getString("password"));
				user.setAccess(rs.getInt("access"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return user;
	}
	
	/**
	 * 根据uid查询用户信息
	 * @param uname
	 * @return
	 * @throws Exception
	 */
	public User getByUid(int uid) throws Exception{
		User user = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			user = new User();
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("SELECT * FROM userinfo WHERE uid=?");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			if(rs.next()){
				user.setUid(rs.getInt("uid"));
				user.setUname(rs.getString("uname"));
				user.setPassword(rs.getString("password"));
				user.setAccess(rs.getInt("access"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return user;
	}
	
	/**
	 * 修改用户密码
	 * @param uid
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean modifyPasswd(int uid,String password) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("UPDATE userinfo SET password=? WHERE uid=?");
			ps.setString(1, password);
			ps.setInt(2, uid);
			if(ps.executeUpdate() > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return false;
	}
	
	/**
	 * 增加新用户
	 * @param uname
	 * @param passwrod
	 * @return
	 * @throws Exception
	 */
	public boolean addUser(String uname,String passwrod) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement(	"INSERT INTO userinfo (uname,password) VALUES (?,?)");
			ps.setString(1, uname);
			ps.setString(2, passwrod);
			if(ps.executeUpdate() > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return false;
	}
	
}
