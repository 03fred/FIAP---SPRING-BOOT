package br.com.fiap.domain.gateways;

public interface EmailGateway {
    
    /**
     * Sends a password reset token to the specified email address
     * @param toEmail the recipient email address
     * @param token the password reset token
     */
    void sendPasswordResetToken(String toEmail, String token);
    
    /**
     * Sends a general notification email
     * @param toEmail the recipient email address
     * @param subject the email subject
     * @param message the email message content
     */
    void sendNotificationEmail(String toEmail, String subject, String message);
}