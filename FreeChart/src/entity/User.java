package entity;

/**
 * �û���Ϣ��
 * �ṩ�û���Ϣ��get()��set()�����Լ�toString()����
 * @author Snail
 *
 */
public class User {
	
	private int uid;			//�û�ID
	private String uname;		//�û���
	private String password;	//����
	private int access;			//Ȩ��
	
	/**
	 * ��дtoString����
	 */
	public String toString(){
		return uid+" "+uname+" "+password+" "+access;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAccess() {
		return access;
	}
	public void setAccess(int access) {
		this.access = access;
	}
	
}
