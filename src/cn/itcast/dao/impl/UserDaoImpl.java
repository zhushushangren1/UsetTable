package cn.itcast.dao.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import cn.itcast.util.JDBCUtils;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDaoImpl implements UserDao {
    private JdbcTemplate template =new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<User> findAll() {
        //使用jdbc操作数据库
        //1.定义sql
        String sql="select * from user";
        List<User> users = template.query(sql, new BeanPropertyRowMapper<User>(User.class));
        return users;
    }

    @Override
    public User findUserByUsernameAndPassword(String username,String password) {
        try {
            String sql="select * from user where username=? and password=?";
            User user=template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),username,password);
            return user;
        }catch (Exception  e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(User user) {
        //sql
        String sql="insert into user values(null,?,?,?,?,?,?,null,null)";
        //执行sql
        template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail());
    }
    @Override
    public void delete(int id) {
        String sql="delete from user where id=?";
        template.update(sql,id);
    }

    @Override
    public void update(User user) {
        String sql="update user set name=?,gender=?,age=?,address=?,qq=?,email=? where id=?";
        template.update(sql,user.getName(),user.getGender(),user.getAge(),user.getAddress(),user.getQq(),user.getEmail(),user.getId());
    }

    @Override
    public User findById(int id) {
        String sql="select * from user where id=?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),id);
    }

    @Override
    public int findTotalCount(Map<String, String[]> condition) {
        String sql="select count(*) from user where 1=1";
        //遍历map拼接sql
        StringBuilder sb=new StringBuilder(sql);
        Set<String> keyset=condition.keySet();
        //d定义一个参数的集合
        List<Object>params=new ArrayList<Object>();
        for (String key : keyset){
            if ("currentPage".equals(key)||"rows".equals(key))
                continue;
            //获取value
            String value = condition.get(key)[0];
            //判断value是否有值
            if (value!=null && !"".equals(value)){
                sb.append(" and "+key+" like ? ");
                params.add("%"+value+"%");//存？条件的值
            }
        }
//        System.out.println(sb.toString());
//        System.out.println(params);
        return template.queryForObject(sb.toString(),Integer.class,params.toArray());
    }

    @Override
    public List<User> findByPage(int start, int rows, Map<String, String[]> condition) {
        String sql="select * from user where 1=1";
        Set<String> keys = condition.keySet();
        StringBuilder sb=new StringBuilder(sql);
        List<Object> params=new ArrayList<Object>();
        for (String key : keys){
            if ("currentPage".equals(key)||"rows".equals(key))
                continue;
            String value=condition.get(key)[0];
            if (value!=null && !"".equals(key)){
                sb.append(" and "+key+" like ? ");
                params.add("%"+value+"%");
            }
        }
//        System.out.println(sb.toString());
//        System.out.println(params);
        //添加分页
        sb.append(" limit ?,?");
        params.add(start);
        params.add(rows);
        return template.query(sb.toString(), new BeanPropertyRowMapper<User>(User.class),params.toArray());
    }
}