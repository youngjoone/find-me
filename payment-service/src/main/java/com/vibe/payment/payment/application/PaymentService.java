package com.vibe.payment.payment.application;

import com.vibe.payment.subscription.domain.Subscription;
import com.vibe.payment.subscription.repository.SubscriptionRepository;
import com.vibe.payment.payment.dto.SubscribeRequest;
import com.vibe.payment.payment.dto.WebhookRequest;
import com.vibe.payment.payment.dto.SubscriptionStatusResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private final SubscriptionRepository subscriptionRepository;

    public PaymentService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public String subscribe(SubscribeRequest request) {
        // This is a stub. In a real scenario, this would interact with a payment gateway.
        // For now, we just return a dummy session ID.
        return "stub-" + System.currentTimeMillis();
    }

    @Transactional
    public void handleWebhook(WebhookRequest request) {
        // This is a stub. In a real scenario, this would validate the webhook signature
        // and process the payment event.
        // For now, we just update the subscription status to 'active'.
        Long userId = request.getUserId(); // Assuming userId is part of the webhook payload

        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserId(userId);

        if (existingSubscription.isPresent()) {
            Subscription subscription = existingSubscription.get();
            subscription.setStatus("active");
            subscription.setPlan("premium"); // Assuming webhook implies premium
            subscription.setCurrentPeriodEnd(LocalDateTime.now().plusMonths(1)); // Stub: 1 month
            subscriptionRepository.save(subscription);
        } else {
            // Create a new subscription if it doesn't exist
            Subscription newSubscription = new Subscription();
            newSubscription.setUserId(userId);
            newSubscription.setPlan("premium");
            newSubscription.setStatus("active");
            newSubscription.setCurrentPeriodEnd(LocalDateTime.now().plusMonths(1));
            subscriptionRepository.save(newSubscription);
        }
    }

    @Transactional(readOnly = true)
    public SubscriptionStatusResponse getSubscriptionStatus(Long userId) {
        return subscriptionRepository.findByUserId(userId)
                .map(sub -> new SubscriptionStatusResponse(sub.getPlan(), sub.getStatus()))
                .orElse(new SubscriptionStatusResponse("free", "inactive")); // Default for non-subscribers
    }
}
