package com.example.FirstSpring.service;

import com.example.FirstSpring.models.*;
import com.example.FirstSpring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserCoinRepository userCoinRepository;

    @Autowired
    UserBrokerCoinRepository userBrokerCoinRepository;

    @Autowired
    TemporaryUserBrokerRepository temporaryUserBrokerRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).orElse(new User());
            List<UserCoin> userCoins = userCoinRepository.findByUser(user);
            List<UserBrokerCoin> userBrokerCoins = userBrokerCoinRepository.findByUser(user);
            List<TemporaryUserBroker> temporaryUserBrokers = temporaryUserBrokerRepository.findByUser(user);
            List<UserBrokerCoin> userBrokerCoins1 = userBrokerCoinRepository.findByBroker(user);
            for(UserCoin coin : userCoins) {
                userCoinRepository.delete(coin);
            }
            for(UserBrokerCoin coin : userBrokerCoins) {
                userBrokerCoinRepository.delete(coin);
            }
            for(TemporaryUserBroker coin : temporaryUserBrokers) {
                temporaryUserBrokerRepository.delete(coin);
            }
            for(UserBrokerCoin coin : userBrokerCoins1) {
                userBrokerCoinRepository.delete(coin);
            }
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }
}