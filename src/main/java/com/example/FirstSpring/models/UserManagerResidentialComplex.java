package com.example.FirstSpring.models;

import javax.persistence.*;

@Entity
@Table(name = "t_user_broker")
public class UserManagerResidentialComplex {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;

    public UserManagerResidentialComplex() {
    }

    public UserManagerResidentialComplex(User user, User manager, ResidentialComplex residentialComplex) {
        this.user = user;
        this.manager = manager;
        this.residentialComplex = residentialComplex;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "residentialComplex_id")
    private ResidentialComplex residentialComplex;

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
        return manager;
    }

    public void setBroker(User manager) {
        this.manager = manager;
    }

    public ResidentialComplex getCoin() {
        return residentialComplex;
    }

    public void setCoin(ResidentialComplex residentialComplex) {
        this.residentialComplex = residentialComplex;
    }
}
