package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.DBUtil;
import entity.FileInf;

/**
 * 提供文件信息查询、修改、增加、删除的方法
 * 1.查询所有文件信息
 * 2.根据上传者"uid"查询文件信息
 * 3.根据文件名fname查询文件信息
 * 4.增加文件
 * 5.删除文件
 * @author Snail
 *
 */
public class FileDAO {
	/**
	 * 查询所有文件信息
	 * @return
	 * @throws Exception
	 */
	public List<FileInf> getAll() throws Exception{
		List<FileInf> files = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			files = new ArrayList<FileInf>();
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("SELECT * FROM fileinfo");
			rs = ps.executeQuery();
			while(rs.next()){
				FileInf file = new FileInf();
				file.setFid(rs.getInt("fid"));
				file.setFname(rs.getString("fname"));
				file.setCreatetime(rs.getString("createtime"));
				file.setUid(rs.getInt("uid"));
				files.add(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return files;
	}
	
	/**
	 * 根据上传者"uid"查询文件信息
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public List<FileInf> getByUid(int uid) throws Exception{
		List<FileInf> files = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			files = new ArrayList<FileInf>();
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("SELECT * FROM fileinfo WHERE uid=?");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			while(rs.next()){
				FileInf file = new FileInf();
				file.setFid(rs.getInt("fid"));
				file.setFname(rs.getString("fname"));
				file.setCreatetime(rs.getString("createtime"));
				file.setUid(rs.getInt("uid"));
				files.add(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return files;
	}
	
	/**
	 * 根据文件名fname查询文件信息
	 * @param fname
	 * @return
	 * @throws Exception
	 */
	public FileInf getByFname(String fname) throws Exception{
		FileInf file = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("SELECT * FROM fileinfo WHERE fname=?");
			ps.setString(1, fname);
			rs = ps.executeQuery();
			if(rs.next()){
				file = new FileInf();
				file.setFid(rs.getInt("fid"));
				file.setFname(rs.getString("fname"));
				file.setCreatetime(rs.getString("createtime"));
				file.setUid(rs.getInt("uid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			DBUtil.close(conn);
		}
		return file;
	}
	
	/**
	 * 增加文件
	 * @param fname
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public boolean addFile(String fname,int uid) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//获取系统时间作为文件存储时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
			String dateStr = sdf.format(new Date());
			
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("INSERT INTO fileinfo (fname,createtime,uid) VALUES (?,?,?)");
			ps.setString(1, fname);
			ps.setString(2, dateStr);
			ps.setInt(3, uid);
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
	 * 删除文件
	 * @param fid
	 * @return
	 * @throws Exception
	 */
	public boolean removeFile(String fname) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConnection();
			ps = conn.prepareStatement("DELETE FROM fileinfo WHERE fname=?");
			ps.setString(1, fname);
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
