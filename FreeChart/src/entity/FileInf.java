package entity;

/**
 * �ļ���Ϣ��
 * �ṩ�ļ���Ϣ��get()��set()�����Լ�toString()����
 * @author Snail
 *
 */
public class FileInf {
	
	private int fid;			//�ļ�ID
	private String fname;		//�ļ���
	private String createtime;	//�ϴ�ʱ��
	private int uid;			//�ϴ���ID
	
	/**
	 * ��дtoString����
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
