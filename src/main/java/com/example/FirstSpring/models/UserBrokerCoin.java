package com.example.FirstSpring.models;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "t_user_broker")
public class UserBrokerCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "broker_id")
    private User broker;

    public UserBrokerCoin() {
    }

    public UserBrokerCoin(User user, User broker, Coin coin) {
        this.user = user;
        this.broker = broker;
        this.coin = coin;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getBroker() {
        return broker;
    }

    public void setBroker(User broker) {
        this.broker = broker;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }
}
