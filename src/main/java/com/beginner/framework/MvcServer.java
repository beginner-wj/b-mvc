package com.beginner.framework;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.beginner.framework.domain.AppProperties;
import com.beginner.framework.servlet.DisptcherServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class MvcServer {

    private AppProperties appProperties;
    public static void main(String[] args) throws Exception {
        MvcServer mvcServer = new MvcServer();
        mvcServer.starter();
    }

    void starter() throws Exception {
        //加载配置文件
        doConfig();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(this.appProperties.getServerpath());

        DisptcherServlet dh=new DisptcherServlet(this.appProperties.getScanpackage(),this.appProperties.getServerpath());
        dh.initData();
        context.addServlet(new ServletHolder(dh), "/*");


        Server server = new Server(this.appProperties.getServerport());
        server.setHandler(context);
        server.start();
    }


    void doConfig() throws Exception {
        this.appProperties = readYamlFile();
    }

    private AppProperties readYamlFile() throws Exception{
        String path = "classpath:application.yml";
        AppProperties properties  = YamlUtil.loadByPath(path, AppProperties.class);

        if(properties.getServerport() == 0){
            throw new Exception("请检查配置文件服务端口");
        }
        if(StrUtil.isBlank(properties.getServerpath())){
            throw new Exception("请检查配置文件服务路径");
        }
        return properties;
    }
}
