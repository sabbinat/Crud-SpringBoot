package com.clase.crud.Producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoDto(@NotBlank String nome, @NotNull Double preco, @NotNull int categoriaId) {

}
