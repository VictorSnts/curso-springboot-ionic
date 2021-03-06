package com.victor.spring.modeloconceitual.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.victor.spring.modeloconceitual.domain.Produto;
import com.victor.spring.modeloconceitual.dto.ProdutoDTO;
import com.victor.spring.modeloconceitual.resource.utils.Url;
import com.victor.spring.modeloconceitual.services.ProdutoService;

@RestController
@RequestMapping(value = "/produto")
public class ProdutoResource {

	@Autowired
	private ProdutoService produtoService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> buscar(@PathVariable Integer id) {

		Produto produto = produtoService.buscar(id);
		return ResponseEntity.ok().body(produto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> listarPage(
			@RequestParam(value="nome", defaultValue = "") String nome, 
			@RequestParam(value="categorias", defaultValue = "") String categorias, 
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue = "nome") String orderBy,  
			@RequestParam(value="direction", defaultValue = "ASC") String direction
			) {
		String nomeDecode = Url.decodeParam(nome);
		List<Integer> listCategorias = Url.convertToIntegerList(categorias);
		
		Page<Produto> produtos = produtoService.search(nomeDecode, listCategorias, page, linesPerPage, orderBy, direction);
		
		Page<ProdutoDTO> produtoDTO = produtos.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(produtoDTO);
	}
}
