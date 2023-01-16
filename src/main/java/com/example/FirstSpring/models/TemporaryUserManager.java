package com.example.FirstSpring.models;

import javax.persistence.*;

@Entity
@Table(name = "temporary_user_manager")
public class TemporaryUserManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coin_id")
    private ResidentialComplex ResidentialComplex;

    public TemporaryUserManager() {
    }

    public TemporaryUserManager(User user, ResidentialComplex residentialComplex) {
        this.user = user;
        this.ResidentialComplex = residentialComplex;
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

    public ResidentialComplex getCoin() {
        return ResidentialComplex;
    }

    public void setResidentialComplex(ResidentialComplex residentialComplex) {
        this.ResidentialComplex = residentialComplex;
    }
}
