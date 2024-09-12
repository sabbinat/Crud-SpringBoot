package com.clase.crud.Usuario;

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
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	UsuarioRepository repository;
	
	@GetMapping("/inserir/")
	public String inserir() {
		return "usuario/inserir";
	}
	
	@PostMapping("/inserir/")
	public String inserirBD(@ModelAttribute @Valid UsuarioDto usuarioDTO, 
		BindingResult result, RedirectAttributes msg) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("errorCadastrar", "Error al ingresar el nuevo usuario");
			return "redirect:/usuario/inserir/";
		}
		var usuarioModel = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDTO, usuarioModel);
		usuarioModel.setTipo("Común");
		repository.save(usuarioModel);
		msg.addFlashAttribute("sucessoCadastrar", "Usuario ingresado exitosamente!");
		return "redirect:/usuario/listar/";
	}
	@GetMapping("/listar/")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("usuario/listar");
		List<UsuarioModel> lista = repository.findAll(); 
		mv.addObject("usuarios", lista);
		return mv;
	}
	
	@GetMapping("/editar/{id}")
		public ModelAndView editar(@PathVariable(value="id") int id){
			ModelAndView mv = new ModelAndView("usuario/editar");
			Optional<UsuarioModel> usuario = repository.findById(id); 
			mv.addObject("id", usuario.get().getId());
			mv.addObject("nome", usuario.get().getNome());
			mv.addObject("email", usuario.get().getEmail());
			mv.addObject("senha", usuario.get().getSenha());
			mv.addObject("tipo", usuario.get().getTipo());
			return mv;
		}
	
	@PostMapping("/editar/{id}")
		public String editarBD(@ModelAttribute @Valid UsuarioDto usuarioDTO, 
		BindingResult result, RedirectAttributes msg, 
		@PathVariable(value="id") int id) {
		
		Optional<UsuarioModel> usuario = repository.findById(id);
		
		if(result.hasErrors()) {
			msg.addFlashAttribute("errorCadastrar", "Error al editar el usuario");
			return "redirect:/usuario/listar/";
		}
		var usuarioModel = usuario.get();
		BeanUtils.copyProperties(usuarioDTO, usuarioModel);
		usuarioModel.setTipo(usuarioDTO.tipo());
		repository.save(usuarioModel);
		msg.addFlashAttribute("sucessoCadastrar", "Usuario ingresado exitosamente!");
		return "redirect:../../usuario/listar/";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") int id, RedirectAttributes msg){
	    Optional<UsuarioModel> usuario = repository.findById(id); 
	    if(usuario.isEmpty()) {
	        msg.addFlashAttribute("errorEliminar", "Usuario no encontrado");
	        return "redirect:/usuario/listar/";
	    }
	    repository.deleteById(id);
	    msg.addFlashAttribute("sucessoEliminar", "Usuario eliminado exitosamente!");
	    return "redirect:/usuario/listar/";
	}

}
