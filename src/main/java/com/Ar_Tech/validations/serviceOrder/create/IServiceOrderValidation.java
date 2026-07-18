package com.Ar_Tech.validations.serviceOrder.create;

import com.Ar_Tech.dto.serviceOrder.CreateServiceOrderDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IServiceOrderValidation {
    // Validar que no se encuentre ingresado
    // Validar que el customerDevice exista
    // Si se recibe estimatedDelivery verificar que sea una fecha válida
    // Si se recibe estimatedCost verificar que sea una cifra válida
    void validate(CreateServiceOrderDTO serviceOrderDTO, HttpServletRequest request);
}
