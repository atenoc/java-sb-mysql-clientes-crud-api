package mx.atn.sp.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import mx.atn.sp.backend.apirest.models.entity.Cliente;
import mx.atn.sp.backend.apirest.models.services.IClienteService;

@CrossOrigin (origins = {"http://localhost:4200"})    //origen del dominio que puede enviar y recibir datos (Angular)
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	IClienteService clienteservice;
	
	@GetMapping("/clientes")
	public List<Cliente> index(){
		return clienteservice.findAll();
	}
	
	/*
	@GetMapping("/clientes/{id}")					//2. Recuperamos el id de la URL
	public Cliente show(@PathVariable Long id){		//1. Con PathVariable contenemos el id 
		Cliente c = clienteservice.findById(id);
		return clienteservice.findById(id);
	}*/												//Retorna un OK (200) por default

	/* Con manejo de errores */
	@GetMapping("/clientes/{id}")					
	public ResponseEntity<?> show(@PathVariable Long id){	//Entidad Generica
		
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = clienteservice.findById(id);
		} catch (DataAccessException dae) {
			response.put("mensaje", "Error al realizar la consulta en la Base de Datos");
			response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mensaje","El id: ".concat(id.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	/*
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)							//Retorna valor 201
	public Cliente create(@RequestBody Cliente cliente) {		//Con RequestBody mapea el Json al objeto Cliente
		return clienteservice.save(cliente);
	}*/
	
	/* Manejo de Errores*/
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)							
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {	
		
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			clienteNew = clienteservice.save(cliente);
		}catch (DataAccessException dae) {
			response.put("mensaje", "Error al insertar en la Base de Datos");
			response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido creado con éxito");
		response.put("cliente", clienteNew);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/*
	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente clienteActual = clienteservice.findById(id); 	//Buscamos el cliente a la BD
		
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setEmail(cliente.getEmail());
		clienteActual.setNombre(cliente.getNombre());
		return clienteservice.save(clienteActual); 				//Realiza un merge en vez de un insert
	}*/
	
	/* Con manejo de errores */
	@PutMapping("/clientes/{id}")
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		
		Cliente clienteActual = clienteservice.findById(id);
		Cliente clienteUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		if(clienteActual == null) {
			response.put("mensaje","Error: no se puede editar, el id: ".concat(id.toString().concat(" no existe en la Base de Datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setCreateAt(cliente.getCreateAt());
			
			clienteUpdated = clienteservice.save(clienteActual); 	
		
		}catch (DataAccessException dae) {
			response.put("mensaje", "Error al actualizar en la Base de Datos");
			response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con éxito");
		response.put("cliente", clienteUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);	
		
	}
	
	/*
	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)						//Retorna
	public void delete(@PathVariable Long id) {
		clienteservice.delete(id);
	}*/
	
	/* Manejo de Errores */
	@DeleteMapping("/clientes/{id}")
	//@ResponseStatus(HttpStatus.NO_CONTENT)						//Retorna
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		try {
			//No es necesario validar por cliente
			clienteservice.delete(id); 		//A traves del crud repository Spring Data valida que el id existe
		} catch (DataAccessException dae) {
			response.put("mensaje", "Error al eliminar en la Base de Datos");
			response.put("error", dae.getMessage().concat(": ").concat(dae.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido eliminado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);	
		
	}
	
}
