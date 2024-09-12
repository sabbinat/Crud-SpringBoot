package com.clase.crud.Home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clase.crud.Usuario.UsuarioRepository;

@Controller
@RequestMapping("/index")
public class HomeController {
    @Autowired
	UsuarioRepository repository;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
}
