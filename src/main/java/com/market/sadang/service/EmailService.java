package com.market.sadang.service;

public interface EmailService {
    void sendMail(String to, String sub, String text);
}
