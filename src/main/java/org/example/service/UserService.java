package org.example.service;

import org.example.model.GenericResponse;
import org.example.model.User;

public interface UserService {
    Boolean userValidation(String username, String password);

    Boolean otpValidation(String otp, String username);

    GenericResponse registerClient(User user);

    void removeUnactivatedUsers();
}
