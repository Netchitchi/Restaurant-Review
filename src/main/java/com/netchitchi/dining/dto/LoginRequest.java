package com.netchitchi.dining.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "O nome é obrigatorio") @Size(min = 3, max = 40, message = "Use o mesmo nome que utilizou para registrar") String name,
        @NotBlank(message = "a password e obrigatoria") @Size(min = 8, max = 20, message = "Use a mesma password que utilizou para registrar") String password) {


}
