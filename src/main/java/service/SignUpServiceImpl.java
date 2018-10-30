package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import db.DataBase;
import model.User;

/**
 * single tone이 적합
 * 나중에 enum으로 바꾸자
 */
public class SignUpServiceImpl implements SignUpService {
    private Logger logger = LoggerFactory.getLogger(SignUpServiceImpl.class);
    private static SignUpServiceImpl singleTone;
    private DataBase database;
    private SignUpServiceImpl() {
        logger.debug("create SignUpServiceImpl");
    }

    public static SignUpServiceImpl getService() {
        if(singleTone == null) {
            singleTone = new SignUpServiceImpl();
        }
        return singleTone;
    }

    @Override
    public User signUp(User user) {
        logger.debug("sign up. user: {}",user);
        database.addUser(user);
        return user;
    }
}
