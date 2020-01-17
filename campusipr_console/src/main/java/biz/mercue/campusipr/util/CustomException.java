package biz.mercue.campusipr.util;

public class CustomException {
    public static class TokenNullException extends RuntimeException {
        public TokenNullException() {
        }

        public TokenNullException(String message) {
            super(message);
        }
    }

    public static class CanNotFindDataException extends RuntimeException {
        public CanNotFindDataException() {
        }

        public CanNotFindDataException(String message) {
            super(message);
        }
    }

    public static class DataErrorException extends RuntimeException {
        public DataErrorException() {
        }

        public DataErrorException(String message) {
            super(message);
        }
    }

    public static class NoPermission extends RuntimeException {
        public NoPermission() {
        }

        public NoPermission(String message) {
            super(message);
        }
    }

    public static class SyntaxError extends RuntimeException {
        public SyntaxError() {
        }

        public SyntaxError(String message) {
            super(message);
        }
    }

}
