package com.clase.crud.Producto;

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

import com.clase.crud.Categoria.CategoriaModel;
import com.clase.crud.Categoria.CategoriaRepository;

import org.springframework.ui.Model;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/producto")
public class ProductoController {
	@Autowired
	ProductoRepository repository;

	@Autowired
	CategoriaRepository categoriaRepository;
	
	@GetMapping("/inserir/")
	public String inserir(Model model) {
		List<CategoriaModel> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
		return "producto/inserir";
	}
	
	@PostMapping("/inserir/")
	public String inserirBD(@ModelAttribute @Valid ProductoDto productoDTO, 
		BindingResult result, RedirectAttributes msg) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("errorCadastrar", "Error al ingresar el producto");
			return "redirect:/producto/inserir";
		}
		CategoriaModel categoria = categoriaRepository.findById(productoDTO.categoriaId())
                              .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
		var productoModel = new ProductoModel();
		BeanUtils.copyProperties(productoDTO, productoModel);
		productoModel.setCategoria(categoria);
		repository.save(productoModel);
		msg.addFlashAttribute("sucessoCadastrar", "Producto ingresado exitosamente!");
		return "redirect:/producto/listar/";
	}
	
	@GetMapping("/listar/")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("producto/listar");
		List<ProductoModel> lista = repository.findAll(); 
		mv.addObject("productos", lista);  
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value="id") int id){
		ModelAndView mv = new ModelAndView("producto/editar");
		Optional<ProductoModel> producto = repository.findById(id); 
		mv.addObject("id", producto.get().getId());
		mv.addObject("nome", producto.get().getNome());
		mv.addObject("preco", producto.get().getPreco());
		mv.addObject("categoria_id", producto.get().getCategoria().getId());
		List<CategoriaModel> categorias = categoriaRepository.findAll();
		mv.addObject("categorias", categorias);

		return mv;
	}

	@PostMapping("/editar/{id}")
		public String editarBD(@ModelAttribute @Valid ProductoDto productoDTO, 
		BindingResult result, RedirectAttributes msg, 
		@PathVariable(value="id") int id) {
		
		Optional<ProductoModel> producto = repository.findById(id);
		
		if(result.hasErrors()) {
			msg.addFlashAttribute("errorCadastrar", "Error al editar el producto");
			return "redirect:/producto/listar/";
		}
		var productoModel = producto.get();
		BeanUtils.copyProperties(productoDTO, productoModel);
		repository.save(productoModel);
		msg.addFlashAttribute("sucessoCadastrar", "Producto ingresado exitosamente!");
		return "redirect:../../producto/listar/";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") int id, RedirectAttributes msg){
	    Optional<ProductoModel> producto = repository.findById(id); 
	    if(producto.isEmpty()) {
	        msg.addFlashAttribute("errorEliminar", "Producto no encontrado");
	        return "redirect:/producto/listar/";
	    }
	    repository.deleteById(id);
	    msg.addFlashAttribute("sucessoEliminar", "Producto eliminado exitosamente!");
	    return "redirect:/producto/listar/";
	}
}
