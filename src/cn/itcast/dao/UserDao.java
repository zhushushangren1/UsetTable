package cn.itcast.dao;

import cn.itcast.domain.User;

import java.util.List;
import java.util.Map;

//用户操作的Dao
public interface UserDao {
    //查询所有用户信息
    public List<User> findAll();
    //根据用户名密码查询数据
    public User findUserByUsernameAndPassword(String username,String password);
    //添加用户
    public void add(User user);
    //删除用户
    public void delete(int id);
    //修改用户
    public void update(User user);
    //查询指定用户
    public User findById(int id);
    //查询总记录数
    int findTotalCount(Map<String, String[]> condition);
    //翻页查询的记录
    List<User> findByPage(int start, int rows, Map<String, String[]> condition);
}
