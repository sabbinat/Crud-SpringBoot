package com.clase.crud.Categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
	
	@Autowired
	CategoriaRepository repository;
	
	@GetMapping("/inserir/")
	public String inserir() {
		return "categoria/inserir";
	}
	
	@PostMapping("/inserir/")
	public String inserirBD(@ModelAttribute @Valid CategoriaDto categoriaDTO, 
		BindingResult result, RedirectAttributes msg) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("errorCadastrar", "Error al ingresar la categoría");
			return "redirect:/categoria/inserir/";
		}
		var categoriaModel = new CategoriaModel();
		BeanUtils.copyProperties(categoriaDTO, categoriaModel);
		repository.save(categoriaModel);
		msg.addFlashAttribute("sucessoCadastrar", "Categoría ingresado exitosamente!");
		return "redirect:/categoria/listar/";
	}
	
	@GetMapping("/listar/")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("categoria/listar");
		List<CategoriaModel> lista = repository.findAll(); 
		mv.addObject("categorias", lista);
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value="id") int id){
		ModelAndView mv = new ModelAndView("categoria/editar");
		Optional<CategoriaModel> categoria = repository.findById(id); 
		mv.addObject("id", categoria.get().getId());
		mv.addObject("descricao", categoria.get().getDescricao());
		return mv;
	}

	@PostMapping("/editar/{id}")
		public String editarBD(@ModelAttribute @Valid CategoriaDto categoriaDTO, 
		BindingResult result, RedirectAttributes msg, 
		@PathVariable(value="id") int id) {
		
		Optional<CategoriaModel> categoria = repository.findById(id);
		
		if(result.hasErrors()) {
			msg.addFlashAttribute("errorCadastrar", "Error al editar la categoría");
			return "redirect:/categoria/listar/";
		}
		var categoriaModel = categoria.get();
		BeanUtils.copyProperties(categoriaDTO, categoriaModel);
		repository.save(categoriaModel);
		msg.addFlashAttribute("sucessoCadastrar", "Usuario ingresado exitosamente!");
		return "redirect:../../usuario/listar/";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") int id, RedirectAttributes msg){
	    Optional<CategoriaModel> categoria = repository.findById(id); 
	    if(categoria.isEmpty()) {
	        msg.addFlashAttribute("errorEliminar", "Categoria no encontrado");
	        return "redirect:/categoria/listar/";
	    }
	    repository.deleteById(id);
	    msg.addFlashAttribute("sucessoEliminar", "Categoria eliminado exitosamente!");
	    return "redirect:/categoria/listar/";
	}
}
