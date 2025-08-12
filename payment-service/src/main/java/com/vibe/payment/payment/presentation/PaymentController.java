package com.vibe.payment.payment.presentation;

import com.vibe.payment.payment.application.PaymentService;
import com.vibe.payment.payment.dto.SubscribeRequest;
import com.vibe.payment.payment.dto.SubscriptionStatusResponse;
import com.vibe.payment.payment.dto.WebhookRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody SubscribeRequest request) {
        String sessionId = paymentService.subscribe(request);
        return new ResponseEntity<>(sessionId, HttpStatus.OK);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody WebhookRequest request) {
        paymentService.handleWebhook(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<SubscriptionStatusResponse> getStatus(@RequestParam Long userId) {
        SubscriptionStatusResponse status = paymentService.getSubscriptionStatus(userId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Payment service is up and running!");
    }
}
