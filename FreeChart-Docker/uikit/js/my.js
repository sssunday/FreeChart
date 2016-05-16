// 页面中的业务逻辑表单提交等

$(function(){
	$(document).keydown(function(event) {
		if(event.keyCode==13){
			$('#submit').click();
		}
	});
});

// 提交登录信息
function login_submit(){

	var username = $('#username').val();
	var password = $.md5($('#password').val());//对密码进行MD5加密

	$.post('login.do','username='+username+"&password="+password,
		function(data){
			if(data == 1){
				self.location="filelist.jsp"
			}else{
				UIkit.notify("用户名或密码错误！", {timeout: 800});
				$('#password').val('');
			}
		});
}

// 提交修改密码的信息
function chpwd_submit(){

	var username = $('#username').val();
	var oldpwd = $.md5($('#oldpwd').val());//对密码进行MD5加密
	var newpwd_1 = $('#newpwd_1').val();
	var newpwd_2 = $('#newpwd_2').val();

	if(newpwd_1.length < 5 || newpwd_2.length < 5){
		UIkit.notify("密码必须大于5位！", {timeout: 800});
		return;
	}

	if(newpwd_1 == newpwd_2){
		$.post('login.do','username='+username+"&password="+oldpwd,
			function(data){
				if(data == 1){
					var password = $.md5($('#newpwd_1').val());
					$.post('modifypwd.do', 'username='+username+"&password="+password, 
						function(data) {
						if(data == 1){
							UIkit.notify("修改成功！即将返回登录页面。", {timeout: 800});
							setTimeout(function(){self.location="login.jsp"},1000);
						}else{
							UIkit.notify("系统忙，请稍后再试！", {timeout: 800});
						}
					});
				}else{
					UIkit.notify("初始用户名或密码错误！", {timeout: 800});
					$('#password').val('');
				}
			});
	}else{
		UIkit.notify("两次密码不一致！", {timeout: 800});
	}
}

//提交注册信息
function regist_submit(){

	var username = $('#username').val();
	var pwd_1 = $('#pwd_1').val();
	var pwd_2 = $('#pwd_2').val();

	if(pwd_1.length < 5 || pwd_2.length < 5 || username .length < 5){
		UIkit.notify("用户名或密码必须大于5位！", {timeout: 800});
		return;
	}

	if(pwd_1 == pwd_2){
		$.post('hasname.do', 'username='+username, 
			function(data) {
				if(data == 1){
					var password = $.md5($('#pwd_1').val());
					$.post('adduser.do', 'username='+username+"&password="+password, 
						function(data) {
							if(data == 1){
								UIkit.notify("注册成功！即将返回登录页面。", {timeout: 800});
								setTimeout(function(){self.location="login.jsp"},1000);
							}else{
								UIkit.notify("系统忙，请稍后再试！", {timeout: 800});
							}
						});
				}else{
					UIkit.notify("用户名已经存在！", {timeout: 800});
				}
			});
	}else{
		UIkit.notify("两次密码不一致！", {timeout: 800});
	}
}

//根据权限显示相应功能
function showview(){
			$.post('showview.do', function(data) {
				if(data == 1){
					$('.del').addClass('uk-hidden');
				}
				if(data == 0){
					$('.del').addClass('uk-hidden');
					$('#uploadview').addClass('uk-hidden');
				}
			});
		}

//上传文件
var IntervalID;
function do_upload(){
	if($('#file').val() != ""){
		$('#upload').submit();
		$('#updiv').addClass('uk-hidden');
		$('#progress').removeClass('uk-hidden');
		IntervalID = setInterval(function(){do_getprogress();},500);
	}else{
		UIkit.notify("请选择一个文件！", {timeout: 800});
	}
}

//获取上传进度
function do_getprogress(){
	$.post('uploadstate.do',function(data) {
		if(data == 200){
			clearInterval(IntervalID);
			$('#updiv').removeClass('uk-hidden');
			$('#progress').addClass('uk-hidden');
			$('#progressbar').css('width',0);
			UIkit.notify("文件已经存在！请更改文件名。", {timeout: 800});
			setTimeout(function(){do_showall();},100);
			return;
		}
		if(data == 300){
			clearInterval(IntervalID);
			$('#updiv').removeClass('uk-hidden');
			$('#progress').addClass('uk-hidden');
			$('#progressbar').css('width',0);
			UIkit.notify("上传成功！", {timeout: 800});
			setTimeout(function(){do_showall();},100);
			return;
		}
		if(data == 400){
			clearInterval(IntervalID);
			$('#updiv').removeClass('uk-hidden');
			$('#progress').addClass('uk-hidden');
			$('#progressbar').css('width',0);
			UIkit.notify("系统忙，请稍后再试！", {timeout: 800});
			setTimeout(function(){do_showall();},100);
			return;
		}
		if(data <100){
			var curwidth = parseInt(data)*($('#progress').width()/100);
			$('#progressbar').css('width',curwidth);
		}
	});
}

// 搜索
function do_search(){
	$('#filetable').load('showfile.do', 'searchcode='+$('#searchcode').val());
	showview();
}

//显示全部文件
function do_showall(){
	$('#filetable').load('showfile.do', 'searchcode=');
	$('#filetable_small').load('showfilesmall.do');
	showview();
}

//删除文件
function do_delete(filename){
	if(confirm("确定要删除 "+filename+" 吗？")){
		$.post('removefile.do', 'fname='+filename, function(data) {
			if(data == 1){
				UIkit.notify("删除成功！", {timeout: 800});
				setTimeout(function(){do_showall();},100);
				return;
			}
			if(data == 0){
				UIkit.notify("系统忙请稍后再试！", {timeout: 800});
				setTimeout(function(){do_showall();},100);
				return;
			}
		});
	}
}
