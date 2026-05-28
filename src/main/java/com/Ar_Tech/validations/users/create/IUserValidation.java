package com.Ar_Tech.validations.users.create;

import com.Ar_Tech.dto.users.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IUserValidation {

    void validate(UserDTO userDTO, HttpServletRequest request);
}
