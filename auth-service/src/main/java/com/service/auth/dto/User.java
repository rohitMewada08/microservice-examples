package com.service.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String name;
    private String email;
    private String phoneNo;
    private boolean enabled;

    @ManyToMany(targetEntity = Role.class,cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    List<Role> roles;

}
