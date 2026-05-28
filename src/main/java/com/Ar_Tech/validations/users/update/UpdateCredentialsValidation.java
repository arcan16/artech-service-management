package com.Ar_Tech.validations.users.update;

import com.Ar_Tech.dto.users.UserFullDTO;
import com.Ar_Tech.infra.errors.MyIntegrityValidation;
import com.Ar_Tech.infra.security.utils.JwtUtils;
import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.models.enums.EUserRole;
import com.Ar_Tech.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateCredentialsValidation implements IUpdateUserValidation {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(UserFullDTO userFullDTO, HttpServletRequest request) {
        UserEntity author = jwtUtils.getUserFromRequest(request);

        if(userFullDTO.role() == null)
            return;

        /// Valida que no intentes actualizar tu propio rol
        if(userFullDTO.id().equals(author.getId())){
            throw new MyIntegrityValidation("No es posible actualizar tu role",400);

        }

        /// Valída que solo un admin actualice el rol de otro usuario
        if(!author.getRole().equals(EUserRole.ADMIN)){
            throw new MyIntegrityValidation("No tienes privilegios para realizar actualizaciones de rol",400);
        }

        /// Un ADMIN solo puede actualizar el rol de otro ADMIN si este fue creado primero
        UserEntity userToUpdate = userRepository.findById(userFullDTO.id()).
                orElseThrow(()-> new MyIntegrityValidation("El registro indicado no existe",400));
        if(author.getCreatedAt().isAfter(userToUpdate.getCreatedAt())){
            throw new MyIntegrityValidation("No tienes privilegios para realizar esta actualización",400);
        }
    }
}
