FreeChart 文件共享
===
*欢迎大家一起来完善这个小妖精。*
___

>后台采用 JSP 、 Servlet 进行开发

* util 包中封装了对数据库的操作，应用在初始化的时候会读取 web.xml 中的连接数据。
* entity 包中封装了文件信息类和用户信息类。
* dao 包中实现了对数据库中的数据具体的操作，例如：增加一个用户，修改密码等。
* 上传文件保存的路径可以在 web.xml 中修改。

>页面部分采用 UIKit 进行开发

* 一个好的框架能给开发省不少的时间，同时也保证了页面的美观。

>数据库使用了 MySQL

* 因为 MySQL 易用，简单，想要换成其它数据库的开发者大大们可以在 web.xml 中修改。

>采用Docker进行发布

* Docker 容器是一门比较新的技术，有点像虚拟机和沙盘结合的产物，兼并两者的优点。
* Docker 使得应用发布变得更简单。
* 更多关于Docker的信息可访问http://www.daocloud.io
* 发布用的Docker文件及应用的代码：https://coding.net/u/xtliuke/p/FileShareAPP/git

想看看应用的样子？
看这里：http://www.freechart.win
有什么好的建议或者是发现了BUG，请联系我：xtliuke@sina.com
