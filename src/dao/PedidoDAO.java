package dao;

import model.Pedido;
import model.StatusPedido;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDAO {
    private static final String PEDIDOS_FILE = "data/pedidos.dat";
    private static final String HISTORICO_FILE = "data/historico.dat";
    
    public void salvarPedidos(List<Pedido> pedidos) {
        salvarArquivo(pedidos, PEDIDOS_FILE);
    }
    
    public List<Pedido> carregarPedidos() {
        return carregarArquivo(PEDIDOS_FILE);
    }
    
    public void salvarHistorico(List<Pedido> historico) {
        salvarArquivo(historico, HISTORICO_FILE);
    }
    
    public List<Pedido> carregarHistorico() {
        return carregarArquivo(HISTORICO_FILE);
    }
    
    private void salvarArquivo(List<Pedido> dados, String arquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(dados);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<Pedido> carregarArquivo(String arquivo) {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Pedido>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void adicionarPedido(Pedido pedido) {
        List<Pedido> pedidos = carregarPedidos();
        pedidos.add(pedido);
        salvarPedidos(pedidos);
    }
    
    public void removerPedido(String codigo) {
        List<Pedido> pedidos = carregarPedidos();
        pedidos.removeIf(p -> p.getCodigo().equals(codigo));
        salvarPedidos(pedidos);
    }
    
    public void finalizarPedido(String codigo) {
        List<Pedido> pedidos = carregarPedidos();
        List<Pedido> historico = carregarHistorico();
        
        pedidos.stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst()
                .ifPresent(pedido -> {
                    pedido.finalizar();
                    historico.add(pedido);
                    pedidos.remove(pedido);
                });
        
        salvarPedidos(pedidos);
        salvarHistorico(historico);
    }
    
    public List<Pedido> getPedidosPendentes() {
        return carregarPedidos().stream()
                .filter(p -> p.getStatus() == StatusPedido.PENDENTE)
                .sorted((p1, p2) -> p1.getDataEntrega().compareTo(p2.getDataEntrega()))
                .collect(Collectors.toList());
    }
    
    public void limparHistoricoAntigo() {
        List<Pedido> historico = carregarHistorico();
        historico.removeIf(Pedido::deveSerRemovido);
        salvarHistorico(historico);
    }
}