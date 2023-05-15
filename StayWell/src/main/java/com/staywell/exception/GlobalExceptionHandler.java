package com.staywell.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MyErrorDetails> myInvalidDataExpHandler(MethodArgumentNotValidException me) {
		MyErrorDetails err = new MyErrorDetails();
		err.setTimestamp(LocalDateTime.now());
		err.setMessage("Validation Error");
		err.setDetails(me.getBindingResult().getFieldError().getDefaultMessage());
		return new ResponseEntity<MyErrorDetails>(err,HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyErrorDetails> myAnyExceptionHandler(Exception e, WebRequest req){
		MyErrorDetails err = new MyErrorDetails(LocalDateTime.now(),e.getMessage(),req.getDescription(false));
		return new ResponseEntity<MyErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<MyErrorDetails> myNoExceptionHandler(NoHandlerFoundException ne, WebRequest req){
		MyErrorDetails err = new MyErrorDetails(LocalDateTime.now(),ne.getMessage(),req.getDescription(false));
		return new ResponseEntity<MyErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}
}
