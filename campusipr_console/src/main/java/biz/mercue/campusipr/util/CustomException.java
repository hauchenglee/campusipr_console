package biz.mercue.campusipr.util;

public class CustomException {
    public static class TokenIdNullException extends Exception {
        public TokenIdNullException() {
        }

        public TokenIdNullException(String message) {
            super(message);
        }
    }

    public static class TokenNullException extends Exception {
        public TokenNullException() {
        }

        public TokenNullException(String message) {
            super(message);
        }
    }

    public static class TokenExpireException extends Exception {
        public TokenExpireException() {
        }

        public TokenExpireException(String message) {
            super(message);
        }
    }

    public static class CanNotFindDataException extends Exception {
        public CanNotFindDataException() {
        }

        public CanNotFindDataException(String message) {
            super(message);
        }
    }

    public static class DataErrorException extends Exception {
        public DataErrorException() {
        }

        public DataErrorException(String message) {
            super(message);
        }
    }

    public static class NoPermission extends Exception {
        public NoPermission() {
        }

        public NoPermission(String message) {
            super(message);
        }
    }

    public static class SyntaxError extends Exception {
        public SyntaxError() {
        }

        public SyntaxError(String message) {
            super(message);
        }
    }

}
