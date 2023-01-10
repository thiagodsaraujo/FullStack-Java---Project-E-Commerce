package com.titashop.admin.security;

import com.titashop.admin.user.UserRepository;
import com.titashop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TitaShopUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.getUserByEmail(email);

        if (user != null){
            return new TitaShopUserDetails(user);
        }
        throw new UsernameNotFoundException("Could not find user with this email: " + email);
    }
}
