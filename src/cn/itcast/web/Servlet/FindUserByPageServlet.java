package cn.itcast.web.Servlet;

import cn.itcast.domain.PageBean;
import cn.itcast.domain.User;
import cn.itcast.service.UserService;
import cn.itcast.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/findUserByPageServlet")
public class FindUserByPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //获取参数
        String currentPage = request.getParameter("currentPage");//当前页码
        String rows = request.getParameter("rows");//每页显示的条数
        //获取条件查询的参数
        Map<String, String[]> condition = request.getParameterMap();

        if (currentPage==null || "".equals(currentPage) || Integer.parseInt(currentPage)<=0)
            currentPage="1";
        if (rows==null || "".equals(rows))
            rows="5";
        //调用service
        UserService service=new UserServiceImpl();
        PageBean<User> pb= service.findUserByPage(currentPage,rows,condition);
        System.out.println(pb);

        //将pageBean存入request
        request.setAttribute("pb",pb);
        //查询条件存入request，用于回显
        request.setAttribute("condition",condition);
        request.getRequestDispatcher("/list.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
