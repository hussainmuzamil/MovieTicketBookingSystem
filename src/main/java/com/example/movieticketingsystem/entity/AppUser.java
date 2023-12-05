package com.example.movieticketingsystem.entity;


import com.example.movieticketingsystem.config.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "authentication",uniqueConstraints = {@UniqueConstraint(columnNames = {"email","isActive"}) })
@EntityListeners(AuditingEntityListener.class)
public class AppUser extends Auditable<AppUser> implements UserDetails   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String password;

    @Email(message = "Please enter a valid email Id", regexp="^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}")
    @jakarta.validation.constraints.NotNull(message = "Email Already exist")
//    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "appUser",fetch = FetchType.EAGER)
    private List<Token> token;

    @NotNull
    private boolean isActive;
    //    private Date modifiedAt;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

//    @LastModifiedDate
//    @Column(insertable = false)
//    private LocalDateTime modifiedDate;
//
//    @CreatedDate
//    @Column(
//            updatable = false,
//            nullable = false
//    )
//    private LocalDateTime createdDate;
//
//    @LastModifiedBy
//    @Column(insertable = false)
//    private String modifiedBy;

//    @CreatedBy
//    @Column(
//            updatable = false,
//            nullable = false
//    )
//    private String addedBy;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(role).stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
