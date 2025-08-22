package com.example.shop.helpers;

import com.example.shop.model.User;
import com.example.shop.service.PersonDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClazz) {
        return User.class.equals(aClazz);
    }

    //TODO create method thar returns optional list in personService
    @Override
    public void validate(Object target, Errors errors) {
        User person = (User) target;
        try{
            personDetailsService.loadUserByUsername(person.getEmail());
        }catch (UsernameNotFoundException ignored){
            return;
        }
        errors.rejectValue("email", "", "This email already exists");
    }
}
