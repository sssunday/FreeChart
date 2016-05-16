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
 * �ṩ�ļ���Ϣ��ѯ���޸ġ����ӡ�ɾ���ķ���
 * 1.��ѯ�����ļ���Ϣ
 * 2.�����ϴ���"uid"��ѯ�ļ���Ϣ
 * 3.�����ļ���fname��ѯ�ļ���Ϣ
 * 4.�����ļ�
 * 5.ɾ���ļ�
 * @author Snail
 *
 */
public class FileDAO {
	/**
	 * ��ѯ�����ļ���Ϣ
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
	 * �����ϴ���"uid"��ѯ�ļ���Ϣ
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
	 * �����ļ���fname��ѯ�ļ���Ϣ
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
	 * �����ļ�
	 * @param fname
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public boolean addFile(String fname,int uid) throws Exception{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//��ȡϵͳʱ����Ϊ�ļ��洢ʱ��
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
	 * ɾ���ļ�
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
