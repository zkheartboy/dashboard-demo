Java全栈后台，纯java，不用写一句html和js
==================================
主要是为java后端开发人员提供一个快速后台管理页面，减少花在前端页面上的开发时间。虽然页面调整的灵活性不够，对于常用的展示完全足够。

本程序使用框架 [Vaadin Framework](https://vaadin.com/framework). 可以使用vaadin丰富的插件，可以实现常用的后台功能.

[![View the application](https://vaadin.com/documents/10187/2487938/Dashboard+Demo+2014/a37b2c4d-c941-48fe-97c3-ad5a60586882?t=1412769929183)](http://demo.vaadin.com/dashboard)

运行程序
==
运行maven命令'mvn -Pproduction-mode jetty:run'，调用jetty运行。浏览器打开地址http://localhost:8080查看运行效果。

运行maven命令'mvn package' 打包成war文件，可以运行在任何web容器。


Licenses
==
The source code is released under Apache 2.0.
