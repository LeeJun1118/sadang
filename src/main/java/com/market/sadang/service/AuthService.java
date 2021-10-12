package com.market.sadang.service;

import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.SignUpForm;
import javassist.NotFoundException;

public interface AuthService {
    void signUpUser(SignUpForm signUpForm);

    Member loginUser(String username, String password) throws Exception;

    void verifyEmail(String key) throws NotFoundException;

    void modifyUserRole(Member member, UserRole userRole);

    Member findByUsername(String username) throws NotFoundException;

    void sendVerificationMail(String email) throws NotFoundException;
}
