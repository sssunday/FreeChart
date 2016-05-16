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
	 * 给文件和用户信息数据做缓存，便于查询，提升查询速度
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
	 * Service方法的实现
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		String uri = request.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/"),uri.lastIndexOf("."));
		
		/**
		 * 登录
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
		 * 修改密码
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
		 * 查用户名是否重复
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
		 * 增加用户
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
		 * 退出
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
		 * 上传文件
		 */
		if("/upload".equals(action)){
			//Session验证
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID == null || sessionID.equalsIgnoreCase(session.getId()) != true){
				response.sendRedirect("login.jsp");
			}
			DiskFileItemFactory factory = new DiskFileItemFactory();//创建处理工厂
			String uploadDir = UPLOAD_PATH;//文件实际保存路径
			ServletFileUpload upload = new ServletFileUpload(factory);//创建解析器
			//设置进度监听
			Progress progress = new Progress(session);
			upload.setProgressListener(progress);
			try {
				List<FileItem> items = upload.parseRequest(request);
				for(FileItem item : items){
					if(!item.isFormField()){
						//获取文件名
						String filename = item.getName();
						filename = filename.substring(filename.lastIndexOf("\\")+1);
						//查询是否重名
						FileDAO fileDao = new FileDAO();
						if(fileDao.getByFname(filename)!=null){
							session.setAttribute("uploadState", "200");
							return;
						}
						//向数据库中插入文件
						int uid = (Integer) session.getAttribute("uid");
						fileDao.addFile(filename, uid);
						//向硬盘中写入文件
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
		 * 下载文件
		 */
		if("/download".equals(action)){
			//Session验证
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
				//不处理文件下载异常
			}
		}
		
		/**
		 * 上传状态反馈
		 */
		if("/uploadstate".equals(action)){
			PrintWriter out = response.getWriter();
			//Session验证
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
		 * 删除文件
		 */
		if("/removefile".equals(action)){
			PrintWriter out = response.getWriter();
			//Session验证
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
		 * 列出要显示的文件
		 */
		if("/showfile".equals(action)){
			PrintWriter out = response.getWriter();
			//Session验证
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
								+"<a class=\"uk-button uk-button-danger uk-button-mini\" onclick=\"do_delete('"+file.getFname()+"');\">删除</a>"
								+"<span>&nbsp;&nbsp;&nbsp;</span>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">下载</a>"
							+"</td>"
						+"</tr>";
					}else{
						htmlCode += ""+"<tr>"
								+"<td><div style=\"width: 400px;\" class=\"uk-text-truncate\">"+file.getFname()+"</div></td>"
								+"<td>"+user.getUname()+"</td>"
								+"<td>"+file.getCreatetime()+"</td>"
								+"<td>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">下载</a>"
								+"</td>"
							+"</tr>";
					}
					
				}
				out.println(htmlCode);
			}
		}
		
		/**
		 * 列出要显示的文件(小屏幕)
		 */
		if("/showfilesmall".equals(action)){
			PrintWriter out = response.getWriter();
			//Session验证
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
								+"<a class=\"uk-button uk-button-danger uk-button-mini\" onclick=\"do_delete('"+file.getFname()+"');\">删除</a>"
								+"<span>&nbsp;&nbsp;&nbsp;</span>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">下载</a>"
							+"</td>"
						+"</tr>";
					}else{
						htmlCode += ""+"<tr>"
								+"<td><div style=\"width: 156px;\" class=\"uk-text-truncate\">"+file.getFname()+"</div></td>"
								+"<td>"
								+"<a class=\"uk-button uk-button-success uk-button-mini\" href=\"download.do?fname="+file.getFname()+"\">下载</a>"
								+"</td>"
							+"</tr>";
					}
					
				}
				out.println(htmlCode);
			}
		}
		
		/**
		 * 返回用户权限
		 */
		if("/showview".equals(action)){
			PrintWriter out = response.getWriter();
			//Session验证
			HttpSession session = request.getSession();
			String sessionID = (String)session.getAttribute("sessionID");
			if(sessionID.equalsIgnoreCase(session.getId())){
				String access = (String) session.getAttribute("access");
				out.println(access);
			}
		}
		
	}
	
	/**
	 * 刷新文件信息的线程类
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
	 * 进度监听器
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
