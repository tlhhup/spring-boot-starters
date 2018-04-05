package ort.tlh.spring.test.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insert(String name){
        String sql="insert into users(name) values(?)";
        return this.jdbcTemplate.update(sql,name)>0;
    }

    public List<String> findNames(){
        String sql="select name from users";
        return this.jdbcTemplate.query(sql,new SingleColumnRowMapper<String>(String.class));
    }


}
