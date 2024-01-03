package com.beginner.framework.servlet;

import com.beginner.framework.annotation.Autowired;
import com.beginner.framework.annotation.Controller;
import com.beginner.framework.annotation.RequestMapping;
import com.beginner.framework.annotation.Service;
import com.beginner.framework.domain.Handler;
import com.beginner.framework.exception.BException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisptcherServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(DisptcherServlet.class);


    private String scanPackage;

    String baseUrl="";

    private List<String> classNames = new ArrayList<>();
    private Map<String,Object> ioc = new HashMap<String,Object>();
    List<Handler> handlerMapping = new ArrayList<Handler>();

    public DisptcherServlet(String scanPackage){
        this.scanPackage = scanPackage;
    }

    public DisptcherServlet(String scanPackage, String baseUrl){
        this.scanPackage = scanPackage;
        this.baseUrl = baseUrl;
    }

    public DisptcherServlet(){
        super();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseServletRequest baseServletRequest = new BaseServletRequest(request);
        BaseServletResponse baseServletResponse = new BaseServletResponse(response);
        try {
            doRequest(baseServletRequest,baseServletResponse);
        } catch (BException e) {
            throw new RuntimeException(e);
        }
    }

    private void doRequest(BaseServletRequest baseServletRequest,BaseServletResponse baseServletResponse) throws BException {
        baseServletResponse.setCharacterEncoding("UTF-8");
        String url  = baseServletRequest.getRequestURI();
        String context = baseServletRequest.getContextPath();
        url = url.replace(context, "").replaceAll("/+", "/");
        String requestUrl = this.baseUrl + url;
        logger.info("request url:"+ requestUrl);
        Handler handler = getHandler(requestUrl);
        if(handler ==null) {
            logger.info("404 not found, url:"+url);
            baseServletResponse.getWriter().write("404 not found");
            return;
        }
        //获取方法的参数列表
        Class<?> [] paramTypes = handler.method.getParameterTypes();
        //保存所有需要自动赋值的参数值
        Object[] paramValues = new  Object[paramTypes.length];
        Map<String,String[]> params = baseServletRequest.getParameterMap();
        for(Map.Entry<String,String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
            //匹配上对象，填充参数
            if(!handler.paramIndexMapping.containsKey(param.getKey())) {continue;}
            int index = handler.paramIndexMapping.get(param.getKey());
            paramValues[index]  = handler.convert(paramTypes[index],value);
            logger.info("method param :["+index+"]"+value);
        }
        int reqIndex = handler.paramIndexMapping.get(BaseServletRequest.class.getName());
        paramValues[0] = baseServletRequest;
        int respIndex = handler.paramIndexMapping.get(BaseServletResponse.class.getName());
        paramValues[respIndex] = baseServletResponse;
        try {
            handler.method.invoke(handler.controller, paramValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initData(){
        //扫描包
        scanPackage();
        //扫描含有service和Controller注解的类并加入容器
        doInstance();
        //依赖注入
        doAutowired();
        //初始化路由
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        if(ioc.isEmpty()) {
            return;
        }
        for(Map.Entry<String,Object> entry:ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if(!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            };
            String controllerUrl = "";
            if(clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                controllerUrl = requestMapping.value();
            }
            Method[] methods = clazz.getMethods();
            for(Method method:methods) {
                if(!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                };
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String mappingUrl =( "/"+baseUrl+"/"+ controllerUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(mappingUrl);
                handlerMapping.add(new Handler(pattern, entry.getValue(),method));
                logger.info("add handlerMapping:"+mappingUrl+"->"+method);
            }
        }
        
    }
    
    private void doAutowired(){
        if(ioc.isEmpty()) {
            return;
        }
        for(Map.Entry<String,Object> entry:ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for(Field field:fields) {
                if(!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Autowired autowired = field.getAnnotation(Autowired.class);
                String beanName = autowired.value().trim();
                if(beanName.equals("")) {
                    beanName = lowerFirest(field.getType().getSimpleName());
                    logger.info("find Autowired:"+beanName);
                }
                //为true,则表示可以忽略访问权限的限制，直接调用。
                // 开启修改权限
                if (!field.isAccessible()){
                    field.setAccessible(true);
                }
                try {
                    Object obj = entry.getValue();
                    Object bean = ioc.get(beanName);
                    field.set(obj, bean);
                    System.out.println("entry.getValue==>" + (obj ==null));
                    if (field.isAccessible()){
                        field.setAccessible(false);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doInstance(){
        if(classNames.isEmpty()) {
            return;
        };
        try {
            for(String className : classNames){
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = lowerFirest(clazz.getSimpleName());
                    ioc.put(beanName, clazz.newInstance());
                    logger.info("add ioc bean Controller:"+beanName);
                }else if(clazz.isAnnotationPresent(Service.class)) {
                    //1. 注解中自定义了名字 ： @Service("demoService")
                    //2. 没有定义名字，使用首字母小写
                    //3. 注入的是接口，找到实现的类实例注入
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value();
                    if(!beanName.trim().equals("")) {//如果设置了名字
                        ioc.put(beanName, clazz.newInstance());
                        logger.info("add ioc bean Service:"+beanName);
                    }else {
                        beanName = lowerFirest(clazz.getSimpleName());
                        ioc.put(beanName, clazz.newInstance());
                        logger.info("add ioc bean lowerFirest Service:"+beanName);
                    }
                    Class<?>[] interfaces = clazz.getInterfaces();
                    //找到接口实现类
                    for(Class<?> i:interfaces) {
                        beanName =i.getName();
                        ioc.put(beanName, clazz.newInstance());
                        logger.info("add ioc bean Interface:"+beanName);
                    }
                }

            }
        }catch (Exception e){

        }
    }



    private  boolean isRunningFromJAR() {
        String command = System.getProperty("sun.java.command");
        return command != null && command.endsWith(".jar");
    }

    /***
     * 扫描包
     */
    private void scanPackage(){
        //判断是否是jar执行的
        if(isRunningFromJAR()){
            scanPackageInJar(getJarFilePath(),this.scanPackage);
        }else{
            //class执行时会走这里
            scanJavaFiles(this.scanPackage);
        }
    }


    private   void scanJavaFiles(String packageName) {
        try {
            // 将包名转换为相对目录路径
            String packagePath = packageName.replace('.', '/');
            // 获取类加载器
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            // 获取所有在指定包名下的资源
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                // 遍历目录，找到以".java"为扩展名的文件
                findJavaFiles(directory, packageName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void findJavaFiles(File directory, String packageName) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归查找子目录
                    findJavaFiles(file, packageName + "." + file.getName());
                } else if (file.isFile() && file.getName().endsWith(".class")) {
                    // 找到Java文件，将其类名添加到列表中
                    String className = packageName + "." + file.getName().replace(".class", "");
                    classNames.add(className);
                }
            }
        }
    }

    private static String getJarFilePath() {
        String jarFilePath = DisptcherServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        // If the path contains '%20' (URL encoding for space), replace it with a space.
        jarFilePath = jarFilePath.replace("%20", " ");
        return jarFilePath;
    }

    private  void scanPackageInJar(String jarFilePath, String packageName) {
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            String packagePath = packageName.replace(".", "/");

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                // 检查是否为类文件且在指定包下
                if (entry.getName().endsWith(".class") && entry.getName().startsWith(packagePath)) {
                    // 将类名转换为标准类名（例如：com/example/MyClass.class 转换为 com.example.MyClass）
                    String className = entry.getName().replace("/", ".").replace(".class", "");
                    classNames.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 首字母转换成小写
     * @param str
     * @return
     */
    private String lowerFirest(String str) {
        char [] cs = str.toCharArray();
        cs[0]+=32;
        return String.valueOf(cs);
    }

    private Handler getHandler(String url) {
        if(handlerMapping.isEmpty()) {
            return null;
        }
        for(Handler handler:handlerMapping) {
            try{
                Matcher matcher  = handler.pattern.matcher(url);
                if(!matcher.matches()){continue;}
                return handler;
            }catch(Exception e) {
                e.getStackTrace();
            }
        }
        return null;
    }

}
