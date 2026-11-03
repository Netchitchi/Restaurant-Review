package com.netchitchi.dining.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(
        @NotBlank(message = "O nome é obrigatório.") @Size(min = 3, max = 100, message = "Selecione um nome que tenha entre 3 a 100 carateres") String name,
        @NotBlank(message = "A cidade é obrigatória.") @Size(min = 3, max = 50, message = "A cidade deve ter entre 3 e 50 carateres.") String city,
        @NotBlank(message = "O distrito é obrigatório.") @Size(min = 3, max =50, message = "O distrito deve ter entre 3 e 50 carateres.") String state,
        @NotBlank(message = "O código postal é obrigatório.") @Pattern(regexp = "\\d{4}-\\d{3}", message = "Código postal inválido (formato: 1000-001).") String zipCode) {
}
