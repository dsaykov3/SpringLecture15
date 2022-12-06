package net.codejava.spring.exceptions;

public class CrudValidationException extends RuntimeException{

    public CrudValidationException(String message){
        super(message);
    }
}
