package org.tlh.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ort.tlh.spring.test.App;
import ort.tlh.spring.test.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void save(){
        this.userService.insert("张三");
    }

    @Test
    public void findNames(){
        this.userService.findNames();
    }

}
