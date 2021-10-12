package com.market.sadang.service;

import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import javassist.NotFoundException;

public interface AuthService {
    void signUpUser(Member member);

    Member loginUser(String username, String password) throws Exception;

    void verifyEmail(String key) throws NotFoundException;

    void modifyUserRole(Member member, UserRole userRole);

    Member findByUsername(String username) throws NotFoundException;

    void sendVerificationMail(Member member) throws NotFoundException;
}
