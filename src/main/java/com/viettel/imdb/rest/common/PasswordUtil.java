package com.viettel.imdb.rest.common;

import com.viettel.imdb.rest.exception.RestErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,32}$";

    public static Result validateUsername(String username) {
        if(username == null || username.isEmpty()) {
            return new Result(false, RestErrorCode.INVALID_USERNAME.name());
        }

        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        if(matcher.matches()) {
            return new Result(true);
        }

        return new Result(false, RestErrorCode.INVALID_USERNAME.name());
    }

    public static Result validatePassword(String userName, String password)
    {
        Result result = new Result();
        result.setError(RestErrorCode.INVALID_PASSWORD.name());
        if(password==null || password.isEmpty()) {
            result.setAccepted(false);
            return result;
        }

        if (password.length() < 8 || password.length() > 32)
        {
            result.setAccepted(false);
            result.addError("Password should be more than 7 characters and less than 33 characters in length.");
            return result;
        }
        if (password.indexOf(userName) > -1)
        {
            result.setAccepted(false);
            result.addError("Password should not contain user name");
            return result;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
        {
            result.setAccepted(false);
            result.addError("Password should contain at least one upper case alphabet");
            return result;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars ))
        {
            result.setAccepted(false);
            result.addError("Password should contain at least one lower case alphabet");
            return result;
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers ))
        {
            result.setAccepted(false);
            result.addError("Password should contain at least one number.");
            return result;
        }
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!password.matches(specialChars ))
        {
            result.setAccepted(false);
            result.addError("Password should contain at least one special character");
            return result;
        }

        result.setAccepted(true);
        return result;
    }

    public static class Result {
        private boolean accepted;
        private String error;

        public Result() {
        }

        public Result(boolean accepted) {
            this.accepted = accepted;
        }

        public Result(boolean accepted, String error) {
            this.accepted = accepted;
            this.error = error;
        }

        public boolean isAccepted() {
            return accepted;
        }

        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public void addError(String error) {
            this.error = this.error + " - " + error;
        }
    }

}
