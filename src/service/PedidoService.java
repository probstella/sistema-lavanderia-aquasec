package service;

import dao.PedidoDAO;
import model.Cliente;
import model.Pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PedidoService {
    private PedidoDAO pedidoDAO;
    
    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
    }
    
    public void criarPedido(Cliente cliente, LocalDate dataEntrega, boolean entregaEmPorta, 
                           String descricao, double valor) {
        Pedido pedido = new Pedido(cliente, dataEntrega, entregaEmPorta, descricao, valor);
        pedidoDAO.adicionarPedido(pedido);
    }
    
    public List<Pedido> listarPedidosPendentes() {
        return pedidoDAO.getPedidosPendentes();
    }
    
    public void finalizarPedido(String codigo) {
        pedidoDAO.finalizarPedido(codigo);
    }
    
    public void limparHistorico() {
        pedidoDAO.limparHistoricoAntigo();
    }
    
    public List<Pedido> visualizarHistorico() {
        return pedidoDAO.carregarHistorico();
    }
    
    public void cancelarPedido(String codigo) {
        pedidoDAO.removerPedido(codigo);
    }
}