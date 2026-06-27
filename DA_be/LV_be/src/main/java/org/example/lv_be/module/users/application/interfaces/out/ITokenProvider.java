package org.example.lv_be.module.users.application.interfaces.out;

import org.example.lv_be.module.users.domain.entity.User;

public interface ITokenProvider {
    String generateToken(User user);
}