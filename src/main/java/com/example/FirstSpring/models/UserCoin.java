package com.example.FirstSpring.models;

import javax.persistence.*;

@Entity
@Table(name = "t_user_coin")
public class UserCoin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coin_id")
    private Coin coin;

    public UserCoin(User user, Coin coin, Double count) {
        this.user = user;
        this.coin = coin;
        this.count = count;
    }


    public UserCoin(){

    }
    private Double count;

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

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
