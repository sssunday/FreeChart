<%@page import="java.io.OutputStream"%><%@page import="java.io.FileInputStream"%><%@page import="java.io.InputStream"%><%@page import="java.io.File"%><%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%><%
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
				
	}
%>