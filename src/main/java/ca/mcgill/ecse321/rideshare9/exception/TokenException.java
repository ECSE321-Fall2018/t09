package ca.mcgill.ecse321.rideshare9.exception;

/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */


public class TokenException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TokenException(String message) {
        super(message);
    }
}