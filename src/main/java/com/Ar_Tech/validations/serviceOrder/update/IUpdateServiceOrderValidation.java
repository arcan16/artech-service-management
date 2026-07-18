package com.Ar_Tech.validations.serviceOrder.update;

import com.Ar_Tech.dto.serviceOrder.UpdateServiceOrderDTO;
import com.Ar_Tech.models.ServiceOrderEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface IUpdateServiceOrderValidation {
    /*
              1. Si se recibe el customerDeviceId verificar que exista
               Validar que no se encuentre ingresado en una orden distinta a la actual (no sería posible reingresar un
               teléfono que ya tenemos ingresado) con un "status" distinto a "DELIVERED"
              2. Sí llega el Status verificar que exista - No es necesario, ya que al momento de recepción debe empatar con
              el Enum en la deserialization
              3. Sí se recibe el usuario para asignación verificar que exista
              4. Sí se recibe la estimatedDelivery de entrega verificar que sea válida (después de la fecha actual)
              5. Sí se recibe deliveredAt verificar que la fecha sea posterior a la fecha de recepción
              6. Sí se recibe el estimatedCost verificar que sea un valor positivo superior a 0
              7. Verificar que el usuario que realiza la modificación tenga "status: ACTIVE"
             */
    void validation(UpdateServiceOrderDTO updateServiceOrderDTO, ServiceOrderEntity serviceOrder,
                    HttpServletRequest request);
}
