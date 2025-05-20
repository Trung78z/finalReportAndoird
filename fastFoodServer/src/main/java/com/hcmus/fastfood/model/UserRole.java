package com.hcmus.fastfood.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String name;
    
    @Column(length = 200)
    private String description;
    
    @Column(name = "can_manage_orders")
    private boolean canManageOrders;
    
    @Column(name = "can_manage_menu")
    private boolean canManageMenu;
    
    @Column(name = "can_manage_users")
    private boolean canManageUsers;
    
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<User> users = new ArrayList<>();
    
    // Custom constructor without users list
    public UserRole(String name, String description) {
        this.name = name;
        this.description = description;
        this.users = new ArrayList<>();
    }
}