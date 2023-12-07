package com.example.movieticketingsystem.service;

import com.example.movieticketingsystem.entity.AppUser;
import com.example.movieticketingsystem.entity.Role;
import com.example.movieticketingsystem.repository.RoleRepository;
import com.example.movieticketingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserDetailsService(@Autowired UserRepository userRepository, @Autowired RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findAppUserByEmail(email).orElseThrow(()->new UsernameNotFoundException("Username doesn't exists"));
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(),mapAuthoritiesToRole(Arrays.asList(appUser.getRole())));
    }
    public Collection<? extends GrantedAuthority> mapAuthoritiesToRole(List<Role> roleSet){
        return roleSet.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}