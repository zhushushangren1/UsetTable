package cn.itcast.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

//敏感词汇过滤filter
@WebFilter("/*")
public class SensitiveWordsFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //1.创建代理对象，增强getParameter方法
        ServletRequest proxy_req= (ServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(), req.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //增强getParameter
                //判断是否是getParameter方法
                if (method.getName().equals("getParameter")){
                    //增强返回值
                    //获取返回值
                    String value= (String) method.invoke(req,args);
                    if (value!=null){
                        for (String str : list){
                            if (value.contains(str)){
                                value=value.replaceAll(str,"***");
                            }
                        }
                    }
                    return value;
                }
                //判断是否是getParameterMap
                if (method.getName().equals("getParameterMap")){
                    Map<String,String[]>map=(Map<String, String[]>) method.invoke(req,args);//真实返回值
                    Map<String,String[]>remap=new HashMap<>();
                    Set<String> strings = map.keySet();
                    for (String key:strings){//遍历map
                        String value[]=map.get(key);
                        for (String str:list){//遍历敏感词
                            if (value[0].contains(str)){
                                value[0]=value[0].replaceAll(str,"***");
                            }
                        }
                    }
                    return map;
                }
                //判断是否是getParameterValue
                if (method.getName().equals("getParameterValue")){
                    String values[]= (String[]) method.invoke(req,args);
                    for (int i=0;i<values.length;i++){
                        for (String str:list){
                            if (values[i].equals(str)){
                                values[i]=values[i].replaceAll(str,"***");
                            }
                        }
                    }
                    return values;
                }
                return method.invoke(req,args);
            }
        });
        chain.doFilter(proxy_req, resp);
    }
    private List<String> list=new ArrayList<String>();//敏感词汇的集合
    public void init(FilterConfig config) throws ServletException {
        try {
            //获取文件的路径
            ServletContext servletContext = config.getServletContext();
            String realPath = servletContext.getRealPath("/WEB-INF/classes/敏感词汇.txt");
            BufferedReader br=new BufferedReader(new FileReader(realPath));
            //读取
            String line=null;
            while((line=br.readLine())!=null){
                list.add(line);
            }
            br.close();
            System.out.println(list);//打印测试
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
