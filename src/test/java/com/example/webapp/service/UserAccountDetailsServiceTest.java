package com.example.webapp.service;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.UserAccount;
import com.example.webapp.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountDetailsServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountDetailsService userAccountDetailsService;

    @Test
    void loadUserByUsername() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);
        userAccount.setUsername("testuser");
        userAccount.setPassword("password123");
        userAccount.setRole(Role.STUDENT);
        
        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.of(userAccount));
        
        UserDetails result = userAccountDetailsService.loadUserByUsername("testuser");
        
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password123", result.getPassword());
    }
}