package ca.mcgill.ecse321.rideshare9.exception;

/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */



public class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}