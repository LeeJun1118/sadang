package com.market.sadang.service;

import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.SignUpForm;
import javassist.NotFoundException;

public interface AuthService {
//    void signUpUser(SignUpForm signUpForm);
    Member signUpUser(Member member);

    Member loginUser(String userId, String password) throws Exception;

    void verifyEmail(String key) throws NotFoundException;

    void modifyUserRole(Member member, UserRole userRole);

    Member findByUserId(String userId) throws NotFoundException;

    void sendVerificationMail(Member member) throws NotFoundException;
}
