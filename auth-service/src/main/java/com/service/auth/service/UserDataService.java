package com.service.auth.service;

import com.service.auth.config.UserData;
import com.service.auth.dto.User;
import com.service.auth.exception.NotFoundException;
import com.service.auth.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDataService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return new UserData(user);
    }

    public String save(User user) {

        if(userRepository.findByEmail(user.getEmail())!=null){
            return "Duplicate userName: " + user.getEmail();
        }
        return "userName: " + userRepository.save(user).getEmail();
    }

    public User get(Long userId) {
       return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id " + userId +" does not exist."));
    }

}
