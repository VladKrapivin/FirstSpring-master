package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.Coin;
import com.example.FirstSpring.models.TemporaryUserBroker;
import com.example.FirstSpring.models.User;
import com.example.FirstSpring.models.UserBrokerCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemporaryUserBrokerRepository extends JpaRepository<TemporaryUserBroker, Long> {

    TemporaryUserBroker findByUserAndCoin(User user, Coin coin);

    List<TemporaryUserBroker> findByUser(User user);
    List<TemporaryUserBroker> findByCoin(Coin coin);
}
