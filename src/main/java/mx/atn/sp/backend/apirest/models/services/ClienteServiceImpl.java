package mx.atn.sp.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.atn.sp.backend.apirest.models.dao.IClienteDao;
import mx.atn.sp.backend.apirest.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService{
	
	@Autowired
	private IClienteDao clienteDao;

	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Transactional(readOnly = true) 
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);		//retorna un Optional  (Spring 5);
	}

	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

}
