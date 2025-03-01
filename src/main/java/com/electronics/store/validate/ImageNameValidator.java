package com.electronics.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //logic for validation
        if(s.isBlank()){ // simple blank check
            return false;
        }
        return true;
    } //interface name
}
