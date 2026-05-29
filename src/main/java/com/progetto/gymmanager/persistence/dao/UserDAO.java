package com.progetto.gymmanager.persistence.dao;

import com.progetto.gymmanager.model.User;
import java.io.IOException;
import java.util.List;

public interface UserDAO {
    User findByNicknameAndPassword(String nickname, String password) throws IOException;
    boolean existsNickname(String nickname) throws IOException;
    void save(User user) throws IOException;
    void updateUser(User user) throws IOException;
    void deleteUser(String nickname) throws IOException;
    List<User> findUsersByRole(String role) throws IOException;
}