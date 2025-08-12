package com.vibe.payment.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionStatusResponse {
    private String plan;
    private String status;
}
