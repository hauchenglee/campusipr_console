package biz.mercue.campusipr.util;

public class CustomException {
    public static class TokenNullException extends Exception {
        public TokenNullException(String message) {
            super(message);
        }
    }

    public static class TokenExpireException extends Exception {
        public TokenExpireException(String message) {
            super(message);
        }
    }

    public static class CanNotFindDataException extends Exception {
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
}
