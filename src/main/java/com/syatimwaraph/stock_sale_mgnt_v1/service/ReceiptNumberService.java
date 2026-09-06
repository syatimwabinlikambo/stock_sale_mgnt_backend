package com.syatimwaraph.stock_sale_mgnt_v1.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReceiptNumberService {

    private final AtomicLong counter = new AtomicLong(1);

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generateReceiptNumber() {

        String date =
                LocalDate.now().format(DATE_FORMAT);

        long number =
                counter.getAndIncrement();

        return String.format(
                "REC-%s-%06d",
                date,
                number
        );
    }
}

//@Service
//public class ReceiptNumberService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public ReceiptNumberService(
//            JdbcTemplate jdbcTemplate
//    ) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//
//    public String generateReceiptNumber() {
//
//        Long number =
//                jdbcTemplate.queryForObject(
//                        "SELECT nextval('receipt_number_seq')",
//                        Long.class
//                );
//
//
//        String date =
//                LocalDate.now()
//                        .format(
//                                DateTimeFormatter.ofPattern(
//                                        "yyyyMMdd"
//                                )
//                        );
//
//
//        return String.format(
//                "REC-%s-%06d",
//                date,
//                number
//        );
//    }
//}