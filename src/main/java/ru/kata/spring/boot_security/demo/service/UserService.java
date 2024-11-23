package ru.kata.spring.boot_security.demo.service;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;


@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getRoles()
        );
    }

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User findUserbyId(int id) { // find user by id
        Optional<User> user = userRepository.findById(id);
        return user.orElse(new User());
    }

    public List<User> getAllUsers() { // get all users
        return userRepository.findAll();
    }

    @Transactional
    public boolean createUser(User user) { // create new User
        User newUser = userRepository.findByLogin(user.getLogin());

        if (newUser != null) return false;

        // начало супер костыля
        Set<Role> abraCodabra = new HashSet<>();
        Role userRole = roleRepository.getById(1);
        abraCodabra.add(userRole);
        user.setRoles(abraCodabra);
        // конец супер костыля
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }

    @Transactional // delete user
    public boolean deleteUser(int userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean updateUser(int id, User updateUser) {
        if (findUserbyId(id) == null) return false;
        updateUser.setId(id);
        userRepository.save(updateUser);
        return true;
    }

    @Transactional
    public boolean addAdminRole(int id) {
        if (userRepository.findById(id).isEmpty()) {
            return false; // no data
        }else if (userRepository.findById(id).get().getRoles().contains(roleRepository.getById(2))){
            return false; // have admin role
        }else {
            User updateUser = userRepository.findById(id).get();
            Role roleAdmin = roleRepository.getById(2);
            System.out.println(roleAdmin.getUserRole().name());
            updateUser.getRoles().add(roleAdmin);
            userRepository.save(updateUser);
            return true;
        }

    }

    @Transactional
    public boolean removeAdminRole(int id) {
        if (userRepository.findById(id).isEmpty()) {
            return false; // no data
        } else if (!userRepository.findById(id).get().getRoles().contains(roleRepository.getById(2))){
            return false;  // don't have admin role
        } else {
            User updateUser = userRepository.findById(id).get();
            updateUser.getRoles().remove(roleRepository.getById(2));
            return true;
        }
    }


}
