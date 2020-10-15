package cn.itcast.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
//登录状态验证的过滤器
@WebFilter("/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //判断是否是登陆相关的资源
        HttpServletRequest request=(HttpServletRequest) req;
        String uri = request.getRequestURI();//获取请求路径
        //判断是否包含登陆相关资源，要注意排除掉css /js /图片/验证码等
        if (uri.contains("/login.jsp")||uri.contains("/loginServlet")||uri.contains("/css/")||uri.contains("/js/")||uri.contains("/fonts/")||uri.contains("checkCodeServlet")){
            //包含这些，证明用户想要登陆，放行
            chain.doFilter(req,resp);
        }else{
            //验证用户是否登陆
            if (request.getSession().getAttribute("user")!=null){
                //已经登陆，放行
                chain.doFilter(req,resp);
            }else{
                //没有登陆，跳转登陆页面
                request.setAttribute("login_msg","您尚未登陆，请登录");
                request.getRequestDispatcher("/login.jsp").forward(req,resp);
            }

        }
        //        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
