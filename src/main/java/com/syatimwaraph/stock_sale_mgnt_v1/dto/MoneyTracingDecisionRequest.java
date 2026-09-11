package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyDecision;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyTracingDecisionRequest {

    @NotNull
    private MoneyDecision decision;

    @Size(max = 500)
    private String comment;
}