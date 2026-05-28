package com.Ar_Tech.validations.users.update;

import com.Ar_Tech.dto.users.UserFullDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IUpdateUserValidation {

    void validate(UserFullDTO userFullDTO, HttpServletRequest request);
}
