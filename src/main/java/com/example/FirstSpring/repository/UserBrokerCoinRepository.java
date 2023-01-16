package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.Coin;
import com.example.FirstSpring.models.User;
import com.example.FirstSpring.models.UserBrokerCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBrokerCoinRepository extends JpaRepository<UserBrokerCoin, Long> {
    UserBrokerCoin findByUserAndCoin(User user, Coin coin);

    List<UserBrokerCoin> findByUser(User user);
    List<UserBrokerCoin> findByCoin(Coin coin);
    List<UserBrokerCoin> findByBroker(User broker);
}
