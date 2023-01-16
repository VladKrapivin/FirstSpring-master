package com.example.FirstSpring.models;

import javax.persistence.*;

@Entity
@Table(name = "t_user_coin")
public class UserResidentialcomplex {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coin_id")
    private ResidentialComplex coin;

    public UserResidentialcomplex(User user, ResidentialComplex residentialComplex, Double count) {
        this.user = user;
        this.coin = residentialComplex;
        this.count = count;
    }


    public UserResidentialcomplex(){

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

    public ResidentialComplex getCoin() {
        return coin;
    }

    public void setCoin(ResidentialComplex residentialComplex) {
        this.coin = residentialComplex;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
