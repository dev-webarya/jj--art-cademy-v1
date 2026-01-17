package com.artacademy.service.notification;

import com.artacademy.entity.ArtOrder;
import com.artacademy.entity.User;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.repository.UserRepository;
import com.artacademy.service.notification.model.NotificationMessage;
import com.artacademy.service.notification.template.EmailTemplateEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * High-level service handling business events for Art Academy notifications.
 */
@Service
@RequiredArgsConstructor
public class ArtNotificationService {

        private final NotificationSender notificationSender;
        private final EmailTemplateEngine templateEngine;
        private final UserRepository userRepository;

        private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        // --- Assets ---
        private static final String ICON_ART = "\uD83C\uDFA8"; // 🎨
        private static final String ICON_CHECK = "\u2705"; // ✅
        private static final String ICON_CROSS = "\u274C"; // ❌
        private static final String ICON_TRUCK = "\uD83D\uDE9A"; // 🚚

        private static final String COLOR_PRIMARY = "#6366F1"; // Indigo
        private static final String COLOR_SUCCESS = "#10B981";
        private static final String COLOR_DANGER = "#EF4444";
        private static final String COLOR_DARK = "#1F2937";

        public void sendOtp(User user, String otp) {
                String emailHtml = templateEngine.buildProfessionalEmail(
                                ICON_ART, COLOR_PRIMARY,
                                "Verify Account",
                                "Use the code below to verify your identity.",
                                user.getFirstName(),
                                "OTP-SECURE",
                                new String[][] { { "OTP Code", otp }, { "Validity", "10 Minutes" } },
                                "Security Alert", "Do not share this code with anyone.");

                String sms = String.format("%s Your Art Academy OTP is %s. Valid for 10 mins.", ICON_ART, otp);

                notificationSender.send(user.getEmail(), user.getPhoneNumber(),
                                new NotificationMessage("auth_otp", "Verify Your Account", emailHtml, sms));
        }

        public void sendOrderConfirmation(ArtOrder order) {
                User user = getUser(order.getUserId());
                String emailHtml = templateEngine.buildProfessionalEmail(
                                ICON_CHECK, COLOR_SUCCESS,
                                "Order Confirmed",
                                "Thank you for your purchase! We are preparing your art items.",
                                user.getFirstName(),
                                order.getOrderNumber(),
                                new String[][] {
                                                { "Amount", "₹" + order.getTotalPrice() },
                                                { "Items", String.valueOf(order.getItems().size()) },
                                                { "Date", dateFormatter.format(
                                                                order.getCreatedAt().atZone(ZoneId.systemDefault())) }
                                },
                                "Processing", "We will notify you when it ships.");

                String sms = String.format("%s Order %s Confirmed! Amount: ₹%s. We are processing it now.",
                                ICON_CHECK, order.getOrderNumber(), order.getTotalPrice());

                notificationSender.send(user.getEmail(), user.getPhoneNumber(),
                                new NotificationMessage("order_confirmed",
                                                "Order Confirmed - " + order.getOrderNumber(), emailHtml, sms));
        }

        public void sendOrderShipped(ArtOrder order, String trackingNumber, String carrier) {
                User user = getUser(order.getUserId());
                String emailHtml = templateEngine.buildProfessionalEmail(
                                ICON_TRUCK, COLOR_DARK,
                                "Order Shipped",
                                "Your art items are on the way!",
                                user.getFirstName(),
                                order.getOrderNumber(),
                                new String[][] {
                                                { "Carrier", carrier },
                                                { "Tracking #", trackingNumber },
                                                { "Destination", order.getShippingAddress() }
                                },
                                "In Transit", "Track your package on the carrier's website.");

                String sms = String.format("%s Order %s Shipped via %s. Track: %s",
                                ICON_TRUCK, order.getOrderNumber(), carrier, trackingNumber);

                notificationSender.send(user.getEmail(), user.getPhoneNumber(),
                                new NotificationMessage("order_shipped", "Order Shipped - " + order.getOrderNumber(),
                                                emailHtml, sms));
        }

        public void sendOrderCancelled(ArtOrder order, String reason) {
                User user = getUser(order.getUserId());
                String emailHtml = templateEngine.buildProfessionalEmail(
                                ICON_CROSS, COLOR_DANGER,
                                "Order Cancelled",
                                "Your order has been cancelled.",
                                user.getFirstName(),
                                order.getOrderNumber(),
                                new String[][] {
                                                { "Reason", reason },
                                                { "Refund", "Initiated (if applicable)" }
                                },
                                "Refund Info", "Refunds typically process within 3-5 business days.");

                String sms = String.format("%s Order %s Cancelled. Reason: %s.",
                                ICON_CROSS, order.getOrderNumber(), reason);

                notificationSender.send(user.getEmail(), user.getPhoneNumber(),
                                new NotificationMessage("order_cancelled",
                                                "Order Cancelled - " + order.getOrderNumber(), emailHtml, sms));
        }

        private User getUser(String userId) {
                return userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        }
}
