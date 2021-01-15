package com.github.srg13.socialnetwork.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @Column(name = "profile_image")
    private String profileImage;

    @Size(min = 5, max = 15, message = "Username must not be shorter than 5 symbols and longer than 15")
    @NotBlank(message = "Username cannot be empty")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 5, message = "Password must not be shorter than 5 symbols")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "First name cannot be empty")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique_idx")})
    @Column(name = "role")
    private Set<Role> roles;
}
