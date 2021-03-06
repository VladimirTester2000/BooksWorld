package ru.lesson.springBootProject.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import ru.lesson.springBootProject.exceptions.UserServiceException;
import ru.lesson.springBootProject.models.Author;
import ru.lesson.springBootProject.models.User;

import java.util.List;
public interface UserService extends UserDetailsService {
    public User addUser(User user);
    public boolean existsByUsername(String username);
    public List<User> findAll();
    public User save(User user);

    boolean activateUser(String code);

    User change(User oldUser, User newUser);
    default boolean checkPassword(User user, String password){
        System.out.println("need to implement checkPassword in UserService");
        return user.getPassword().equals(password);
    }
    boolean checkPasswordConfirm(String password, String passwordConfirm);


    User getSubscriptions(User user) throws UserServiceException;

    @Transactional
    User addSubscriptions(User user, Author author)throws UserServiceException;

    @Transactional
    User removeSubscriptions(User user, Author author)throws UserServiceException;
}
