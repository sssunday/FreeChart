package web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.FileDAO;
import dao.UserDAO;
import entity.FileInf;
import entity.User;

/**
 * 
 * @author Snail
 *
 */
public class ActionServlet extends HttpServlet {
	/**
	 * ���ļ����û���Ϣ���������棬���ڲ�ѯ��������ѯ�ٶ�
	 */
	private static List<FileInf> allFileList = null;
	private static Runnable refreshFile = null;
	private static String UPLOAD_PATH = "";
	
	public void init() throws ServletException {
		super.init();
		UPLOAD_PATH = this.getInitParameter("UPLOAD_PATH");
		refreshFile = new RefreshFile();
		Thread thread = new Thread(refreshFile);
		thread.start();
	}

	/**
	 * Service������ʵ��
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		String uri = request.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/"),uri.lastIndexOf("."));
		
		/**
		 * ��¼
		 */
		if("/login".equals(action)){
			PrintWriter out = response.getWriter();
			try {
				String username = (String) request.getParameter("username");
				String password = (String) request.getParameter("password");
				UserDAO userDao = new UserDAO();
				User user = userDao.getByUname(username);
				if(user.getPassword().equals(password)){
					HttpSession session = request.getSession();
					session.setMaxInactiveInterval(24*60*60);
					session.setAttribute("sessionID", session.getId());
					session.setAttribute("access", user.getAccess()+"");
					session.setAttribute("uid", user.getUid());
					session.setAttribute("Path", UPLOAD_PATH);
					out.println("1");
					Thread thread = new Thread(refreshFile);
					thread.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * �޸�����
		 */
		if("/modifypwd".equals(action)){
			PrintWriter out = response.getWriter();
			try {
				String username = (String) request.getParameter("username");
				String password = (String) request.getParameter("password");
				UserDAO userDao = new UserDAO();
				User user = userDao.getByUname(username);
				if(user != null){
					userDao.modifyPasswd(user.getUid(), password);
					out.println("1");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * ���û����Ƿ��ظ�
		 */
		if("/hasname".equals(action)){
			PrintWriter out = response.getWriter();
			try {
				String username = (String) request.getParameter("username");
				UserDAO userDAO = new UserDAO();
				User user = userDAO.getByUname(username);
				if(user == null){
					out.println("1");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * �����û�
		 */
		if("/adduser".equals(action)){
			PrintWriter out = response.getWriter();
			try {
				String username = (String) request.getParameter("username");
				String password = (String) request.getParameter("password");
				UserDAO userDao = new UserDAO();
				if(userDao.addUser(username, password)){
					out.println("1");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * �˳�
		 */
		if("/logout".equals(action)){
			try {
				HttpSession session = request.getSession();
				session.invalidate();
				response.sendRedirect("login.jsp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * �ϴ��ļ�
		 */
		if("/upload".equals(action)){
			//Session��֤
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID == null || sessionID.equalsIgnoreCase(session.getId()) != true){
				response.sendRedirect("login.jsp");
			}
			DiskFileItemFactory factory = new DiskFileItemFactory();//����������
			String uploadDir = UPLOAD_PATH;//�ļ�ʵ�ʱ���·��
			ServletFileUpload upload = new ServletFileUpload(factory);//����������
			//���ý��ȼ���
			Progress progress = new Progress(session);
			upload.setProgressListener(progress);
			try {
				List<FileItem> items = upload.parseRequest(request);
				for(FileItem item : items){
					if(!item.isFormField()){
						//��ȡ�ļ���
						String filename = item.getName();
						filename = filename.substring(filename.lastIndexOf("\\")+1);
						//��ѯ�Ƿ�����
						FileDAO fileDao = new FileDAO();
						if(fileDao.getByFname(filename)!=null){
							session.setAttribute("uploadState", "200");
							return;
						}
						//�����ݿ��в����ļ�
						int uid = (Integer) session.getAttribute("uid");
						fileDao.addFile(filename, uid);
						//��Ӳ����д���ļ�
						InputStream in = item.getInputStream();
						File file = new File(uploadDir,filename);
						if(!file.exists()){
							file.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(file);
						int len = -1;
						byte[] buffer = new byte[1024];
						while((len=in.read(buffer))!=-1){
							fos.write(buffer,0,len);
						}
						fos.close();
						in.close();
						item.delete();
						Thread thread = new Thread(refreshFile);
						thread.start();
					}
				}
			} catch (Exception e) {
				session.setAttribute("uploadState", "400");
				Thread thread = new Thread(refreshFile);
				thread.start();
				e.printStackTrace();
			}
		}
		
		/**
		 * �����ļ�
		 */
		if("/download".equals(action)){
			//Session��֤
			HttpSession session = request.getSession();
			String Path = (String)session.getAttribute("Path");
			if(Path=="" || Path==null){
				response.sendRedirect("login.jsp");
				return;
			}
			try {
				response.setCharacterEncoding("utf-8");
				String downpath = Path + request.getParameter("fname");
				File file = new File(downpath);
				InputStream in = new FileInputStream(file);
				OutputStream fos = response.getOutputStream();
				response.addHeader("Content-Disposition", "attachment;filename="+new String(file.getName().getBytes("gbk"),"iso-8859-1"));
				response.addHeader("Content-Length", file.length()+"");
				response.setContentType("application/octet-stream");
				int len = -1;
				byte[] buffer = new byte[1024];
				while((len=in.read(buffer))!=-1){
					fos.write(buffer,0,len);
				}
				fos.close();
				in.close();
			} catch (Exception e) {
				//�������ļ������쳣
			}
		}
		
		/**
		 * �ϴ�״̬����
		 */
		if("/uploadstate".equals(action)){
			PrintWriter out = response.getWriter();
			//Session��֤
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID.equalsIgnoreCase(session.getId())){
				String msg = (String) session.getAttribute("uploadState");
				if("100".equals(msg)){
					out.println("300");
					session.setAttribute("uploadState", "0");
				}else{
					out.println(msg);
				}
			}
		}
		
		/**
		 * ɾ���ļ�
		 */
		if("/removefile".equals(action)){
			PrintWriter out = response.getWriter();
			//Session��֤
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID.equalsIgnoreCase(session.getId())){
				String fname = request.getParameter("fname");
				FileDAO fileDao = new FileDAO();
				try {
					File file = new File(UPLOAD_PATH+fname);
					if(fileDao.removeFile(fname) && file.delete()){
						out.println(1);
						Thread thread = new Thread(refreshFile);
						thread.start();
					}
				} catch (Exception e) {
					out.println(0);
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * �г�Ҫ��ʾ���ļ�
		 */
		if("/showfile".equals(action)){
			PrintWriter out = response.getWriter();
			//Session��֤
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID.equalsIgnoreCase(session.getId())){
				String access = (String) session.getAttribute("access");
				String searchcode = request.getParameter("searchcode");
				List<FileInf> fileList = new ArrayList<FileInf>();
				if("".equals(searchcode)){
					fileList = allFileList;
				}else{
					fileList = new ArrayList<FileInf>();
					for(FileInf file : allFileList){
						if(file.getFname().indexOf(searchcode)!=-1 ||
								file.getCreatetime().indexOf(searchcode)!=-1){
							fileList.add(file);
						}
					}
				}
				String htmlCode = "";
				for(FileInf file : fileList){
					UserDAO userDao = new UserDAO();
					User user = new User();
					try {
						user = userDao.getByUid(file.getUid());
					} catch (Exception e) {
						out.print("404");
						e.printStackTrace();
					}
					if("2".equals(access)){
						htmlCode += ""+"<tr>"
							+"<td><div style=\"width: 400px;\" class=\"uk-text-truncate\">"+file.getFname()+"</div></td>"
							+"<td>"+user.getUname()+"</td>"
							+"<td>"+file.getCreatetime()+"</td>"
							+"<td>"
								+"<a class=\"uk-button uk-button-danger uk-button-mini\" onclick=\"do_delete('"+file.getFname()+"');\">ɾ��</a>"
								+"<span>&nbsp;&nbsp;&nbsp;</span>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">����</a>"
							+"</td>"
						+"</tr>";
					}else{
						htmlCode += ""+"<tr>"
								+"<td><div style=\"width: 400px;\" class=\"uk-text-truncate\">"+file.getFname()+"</div></td>"
								+"<td>"+user.getUname()+"</td>"
								+"<td>"+file.getCreatetime()+"</td>"
								+"<td>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">����</a>"
								+"</td>"
							+"</tr>";
					}
					
				}
				out.println(htmlCode);
			}
		}
		
		/**
		 * �г�Ҫ��ʾ���ļ�(С��Ļ)
		 */
		if("/showfilesmall".equals(action)){
			PrintWriter out = response.getWriter();
			//Session��֤
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID.equalsIgnoreCase(session.getId())){
				String access = (String) session.getAttribute("access");
				String htmlCode = "";
				for(FileInf file : allFileList){
					UserDAO userDao = new UserDAO();
					User user = new User();
					try {
						user = userDao.getByUid(file.getUid());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if("2".equals(access)){
						htmlCode += ""+"<tr>"
							+"<td><div style=\"width: 156px;\" class=\"uk-text-truncate\">"+file.getFname()+"</div></td>"
							+"<td>"
								+"<a class=\"uk-button uk-button-danger uk-button-mini\" onclick=\"do_delete('"+file.getFname()+"');\">ɾ��</a>"
								+"<span>&nbsp;&nbsp;&nbsp;</span>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">����</a>"
							+"</td>"
						+"</tr>";
					}else{
						htmlCode += ""+"<tr>"
								+"<td><div style=\"width: 156px;\" class=\"uk-text-truncate\">"+file.getFname()+"</div></td>"
								+"<td>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">����</a>"
								+"</td>"
							+"</tr>";
					}
					
				}
				out.println(htmlCode);
			}
		}
		
		/**
		 * �����û�Ȩ��
		 */
		if("/showview".equals(action)){
			PrintWriter out = response.getWriter();
			//Session��֤
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID.equalsIgnoreCase(session.getId())){
				String access = (String) session.getAttribute("access");
				out.println(access);
			}
		}
		
	}
	
	/**
	 * ˢ���ļ���Ϣ���߳���
	 * @author Snail
	 *
	 */
	private class RefreshFile implements Runnable{
		public synchronized void run() {
			FileDAO fileDao = new FileDAO();
			try {
				allFileList = fileDao.getAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ���ȼ�����
	 * @author Snail
	 *
	 */
	private class Progress implements ProgressListener{
		private HttpSession session;
		long num = 0;
		
		public Progress(HttpSession session) {
			this.session = session;
		}

		public void update(long bytesRead, long contentLength, int items) {
			long progress = bytesRead*100/contentLength;
			if(progress == num){
				return;
			}
			num = progress;
			session.setAttribute("uploadState", num+"");
		}
	}
}
