package com.ghopital.projet.auth;

import com.ghopital.projet.entity.User;
import com.ghopital.projet.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByCin(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with cin")
        );
        return new UserModel(user.getCin(), user.getPassword(), user.getRole());
    }
}
