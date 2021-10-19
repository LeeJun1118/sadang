package com.market.sadang.service.authUtil;

public interface EmailService {
    void sendMail(String to, String sub, String text);
}
