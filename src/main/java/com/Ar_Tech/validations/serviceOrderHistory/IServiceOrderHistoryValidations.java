package com.Ar_Tech.validations.serviceOrderHistory;

import com.Ar_Tech.dto.serviceOrderHistory.CreateServiceOrderHistoryDTO;
import com.Ar_Tech.models.ServiceOrderEntity;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EServiceOrderStatus;

public interface IServiceOrderHistoryValidations {

    void validate(EServiceOrderStatus status, UserEntity author, ServiceOrderEntity serviceOrder);
}
