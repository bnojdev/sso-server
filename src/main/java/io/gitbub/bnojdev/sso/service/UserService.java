package io.github.bnojdev.sso.service;

import io.github.bnojdev.sso.model.GenericResponse;
import io.github.bnojdev.sso.model.User;

public interface UserService {
    Boolean userValidation(String username, String password);

    Boolean otpValidation(String otp, String username);

    GenericResponse registerClient(User user);

    void removeUnactivatedUsers();
}
