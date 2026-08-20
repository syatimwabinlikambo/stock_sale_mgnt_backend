package com.syatimwaraph.stock_sale_mgnt_v1.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
