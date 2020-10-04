package cn.itcast.service;

import cn.itcast.domain.PageBean;
import cn.itcast.domain.User;

import java.util.List;
import java.util.Map;

//用户管理的业务接口
public interface UserService {
    //查询所有用户信息
    public List<User> findAll();
    //登陆校验
    public User login(User user);
    //保存user
    public void addUser(User user);
    //删除user
    public void deleteUser(String id);
    //修改user
    public void updateUser(User user);
    //查询指定用户
    public User findUserById(String id);
    //批量删除用户
    public void delSelectedUser(String[] ids);
    //分页条件查询
    PageBean<User> findUserByPage(String currentPage, String rows, Map<String, String[]> condition);
}
