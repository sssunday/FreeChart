<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="en" class="uk-height-1-1">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>FreeChart 文件共享</title>
	<link rel="stylesheet" type="text/css" href="./uikit/css/uikit.gradient.min.css">
	<link rel="stylesheet" type="text/css" href="./uikit/css/components/notify.gradient.min.css">
	<link rel="shortcut icon" href="./favicon.ico">
	<script type="text/javascript" src="./uikit/js/jquery.js"></script>
	<script type="text/javascript" src="./uikit/js/uikit.min.js"></script>
	<script type="text/javascript" src="./uikit/js/components/notify.min.js"></script>
	<script type="text/javascript" src="./uikit/js/jquery.md5.js"></script>
	<script type="text/javascript" src="./uikit/js/my.js"></script>
</head>
<body class="uk-height-1-1">

	<!-- 导航区 -->
	<div class="uk-navbar" data-uk-sticky="{clsactive:uk-navbar-attached}">
		<div class="uk-container uk-container-center">
			<ul class="uk-navbar-nav">
				<li class="uk-visible-large"><a href="./login.jsp">登录</a></li>
				<li class="uk-visible-large"><a href="https://coding.net/u/xtliuke/p/FileShare/git" target="_blank">关于</a></li>
			</ul>
			<a href="#offcanvas" data-uk-offcanvas class="uk-navbar-toggle uk-hidden-large"></a>
			<span class="uk-navbar-brand uk-navbar-center uk-hidden-large">注册</span>
			<div class="uk-navbar-content uk-navbar-flip uk-visible-large">
				<span class="uk-icon-user"> 未登录</span>
			</div>
		</div>
	</div>
	
	<div class="uk-block uk-block-secondary">
		
	</div>

	<!-- 注册区 -->
	<div class="uk-block uk-block-large uk-block-primary">
		<div class="uk-vertical-align uk-text-center">
			<div class="uk-vertical-align-middle" width="250px">
				<img class="uk-margin-bottom uk-animation-shake uk-animation-hover" width="120" height="100" src="./img/7.png">
				<form id="regist" class="uk-form uk-panel uk-panel-box">
					<div class="uk-form-row">
						<input id="username" class="uk-width-1-1 uk-form-large" placeholder="用户名"></input>
					</div>
					<div class="uk-form-row">
						<input id="pwd_1" class="uk-width-1-1 uk-form-large" type="password" placeholder="密码"></input>
					</div>
					<div class="uk-form-row">
						<input id="pwd_2" class="uk-width-1-1 uk-form-large" type="password" placeholder="确认密码"></input>
					</div>
					<div class="uk-form-row">
						<a onclick="regist_submit();" class="uk-width-1-1 uk-button uk-button-danger uk-button-large" id="submit">注册</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<div class="uk-container uk-container-center uk-text-center">
		<p class="uk-visible-large"></p>
		<em class="uk-text-small uk-text-primary uk-text-bottom">Create&nbsp;By:&nbsp;蜗牛&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;xtliuke@sina.com</em>
	</div>

	<!-- 侧边导航区(小页面和手机版使用) -->
	<div id="offcanvas" class="uk-offcanvas">
		<div class="uk-offcanvas-bar">
			<ul class="uk-nav uk-nav-offcanvas">
				<li><a href="./login.jsp">登录</a></li>
				<li><a href="https://coding.net/u/xtliuke/p/FileShare/git" target="_blank">关于</a></li>
			</ul>
		</div>
	</div>
</body>
</html>