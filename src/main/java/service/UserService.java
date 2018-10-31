package service;

import java.util.List;

import model.User;

public interface UserService {
    User signUp(User user);
    User login(String id, String pw);
    List<User> getUserList();
}
