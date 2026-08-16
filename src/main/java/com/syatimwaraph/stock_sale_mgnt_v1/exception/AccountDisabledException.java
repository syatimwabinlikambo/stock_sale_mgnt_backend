package com.syatimwaraph.stock_sale_mgnt_v1.exception;

public class AccountDisabledException extends RuntimeException {
    public AccountDisabledException(String message) {
        super(message);
    }
}
