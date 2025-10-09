package com.server.util;

import com.server.models.User;
import com.server.services.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class UserValidator implements Validator {
    private final UserDetailsService userDetailsService;

    public UserValidator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClazz) {
        return User.class.equals(aClazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User person = (User) target;
        try{
            userDetailsService.loadUserByUsername(person.getEmail());
        }catch (UsernameNotFoundException ignored){
            return;
        }
        errors.rejectValue("email", "", "This email already exists");
    }
}


