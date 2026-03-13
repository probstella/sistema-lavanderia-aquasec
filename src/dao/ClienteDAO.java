package dao;

import model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private static final String CLIENTES_FILE = "data/clientes.dat";
    
    public void salvarClientes(List<Cliente> clientes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CLIENTES_FILE))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Cliente> carregarClientes() {
        File file = new File(CLIENTES_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CLIENTES_FILE))) {
            return (List<Cliente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void adicionarCliente(Cliente cliente) {
        List<Cliente> clientes = carregarClientes();
        clientes.add(cliente);
        salvarClientes(clientes);
    }
    
    public void removerCliente(String id) {
        List<Cliente> clientes = carregarClientes();
        clientes.removeIf(c -> c.getId().equals(id));
        salvarClientes(clientes);
    }
    
    public Cliente buscarClientePorId(String id) {
        return carregarClientes().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public List<Cliente> listarClientes() {
        return carregarClientes();
    }
}