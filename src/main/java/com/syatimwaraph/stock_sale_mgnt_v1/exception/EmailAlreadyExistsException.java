package com.syatimwaraph.stock_sale_mgnt_v1.exception;


public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}