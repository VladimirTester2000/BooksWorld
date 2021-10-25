package ru.lesson.springBootProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lesson.springBootProject.exceptions.ActivationServiceException;
import ru.lesson.springBootProject.exceptions.UserServiceException;
import ru.lesson.springBootProject.models.Activation;
import ru.lesson.springBootProject.models.Role;
import ru.lesson.springBootProject.models.State;
import ru.lesson.springBootProject.models.User;
import ru.lesson.springBootProject.repositories.ActivationRepository;
import ru.lesson.springBootProject.repositories.UserRepository;
import ru.lesson.springBootProject.security.details.UserDetailsImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivationService activationService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findByUsername(s).orElseThrow(()->new IllegalArgumentException("User not found by UserDetailsServiceImpl")));
    }

    @Override
    public User addUser(User user) throws UserServiceException{
        if (existsByUsername(user.getUsername())){
           throw new UserServiceException("Username exist");
        }
        user.setState(State.UNVERIFIED);
        user.setRoles(Collections.singleton(Role.USER));
        User result = userRepository.save(user);        //сохраняем нового пользователя, и получаем его объект с заполненым id
        activationService.newActivation(result);        //создаём и отправляем активационный код на почту
        return result;
    }
    @Override
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }
    @Override
    public User save(User user){
        return userRepository.save(user);
    }

    @Override
    public boolean activateUser(String code) {
        try {
            User user = activationService.activate(code);
            user.setState(State.ACTIVE);
            userRepository.save(user);
        } catch (ActivationServiceException exception){
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public User change(User oldUser, User newUser) {
        boolean isEmailChanged = false;
        //если введён username и он отличен от существующего
        if (newUser.getUsername()!=null&&
                !newUser.getUsername().strip().isEmpty()&&
                !newUser.getUsername().equals(oldUser.getUsername())){
            oldUser.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword()!=null&&
                !newUser.getPassword().strip().isEmpty()){
            oldUser.setPassword(newUser.getPassword());
        }
        //если введён email и он отличен от существующего
        if (newUser.getEmail()!=null&&
                !newUser.getEmail().strip().isEmpty()&&
                !newUser.getEmail().equals(oldUser.getEmail())){
            oldUser.setEmail(newUser.getEmail());
            oldUser.setState(State.UNVERIFIED);
            isEmailChanged=true;
        }
        User result = userRepository.save(oldUser);
        if (isEmailChanged) activationService.newActivation(result);
        return result;
    }


}