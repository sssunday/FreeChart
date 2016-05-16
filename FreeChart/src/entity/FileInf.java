package entity;

/**
 * 文件信息类
 * 提供文件信息的get()和set()方法以及toString()方法
 * @author Snail
 *
 */
public class FileInf {
	
	private int fid;			//文件ID
	private String fname;		//文件名
	private String createtime;	//上传时间
	private int uid;			//上传者ID
	
	/**
	 * 重写toString方法
	 */
	public String toString(){
		return fid+" "+fname+" "+createtime+" "+uid;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
}
