package com.example.FirstSpring.models;

import javax.persistence.*;

@Entity
@Table(name = "temporary_user_broker")
public class TemporaryUserBroker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    public TemporaryUserBroker() {
    }

    public TemporaryUserBroker(User user, Coin coin) {
        this.user = user;
        this.coin = coin;
    }

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

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }
}
