package com.Ar_Tech.infra.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class MyIntegrityValidation extends RuntimeException{

    private final int code;
    public MyIntegrityValidation(String message, int code){
        super(message);
        this.code = code;
    }
}
