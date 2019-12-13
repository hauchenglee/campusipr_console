package biz.mercue.campusipr.configuration;

import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.CustomException;
import biz.mercue.campusipr.util.ExceptionResponseBody;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private Logger log = Logger.getLogger(this.getClass().getName());
    private static ExceptionResponseBody responseBody = new ExceptionResponseBody();

    private HttpStatus _200 = HttpStatus.OK;
    private HttpStatus _500 = HttpStatus.INTERNAL_SERVER_ERROR;

    // custom exception
    @ExceptionHandler(CustomException.DataErrorException.class)
    public ResponseEntity TokenNullException(CustomException.DataErrorException e) {
        log.error("DataErrorException: " + e.getMessage());
        responseBody.setCode(Constants.INT_DATA_ERROR);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(CustomException.TokenNullException.class)
    public ResponseEntity TokenNullException(CustomException.TokenNullException e) {
        log.error("TokenNullException");
        responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(CustomException.NoPermission.class)
    public ResponseEntity NoPermission(CustomException.NoPermission e) {
        log.error("NoPermission");
        responseBody.setCode(Constants.INT_NO_PERMISSION);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(CustomException.SyntaxError.class)
    public ResponseEntity SyntaxError(CustomException.SyntaxError e) {
        log.error("SyntaxError: " + e.getMessage());
        responseBody.setCode(Constants.INT_INCORRECT_SYNTAX);
        return new ResponseEntity<>(responseBody, _200);
    }

    // java exception
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity NullPointerException(NullPointerException e) {
        log.error("NullPointerException", e);
        responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity FileNotFound(FileNotFoundException e) {
        log.error("FileNotFoundException", e);
        responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity IOException(IOException e) {
        log.error("IOException", e);
        responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        return new ResponseEntity<>(responseBody, _200);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity Exception(Exception e) {
        log.error("Exception", e);
        responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
        return new ResponseEntity<>(responseBody, _200);
    }
}
