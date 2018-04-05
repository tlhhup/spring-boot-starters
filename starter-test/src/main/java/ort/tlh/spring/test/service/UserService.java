package ort.tlh.spring.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tlh.springboot.autoconfigure.annotation.TargetDataSource;
import ort.tlh.spring.test.dao.UserDao;

import java.util.List;

@Service("userService")
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    @TargetDataSource(("d1"))
    public boolean insert(String name){
       return this.userDao.insert(name);
    }

    @TargetDataSource("d1")
    public List<String> findNames(){
        return this.userDao.findNames();
    }

}
