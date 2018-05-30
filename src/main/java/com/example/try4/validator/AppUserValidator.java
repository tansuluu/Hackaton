package com.example.try4.validator;

import com.example.try4.dao.AppUserDAO;
import com.example.try4.entity.AppUser;
import com.example.try4.form.AppUserForm;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AppUserValidator implements Validator {

    private Pattern pattern;
    private Matcher matcher;

    String STRING_PATTERN = "[a-zA-Z]+";


    // common-validator library.
    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private AppUserDAO appUserDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == AppUserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        AppUserForm form = (AppUserForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "", "Email is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "", "User name is required");

        AppUser userAccount = appUserDAO.findAppUserByUserName( form.getUserName());
        if (userAccount != null) {
            if (form.getUserId() == null) {
                errors.rejectValue("userName", "", "User name is not available");
                return;
            } else if (!form.getUserId().equals(userAccount.getUserId() )) {
                errors.rejectValue("userName", "", "User name is not available");
                return;
            }
        }
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "", "First name is required");
        if (!(form.getFirstName() != null && form.getFirstName().isEmpty())) {
            pattern = Pattern.compile(STRING_PATTERN);
            matcher = pattern.matcher(form.getFirstName());
            if (!matcher.matches()) {
                errors.rejectValue("firstName", "",
                        "Enter a valid First name");
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "", "Last name is required");
        if (!(form.getLastName() != null && form.getLastName().isEmpty())) {
            pattern = Pattern.compile(STRING_PATTERN);
            matcher = pattern.matcher(form.getLastName());
            if (!matcher.matches()) {
                errors.rejectValue("lastName", "",
                        "Enter a valid Last name");
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "", "Password is required");

        if (errors.hasErrors()) {
            return;
        }

        if (!emailValidator.isValid(form.getEmail())) {

            errors.rejectValue("email", "", "Email is not valid");
            return;
        }


        userAccount = appUserDAO.findByEmail(form.getEmail());
        if (userAccount != null) {
                errors.rejectValue("email", "", "Email is not available");

            } }

}
