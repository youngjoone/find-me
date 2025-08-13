package com.vibe.payment.payment.presentation;

// import com.vibe.payment.payment.application.PaymentService;
// import com.vibe.payment.payment.dto.SubscribeRequest;
// import com.vibe.payment.payment.dto.SubscriptionStatusResponse;
// import com.vibe.payment.payment.dto.WebhookRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    // private final PaymentService paymentService;
    //
    // public PaymentController(PaymentService paymentService) {
    //     this.paymentService = paymentService;
    // }

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(/*@RequestBody SubscribeRequest request*/) {
        // String sessionId = paymentService.subscribe(request);
        return ResponseEntity.ok(Map.of("status", "ok", "sessionId", "stub_session_id_12345"));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(/*@RequestBody WebhookRequest request*/) {
        // paymentService.handleWebhook(request);
        System.out.println("Received stub webhook call for /pay/webhook."); // Added log for debugging
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus(/*@RequestParam Long userId*/) {
        // SubscriptionStatusResponse status = paymentService.getSubscriptionStatus(userId);
        Map<String, Object> stubStatus = Map.of(
            "userId", 123,
            "plan", "premium",
            "status", "active",
            "subscribed_at", Instant.now().toString(),
            "expires_at", Instant.now().plusSeconds(30 * 24 * 3600).toString()
        );
        return ResponseEntity.ok(stubStatus);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Payment service is up and running!");
    }
}