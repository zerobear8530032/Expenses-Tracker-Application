package com.zerobear.UserService.utility;

import com.zerobear.UserService.exception.InvalidEmailException;
import com.zerobear.UserService.exception.InvalidPasswordException;
import com.zerobear.UserService.exception.InvalidUserNameException;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;

@Service
public class ValidationUtility {

    public boolean validatePassword(String password){
        if(password==null || password.isBlank()){
            throw new InvalidPasswordException("Password length should be at least 8 ");
        }
        if(password.length()>24){
            throw new InvalidPasswordException("Password length should not exceed 24 characters ");
        }
        boolean specialSymbol= false;
        boolean numbers= false;
        boolean capitalLetters= false;
        boolean smallLetters= false;

        for (int i = 0; i < password.length(); i++) {
            char ch =password.charAt(i);
            if(ch>='a' && ch<='z'){
                smallLetters=true;
            }else if(ch>='A' && ch<='Z') {
                capitalLetters = true;
            }else if(ch>='0' && ch<='9'){
                numbers=true;
            }if(ch>='a' && ch<='z'){
                smallLetters=true;
            }else {
                specialSymbol=true;
            }
        }
        boolean valid=specialSymbol && numbers && capitalLetters && smallLetters;
        if(valid){
            return  true;
        }else{
            throw new InvalidPasswordException("Create Strong Password ");
        }
    }

    public boolean validateEmail(String email) {
        if(email == null || email.isBlank()){
            throw new InvalidEmailException("Please Enter Valid Email ");
        }
        boolean valid= email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") ;
        if(valid){
            return  true;
        }else{
            throw new InvalidEmailException("Please Enter Valid Email");
        }
    }
    public boolean validateUserName(String name) {
        if(name == null || name.isBlank()) {
            throw new InvalidUserNameException("Please Enter Valid UserName ");
        }
        if (name.length()<5){
            throw  new InvalidUserNameException("user-name should be at least 5 characters");
        }

        return name.length()>=5;
    }
}
