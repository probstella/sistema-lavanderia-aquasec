package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Random;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigo;
    private Cliente cliente;
    private LocalDateTime dataPedido;
    private LocalDate dataEntrega;
    private boolean entregaEmPorta;
    private StatusPedido status;
    private String descricao;
    private double valor;
    private LocalDateTime dataFinalizacao;
    
    public Pedido(Cliente cliente, LocalDate dataEntrega, boolean entregaEmPorta, 
                  String descricao, double valor) {
        this.codigo = gerarCodigoUnico();
        this.cliente = cliente;
        this.dataPedido = LocalDateTime.now();
        this.dataEntrega = dataEntrega;
        this.entregaEmPorta = entregaEmPorta;
        this.descricao = descricao;
        this.valor = valor;
        this.status = StatusPedido.PENDENTE;
    }
    
    private String gerarCodigoUnico() {
        Random random = new Random();
        int numero = random.nextInt(900) + 100; // Gera números de 100 a 999 (3 dígitos)
        return String.valueOf(numero);
    }
    
    public boolean isAtrasado() {
        if (status == StatusPedido.PENDENTE) {
            return LocalDate.now().isAfter(dataEntrega);
        }
        return false;
    }
    
    public boolean deveSerRemovido() {
        if (status == StatusPedido.FINALIZADO && dataFinalizacao != null) {
            return ChronoUnit.DAYS.between(dataFinalizacao.toLocalDate(), LocalDate.now()) >= 7;
        }
        return false;
    }
    
    public void finalizar() {
        this.status = StatusPedido.FINALIZADO;
        this.dataFinalizacao = LocalDateTime.now();
    }
    
    // Getters e Setters
    public String getCodigo() { 
        return codigo; 
    }
    
    public Cliente getCliente() { 
        return cliente; 
    }
    
    public void setCliente(Cliente cliente) { 
        this.cliente = cliente; 
    }
    
    public LocalDateTime getDataPedido() { 
        return dataPedido; 
    }
    
    public LocalDate getDataEntrega() { 
        return dataEntrega; 
    }
    
    public void setDataEntrega(LocalDate dataEntrega) { 
        this.dataEntrega = dataEntrega; 
    }
    
    public boolean isEntregaEmPorta() { 
        return entregaEmPorta; 
    }
    
    public void setEntregaEmPorta(boolean entregaEmPorta) { 
        this.entregaEmPorta = entregaEmPorta; 
    }
    
    public StatusPedido getStatus() { 
        return status; 
    }
    
    public void setStatus(StatusPedido status) { 
        this.status = status; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
    
    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }
    
    public double getValor() { 
        return valor; 
    }
    
    public void setValor(double valor) { 
        this.valor = valor; 
    }
    
    public LocalDateTime getDataFinalizacao() { 
        return dataFinalizacao; 
    }
    
    @Override
    public String toString() {
        return String.format("Pedido{codigo='%s', cliente=%s, dataEntrega=%s, entregaEmPorta=%s, status=%s}", 
                codigo, cliente != null ? cliente.getNome() : "Sem cliente", dataEntrega, entregaEmPorta, status);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(codigo, pedido.codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}