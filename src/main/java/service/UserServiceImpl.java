package service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import db.DataBase;
import model.User;

/**
 * single tone이 적합
 * 나중에 enum으로 바꾸자
 */
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static UserServiceImpl singleTone;
    private DataBase database;
    private UserServiceImpl() {
        logger.debug("create UserServiceImpl");
    }

    public static UserServiceImpl getService() {
        if(singleTone == null) {
            singleTone = new UserServiceImpl();
        }
        return singleTone;
    }

    @Override
    public User signUp(User user) {
        logger.debug("sign up. user: {}",user);
        database.addUser(user);
        return user;
    }

    @Override
    public User login(String id, String pw) {
        User user = database.findUserById(id);

        if(user != null && user.getPassword().equals(pw)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(database.findAll());
    }
}
