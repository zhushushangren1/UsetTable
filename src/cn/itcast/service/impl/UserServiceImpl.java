package cn.itcast.service.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.dao.impl.UserDaoImpl;
import cn.itcast.domain.PageBean;
import cn.itcast.domain.User;
import cn.itcast.service.UserService;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao dao=new UserDaoImpl();
    @Override
    public List<User> findAll() {
        //调用dao来完成查询
        return dao.findAll();
    }
    public User login(User user){
        return dao.findUserByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
    public void addUser(User user){
        dao.add(user);
    }

    @Override
    public void deleteUser(String id) {
        dao.delete(Integer.parseInt(id));
    }

    @Override
    public void updateUser(User user) {
        dao.update(user);
    }

    @Override
    public User findUserById(String id) {
        return dao.findById(Integer.parseInt(id));
    }

    @Override
    public void delSelectedUser(String[] ids) {
        //遍历数组
        if (ids!=null)
            for (String id :ids){
                dao.delete(Integer.parseInt(id));
            }
    }

    @Override
    public PageBean<User> findUserByPage(String _currentPage, String _rows , Map<String ,String[]> condition) {
        int currentPage=Integer.parseInt(_currentPage);
        int rows=Integer.parseInt(_rows);
        if (currentPage<=0)
            currentPage=1;
        //创建一个空的pageBean对象
        PageBean<User>pb=new PageBean<User>();
        //设置参数
        pb.setCurrentPage(currentPage);
        pb.setRows(rows);
        //调用dao查询总记录数
        int totalCount=dao.findTotalCount(condition);
        pb.setTotalCount(totalCount);

        //计算总页码
        int totalPage=totalCount%rows==0?totalCount/rows:(totalCount/rows)+1;
        pb.setTotalPage(totalPage);
        //判断当前页码是否大于总页数
        if (currentPage>totalPage)//防止下一页的请求出错
            currentPage=totalPage;
        pb.setCurrentPage(currentPage);
        //调用dao查询list集合
        //计算开始的那一条记录
        int start=(currentPage-1)*rows;
        List<User>list= dao.findByPage(start,rows,condition);
        pb.setList(list);

        return pb;
    }
}
