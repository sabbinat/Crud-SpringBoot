package com.clase.crud.Categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDto(@NotBlank String descricao) {
	
}
