package service;

import model.User;

public interface UserService {
    User signUp(User user);
    User login(String id, String pw);
}
