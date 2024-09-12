package com.clase.crud.Producto;

 import java.io.Serializable;

import com.clase.crud.Categoria.CategoriaModel;

import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
 import jakarta.persistence.GenerationType;
 import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
 import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

 @Data
 @Entity
 @Table(name="producto")
public class ProductoModel implements Serializable{
	 @Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;
		
		@NotBlank
		private String nome;
		@NotNull
		private Double preco;

		@ManyToOne  
    	@JoinColumn(name = "categoria_id")  
		private CategoriaModel categoria;
	
		
}
