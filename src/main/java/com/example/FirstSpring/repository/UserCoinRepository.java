package com.example.FirstSpring.repository;

import com.example.FirstSpring.models.Coin;
import com.example.FirstSpring.models.User;
import com.example.FirstSpring.models.UserCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCoinRepository extends JpaRepository<UserCoin, Long> {
    List<UserCoin> findByUser(User user);
    List<UserCoin> findByCoin(Coin coin);

    Optional<UserCoin> findById(Long id);
    UserCoin findByUserAndCoin(User user,Coin coin);
}
