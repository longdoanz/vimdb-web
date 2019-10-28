package com.viettel.imdb.rest.exception;

import com.viettel.imdb.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * @author quannh22
 * @since 08/08/2019
 */
public class ExceptionType {
    public static class VIMDBRestClientError extends RuntimeException {
        protected String message;
        protected Enum error;

        public HttpStatus getStatusCode() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        public VIMDBRestClientError(String format) {
            super(format);
            this.message = format;
            this.error = ErrorCode.INTERNAL_ERROR;
        }

        public VIMDBRestClientError() {
            this("Rest Client error");
        }

        public String getMessage() {
            return message;
        }

        public Enum getErrorCode() {
            return error;
        }
    }

    public static class InvalidRecordError extends VIMDBRestClientError {
        public HttpStatus statusCode = HttpStatus.BAD_REQUEST;
    }

    public static class InvalidOperationError extends VIMDBRestClientError {

        public InvalidOperationError(String message) {
            super(String.format(message));
            this.message = message;
        }

        public InvalidOperationError() {
            this("Invalid Key type");
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public static class InvalidKeyError extends VIMDBRestClientError {

        public InvalidKeyError(String message) {
            super(message);
            this.message = message;
        }

        public InvalidKeyError() {
            this("Invalid Key Structure");
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public static class NotFoundError extends VIMDBRestClientError {
        public NotFoundError() {
            this("Record Not Found");
        }

        public NotFoundError(String reason) {
            super(reason);
            this.message = reason;
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.NOT_FOUND;
        }
    }

    public static class InvalidDateFormat extends VIMDBRestClientError {

        public InvalidDateFormat() {
            this("Invalid Date Format");
        }

        public InvalidDateFormat(String dateStr) {
            super(String.format("Invalid date format: %s", dateStr));
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public static class ClusterUnstableError extends VIMDBRestClientError {

        public ClusterUnstableError() {
            this("Unable to complete operation, cluster is unstable.");
        }

        public ClusterUnstableError(String message) {
            super(message);
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public static class NotImplementError extends VIMDBRestClientError {
        public NotImplementError() {
            this("Not Implemented");
        }

        public NotImplementError(String message) {
            super(message);
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.NOT_IMPLEMENTED;
        }
    }

    public static class ParseError extends VIMDBRestClientError {
        public ParseError() {
            this("Data format not correct");
        }

        public ParseError(String message) {
            super(message);
            this.error = RestErrorCode.MISSING_INFORMATION;
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
