package service;

import dao.ClienteDAO;
import model.Cliente;

import java.util.List;
import java.util.UUID;

public class ClienteService {
    private ClienteDAO clienteDAO;
    
    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }
    
   public void cadastrarCliente(String nome, String telefone, String endereco, String email) {
    // Validações
    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome é obrigatório");
    }
    if (telefone == null || telefone.trim().isEmpty()) {
        throw new IllegalArgumentException("Telefone é obrigatório");
    }
    
    String id = "CLI" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    Cliente cliente = new Cliente(id, nome.trim(), telefone.trim(), 
                                  endereco != null ? endereco.trim() : "", 
                                  email != null ? email.trim() : "");
    clienteDAO.adicionarCliente(cliente);
}
    
    private String gerarIdUnico() {
        return "CLI" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public List<Cliente> listarClientes() {
        return clienteDAO.listarClientes();
    }
    
    public Cliente buscarCliente(String id) {
        return clienteDAO.buscarClientePorId(id);
    }
    
    public void removerCliente(String id) {
        clienteDAO.removerCliente(id);
    }
    
    public void atualizarCliente(Cliente cliente) {
        clienteDAO.removerCliente(cliente.getId());
        clienteDAO.adicionarCliente(cliente);
    }
}