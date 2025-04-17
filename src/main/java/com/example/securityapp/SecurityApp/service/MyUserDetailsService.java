package com.example.securityapp.SecurityApp.service;

import com.example.securityapp.SecurityApp.model.MyUserDetails;
import com.example.securityapp.SecurityApp.model.Users;
import com.example.securityapp.SecurityApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);

        if(user == null){
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
        return new MyUserDetails(user);
    }
}
