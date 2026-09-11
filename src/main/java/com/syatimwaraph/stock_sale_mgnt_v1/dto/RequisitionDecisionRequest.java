package com.syatimwaraph.stock_sale_mgnt_v1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequisitionDecisionRequest {

    @NotNull
    private RequisitionStatus status;

    @Size(max = 500)
    private String note;
}