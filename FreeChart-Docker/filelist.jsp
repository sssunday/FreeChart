<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	//添加session验证
	String sessionID = (String)session.getAttribute("sessionID");

	if(sessionID == null || sessionID.equalsIgnoreCase(session.getId()) != true){
		response.sendRedirect("login.jsp");
	}
 %>
<!DOCTYPE html>
<html lang="en" class="uk-height-1-1">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>FreeChart 文件共享</title>
	<link rel="stylesheet" type="text/css" href="./uikit/css/uikit.gradient.min.css">
	<link rel="stylesheet" type="text/css" href="./uikit/css/components/notify.gradient.min.css">
	<link rel="stylesheet" type="text/css" href="./uikit/css/components/progress.gradient.min.css">
	<link rel="stylesheet" type="text/css" href="./uikit/css/components/placeholder.gradient.min.css">
	<link rel="stylesheet" type="text/css" href="./uikit/css/components/form-file.gradient.min.css">
	<link rel="shortcut icon" href="./favicon.ico">
	<script type="text/javascript" src="./uikit/js/jquery.js"></script>
	<script type="text/javascript" src="./uikit/js/uikit.min.js"></script>
	<script type="text/javascript" src="./uikit/js/components/notify.min.js"></script>
	<script type="text/javascript" src="./uikit/js/my.js"></script>
	<script type="text/javascript">
		$(function(){
			do_showall();
			showview();
		});
	</script>
</head>
<body class="uk-height-1-1">

	<!-- 导航区 -->
	<div class="uk-navbar" data-uk-sticky="{clsactive:uk-navbar-attached}">
		<div class="uk-container uk-container-center">
			<ul class="uk-navbar-nav">
				<li class="uk-visible-large"><a href="https://coding.net/u/xtliuke/p/FileShare/git" target="_blank">关于</a></li>
			</ul>
			<a href="#offcanvas" data-uk-offcanvas class="uk-navbar-toggle uk-hidden-large"></a>
			<span class="uk-navbar-brand uk-navbar-center uk-hidden-large">文件夹</span>
			<div class="uk-navbar-content uk-navbar-flip uk-visible-large">
				<a class="uk-icon-user" href="logout.do"> 退出</a>
			</div>
			<div class="uk-navbar-content uk-navbar-flip uk-visible-large">
				<div class="uk-form uk-margin-remove uk-display-inline-block">
					<input id="searchcode" placeholder="文件名、创建时间" type="text">
					<a id="submit" class="uk-button uk-button-primary" onclick="do_search()">搜索</a>
					<a class="uk-button uk-button-success" onclick="do_showall()">所有文件</a>
				</div>
			</div>
		</div>
	</div>
	
	<div class="uk-block uk-block-secondary">
		
	</div>
	
	<!-- 上传区 -->
	<div id="uploadview" class="uk-placeholder uk-container uk-container-center uk-width-large-1-2 uk-width-medium-1-1 uk-width-small-1-1 uk-text-center">
		<!-- 进度条 -->
		<div id="progress" class="uk-progress uk-progress-striped uk-active uk-progress-small uk-hidden">
			<div id="progressbar" style="width: 0;" class="uk-progress-bar" ></div>
		</div>

		<!-- 浏览上传 -->
		<div class="uk-placeholder uk-text-center" id="updiv">
			<form class="uk-form" action="upload.do" id="upload" name="upload" enctype="multipart/form-data" method="post" target="hidden_frame">
				<i class="uk-icon-cloud-upload uk-icon-medium uk-text-muted"></i>
				<span>文件上传：</span>
				<a class="uk-form-file">选择文件<input type="file" id="file" name="file" onchange="do_upload();"></a>
				<iframe name="hidden_frame" id="hidden_frame" style="display: none"></iframe>
			</form>
		</div>
		
		
	</div>
	
	<!-- 文件区 -->
	<div class="uk-container uk-container-center">

		<!-- 大屏幕设备 -->
		<table class="uk-table uk-table-striped uk-visible-large uk-table-hover">
			<thead class="uk-text-bold"><tr><td>文件名</td><td>上传者</td><td>上传时间</td><td>操作</td></tr></thead>
			<tbody id="filetable">
				<!-- 访问服务器获得表格数据 -->
			</tbody>
		</table>

		<!-- 小屏幕设备(手机) -->
		<table class="uk-table uk-table-striped uk-hidden-large">
			<thead class="uk-text-bold"><tr><td>文件名</td><td>操作</td></tr></thead>
			<tbody id="filetable_small">
				<!-- 访问服务器获得表格数据 -->
			</tbody>
		</table>
	</div>
	
	<div class="uk-container uk-container-center uk-text-center">
		<p class="uk-visible-large"></p>
		<em class="uk-text-small uk-text-primary uk-text-bottom">Create&nbsp;By:&nbsp;蜗牛&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;xtliuke@sina.com</em>
	</div>

	<!-- 侧边导航区(小页面和手机版使用) -->
	<div id="offcanvas" class="uk-offcanvas">
		<div class="uk-offcanvas-bar">
			<ul class="uk-nav uk-nav-offcanvas">
				<li><a href="logout.do">退出</a></li>
				<li><a href="https://coding.net/u/xtliuke/p/FileShare/git" target="_blank">关于</a></li>
			</ul>
		</div>
	</div>
</body>
</html>