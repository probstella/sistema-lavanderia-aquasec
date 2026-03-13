package ui;

import ui.TemaLavanderia;
import model.Pedido;
import model.Cliente;
import service.PedidoService;
import service.ClienteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaPrincipal extends JFrame {
    private PedidoService pedidoService;
    private ClienteService clienteService;
    private JTable tabelaPendentes;
    private DefaultTableModel modeloTabelaPendentes;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabelaClientes;
    private JLabel labelContadorPendentes;
    private JLabel labelContadorAtrasados;
    private Timer timerAtualizacao;
    private DateTimeFormatter dateFormatter;
    
    public TelaPrincipal() {
        this.pedidoService = new PedidoService();
        this.clienteService = new ClienteService();
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        pedidoService.limparHistorico();
        limparDadosInvalidos();
        
        initComponents();
        configurarJanela();
        carregarDados();
        iniciarAtualizacaoAutomatica();
    }
    
    private void limparDadosInvalidos() {
        List<Pedido> pendentes = pedidoService.listarPedidosPendentes();
        for (Pedido p : pendentes) {
            if (p.getCliente() == null) {
                pedidoService.cancelarPedido(p.getCodigo());
            }
        }
    }
    
    private void initComponents() {
        setTitle("Sistema de Lavanderia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JMenuBar menuBar = criarMenuBar();
        setJMenuBar(menuBar);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        tabbedPane.addTab("Pedidos Pendentes", criarPainelPendentes());
        tabbedPane.addTab("Clientes", criarPainelClientes());
        tabbedPane.addTab("Histórico", criarPainelHistorico());
        tabbedPane.addTab("Novo Pedido", criarPainelNovoPedido());
        tabbedPane.addTab("Novo Cliente", criarPainelNovoCliente());
        
        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelStatus(), BorderLayout.SOUTH);
    }
    
    private JMenuBar criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(164, 76, 164));
        
        JMenu menuArquivo = new JMenu("Arquivo");
        menuArquivo.setForeground(Color.WHITE);
        menuArquivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(itemSair);
        
        JMenu menuAcoes = new JMenu("Ações");
        menuAcoes.setForeground(Color.WHITE);
        menuAcoes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JMenuItem itemNovoPedido = new JMenuItem("Novo Pedido");
        itemNovoPedido.addActionListener(e -> abrirDialogoNovoPedido());
        
        JMenuItem itemNovoCliente = new JMenuItem("Novo Cliente");
        itemNovoCliente.addActionListener(e -> abrirDialogoNovoCliente());
        
        JMenuItem itemFinalizarPedido = new JMenuItem("Finalizar Pedido");
        itemFinalizarPedido.addActionListener(e -> abrirDialogoFinalizarPedido());
        
        menuAcoes.add(itemNovoPedido);
        menuAcoes.add(itemNovoCliente);
        menuAcoes.addSeparator();
        menuAcoes.add(itemFinalizarPedido);
        
        JMenu menuAjuda = new JMenu("Ajuda");
        menuAjuda.setForeground(Color.WHITE);
        menuAjuda.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> mostrarSobre());
        menuAjuda.add(itemSobre);
        
        menuBar.add(menuArquivo);
        menuBar.add(menuAcoes);
        menuBar.add(menuAjuda);
        
        return menuBar;
    }
    
    private JButton criarBotao(String texto) {
    JButton botao = new JButton(texto);

    botao.setBackground(new Color(164, 76, 164));
    botao.setForeground(Color.WHITE);

    botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
    botao.setFocusPainted(false);

    // ESSAS LINHAS SÃO O QUE FAZ FUNCIONAR
    botao.setContentAreaFilled(false);
    botao.setOpaque(true);
    botao.setBorderPainted(false);

    botao.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

    botao.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            botao.setBackground(new Color(144, 56, 144));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            botao.setBackground(new Color(164, 76, 164));
        }
    });

    return botao;
}
    
    private JButton criarBotaoSecundario(String texto) {
    JButton botao = new JButton(texto);

    botao.setBackground(new Color(164, 76, 164));
    botao.setForeground(Color.WHITE);
    botao.setFont(new Font("Segoe UI", Font.BOLD, 12));

    botao.setFocusPainted(false);
    botao.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

    botao.setOpaque(true);
    botao.setContentAreaFilled(false);

    return botao;
}
    
    private JPanel criarPainelPendentes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(new Color(248, 250, 252));
        
        JLabel titulo = new JLabel("PEDIDOS PENDENTES");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(164, 76, 164));
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        painel.add(titulo, BorderLayout.NORTH);
        
        String[] colunas = {"Código", "Cliente", "Data Entrega", "Entrega", "Status", "Valor", "Descrição"};
        modeloTabelaPendentes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaPendentes = new JTable(modeloTabelaPendentes);
        tabelaPendentes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaPendentes.setRowHeight(30);

        TemaLavanderia.estilizarTabela(tabelaPendentes);
        
        tabelaPendentes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaPendentes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaPendentes.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabelaPendentes.getColumnModel().getColumn(3).setPreferredWidth(70);
        tabelaPendentes.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabelaPendentes.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabelaPendentes.getColumnModel().getColumn(6).setPreferredWidth(250);
        
        tabelaPendentes.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = tabelaPendentes.rowAtPoint(e.getPoint());
                int col = tabelaPendentes.columnAtPoint(e.getPoint());
                if (row > -1 && col == 6) {
                    String descricao = (String) tabelaPendentes.getValueAt(row, col);
                    tabelaPendentes.setToolTipText(descricao);
                } else {
                    tabelaPendentes.setToolTipText(null);
                }
            }
        });
        
        tabelaPendentes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabelaPendentes.rowAtPoint(e.getPoint());
                    if (row > -1) {
                        String codigo = (String) tabelaPendentes.getValueAt(row, 0);
                        mostrarDetalhesPedido(codigo);
                    }
                }
            }
        });
        
        tabelaPendentes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String codigo = (String) table.getValueAt(row, 0);
                List<Pedido> pedidos = pedidoService.listarPedidosPendentes();
                
                for (Pedido p : pedidos) {
                    if (p.getCodigo().equals(codigo)) {
                        if (!isSelected) {
                            if (p.isAtrasado()) {
                                c.setBackground(new Color(244, 67, 54));
                                c.setForeground(Color.WHITE);
                            } else {
                                c.setBackground(Color.WHITE);
                                c.setForeground(new Color(33, 33, 33));
                            }
                        } else {
                            c.setBackground(new Color(237, 221, 237));
                            c.setForeground(new Color(33, 33, 33));
                        }
                        break;
                    }
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabelaPendentes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(218, 162, 218)));
        painel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(new Color(248, 250, 252));
        
        JButton btnDetalhes = criarBotao("VER DETALHES");
        btnDetalhes.addActionListener(e -> {
            int linha = tabelaPendentes.getSelectedRow();
            if (linha != -1) {
                String codigo = (String) tabelaPendentes.getValueAt(linha, 0);
                mostrarDetalhesPedido(codigo);
            } else {
                JOptionPane.showMessageDialog(painel, "Selecione um pedido para ver os detalhes!");
            }
        });
        
        JButton btnFinalizar = criarBotao("FINALIZAR PEDIDO");
        btnFinalizar.addActionListener(e -> finalizarPedidoSelecionado());
        
        JButton btnAtualizar = criarBotao("ATUALIZAR");
        btnAtualizar.addActionListener(e -> carregarPedidosPendentes());
        
        painelBotoes.add(btnDetalhes);
        painelBotoes.add(btnFinalizar);
        painelBotoes.add(btnAtualizar);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private void mostrarDetalhesPedido(String codigo) {
        List<Pedido> pendentes = pedidoService.listarPedidosPendentes();
        
        for (Pedido p : pendentes) {
            if (p.getCodigo().equals(codigo) && p.getCliente() != null) {
                String mensagem = String.format(
                    "DETALHES DO PEDIDO %s\n\n" +
                    "Cliente: %s\n" +
                    "Telefone: %s\n" +
                    "Data do Pedido: %s\n" +
                    "Data de Entrega: %s\n" +
                    "Entrega em Porta: %s\n" +
                    "Status: %s\n" +
                    "Valor: R$ %.2f\n\n" +
                    "DESCRIÇÃO:\n%s",
                    p.getCodigo(),
                    p.getCliente().getNome(),
                    p.getCliente().getTelefone(),
                    p.getDataPedido().format(dateFormatter),
                    p.getDataEntrega().format(dateFormatter),
                    p.isEntregaEmPorta() ? "Sim" : "Não",
                    p.isAtrasado() ? "ATRASADO" : "Pendente",
                    p.getValor(),
                    p.getDescricao() != null ? p.getDescricao() : "Sem descrição"
                );
                
                JTextArea textArea = new JTextArea(mensagem);
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                
                JOptionPane.showMessageDialog(this, scrollPane, 
                    "Detalhes do Pedido", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        
        JOptionPane.showMessageDialog(this, "Pedido não encontrado!", 
            "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private JPanel criarPainelClientes() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(new Color(248, 250, 252));
        
        JLabel titulo = new JLabel("CLIENTES CADASTRADOS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(164, 76, 164));
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        painel.add(titulo, BorderLayout.NORTH);
        
        String[] colunas = {"ID", "Nome", "Telefone", "Email", "Endereço"};
        modeloTabelaClientes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaClientes = new JTable(modeloTabelaClientes);
        tabelaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaClientes.setRowHeight(30);

        TemaLavanderia.estilizarTabela(tabelaClientes);
        
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(218, 162, 218)));
        painel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(new Color(248, 250, 252));
        
        JButton btnRemover = criarBotao("REMOVER CLIENTE");
        btnRemover.addActionListener(e -> removerClienteSelecionado());
        
        JButton btnAtualizar = criarBotao("ATUALIZAR");
        btnAtualizar.addActionListener(e -> carregarClientes());
        
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAtualizar);
        
        painel.add(painelBotoes, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelHistorico() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));
        painel.setBackground(new Color(248, 250, 252));
        
        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBackground(new Color(248, 250, 252));
        
        JLabel titulo = new JLabel("HISTÓRICO DE PEDIDOS");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(164, 76, 164));
        
        JButton btnAtualizar = criarBotao("ATUALIZAR");
        btnAtualizar.addActionListener(e -> {
            JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
            tabbedPane.setComponentAt(2, criarPainelHistorico());
        });
        
        painelTitulo.add(titulo, BorderLayout.WEST);
        painelTitulo.add(btnAtualizar, BorderLayout.EAST);
        painelTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        painel.add(painelTitulo, BorderLayout.NORTH);
        
        String[] colunas = {"Código", "Cliente", "Data Finalização", "Valor", "Entrega", "Descrição"};
        DefaultTableModel modeloTabelaHistorico = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tabelaHistorico = new JTable(modeloTabelaHistorico);
        tabelaHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaHistorico.setRowHeight(30);
        tabelaHistorico.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaHistorico.getTableHeader().setBackground(new Color(164, 76, 164));
        tabelaHistorico.getTableHeader().setForeground(Color.WHITE);
        TemaLavanderia.estilizarTabela(tabelaHistorico);
        
        List<Pedido> historico = pedidoService.visualizarHistorico();
        for (Pedido p : historico) {
            if (p.getCliente() != null) {
                modeloTabelaHistorico.addRow(new Object[]{
                    p.getCodigo(),
                    p.getCliente().getNome(),
                    p.getDataFinalizacao() != null ? 
                        p.getDataFinalizacao().format(dateFormatter) : "N/A",
                    String.format("R$ %.2f", p.getValor()),
                    p.isEntregaEmPorta() ? "Sim" : "Não",
                    p.getDescricao() != null ? p.getDescricao() : "Sem descrição"
                });
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(tabelaHistorico);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(218, 162, 218)));
        painel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel info = new JLabel("Pedidos são automaticamente removidos após 7 dias");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(new Color(117, 117, 117));
        info.setBorder(new EmptyBorder(5, 0, 0, 0));
        painel.add(info, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelNovoPedido() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(248, 250, 252));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titulo = new JLabel("NOVO PEDIDO");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(164, 76, 164));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painel.add(titulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        painel.add(new JLabel("Cliente:"), gbc);
        
        JComboBox<Cliente> comboClientes = new JComboBox<>();
        carregarClientesNoComboBox(comboClientes);
        comboClientes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JButton btnAtualizarClientes = criarBotao("ATUALIZAR");
        btnAtualizarClientes.setBackground(new Color(164, 76, 164));
        btnAtualizarClientes.setForeground(Color.WHITE);
        btnAtualizarClientes.setFocusPainted(false);
        btnAtualizarClientes.setToolTipText("Atualizar lista de clientes");
        btnAtualizarClientes.addActionListener(e -> carregarClientesNoComboBox(comboClientes));
        
        JPanel painelCliente = new JPanel(new BorderLayout(5, 0));
        painelCliente.setBackground(new Color(248, 250, 252));
        painelCliente.add(comboClientes, BorderLayout.CENTER);
        painelCliente.add(btnAtualizarClientes, BorderLayout.EAST);
        
        gbc.gridx = 1;
        painel.add(painelCliente, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        painel.add(new JLabel("Data de Entrega:"), gbc);
        
        JTextField txtDataEntrega = new JTextField(10);
        txtDataEntrega.setText(LocalDate.now().plusDays(3).format(dateFormatter));
        txtDataEntrega.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        painel.add(txtDataEntrega, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        painel.add(new JLabel("Entrega em Porta:"), gbc);
        
        JCheckBox chkEntregaPorta = new JCheckBox();
        chkEntregaPorta.setBackground(new Color(248, 250, 252));
        gbc.gridx = 1;
        painel.add(chkEntregaPorta, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        painel.add(new JLabel("Descrição:"), gbc);
        
        JTextArea txtDescricao = new JTextArea(3, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        gbc.gridx = 1;
        painel.add(scrollDescricao, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        painel.add(new JLabel("Valor (R$):"), gbc);
        
        JTextField txtValor = new JTextField(10);
        txtValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        painel.add(txtValor, gbc);
        
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton btnSalvar = criarBotao("SALVAR PEDIDO");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(164, 76, 164));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(new Color(144, 56, 144));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(new Color(164, 76, 164));
            }
        });
        
        btnSalvar.addActionListener(e -> {
            try {
                if (comboClientes.getItemCount() == 0 || comboClientes.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(painel, 
                        "Nenhum cliente cadastrado! Cadastre um cliente primeiro.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Cliente cliente = (Cliente) comboClientes.getSelectedItem();
                LocalDate dataEntrega = LocalDate.parse(txtDataEntrega.getText(), dateFormatter);
                boolean entregaPorta = chkEntregaPorta.isSelected();
                String descricao = txtDescricao.getText();
                double valor = Double.parseDouble(txtValor.getText());
                
                pedidoService.criarPedido(cliente, dataEntrega, entregaPorta, descricao, valor);
                
                JOptionPane.showMessageDialog(painel, "Pedido criado com sucesso!");
                
                txtDataEntrega.setText(LocalDate.now().plusDays(3).format(dateFormatter));
                chkEntregaPorta.setSelected(false);
                txtDescricao.setText("");
                txtValor.setText("");
                
                carregarPedidosPendentes();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro ao criar pedido: " + ex.getMessage(), 
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        painel.add(btnSalvar, gbc);
        
        return painel;
    }
    
    private void carregarClientesNoComboBox(JComboBox<Cliente> comboClientes) {
        comboClientes.removeAllItems();
        List<Cliente> clientes = clienteService.listarClientes();
        
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }
    }
    
    private JPanel criarPainelNovoCliente() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(new Color(248, 250, 252));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titulo = new JLabel("NOVO CLIENTE");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(164, 76, 164));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painel.add(titulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        painel.add(new JLabel("Nome:"), gbc);
        
        JTextField txtNome = new JTextField(20);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        painel.add(txtNome, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        painel.add(new JLabel("Telefone:"), gbc);
        
        JTextField txtTelefone = new JTextField(15);
        txtTelefone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        painel.add(txtTelefone, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        painel.add(new JLabel("Email:"), gbc);
        
        JTextField txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        painel.add(txtEmail, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        painel.add(new JLabel("Endereço:"), gbc);
        
        JTextField txtEndereco = new JTextField(30);
        txtEndereco.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        painel.add(txtEndereco, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton btnSalvar = criarBotao("SALVAR CLIENTE");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(164, 76, 164));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(new Color(144, 56, 144));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(new Color(164, 76, 164));
            }
        });
        
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            String endereco = txtEndereco.getText();
            
            if (nome.isEmpty() || telefone.isEmpty()) {
                JOptionPane.showMessageDialog(painel, "Nome e telefone são obrigatórios!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            clienteService.cadastrarCliente(nome, telefone, endereco, email);
            
            JOptionPane.showMessageDialog(painel, "Cliente cadastrado com sucesso!");
            
            txtNome.setText("");
            txtTelefone.setText("");
            txtEmail.setText("");
            txtEndereco.setText("");
            
            carregarClientes();
        });
        
        painel.add(btnSalvar, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelStatus() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setBorder(BorderFactory.createEtchedBorder());
        painel.setBackground(new Color(230, 242, 248));
        
        labelContadorPendentes = new JLabel("Pendentes: 0");
        labelContadorPendentes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelContadorPendentes.setForeground(new Color(33, 33, 33));
        
        labelContadorAtrasados = new JLabel("Atrasados: 0");
        labelContadorAtrasados.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelContadorAtrasados.setForeground(new Color(244, 67, 54));
        
        JLabel labelData = new JLabel(LocalDate.now().format(dateFormatter));
        labelData.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelData.setForeground(new Color(117, 117, 117));
        
        painel.add(labelContadorPendentes);
        painel.add(new JSeparator(SwingConstants.VERTICAL));
        painel.add(labelContadorAtrasados);
        painel.add(new JSeparator(SwingConstants.VERTICAL));
        painel.add(labelData);
        
        return painel;
    }
    
    private void configurarJanela() {
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pedidoService.limparHistorico();
            }
        });
    }
    
    private void carregarDados() {
        carregarPedidosPendentes();
        carregarClientes();
        atualizarContadores();
    }
    
    private void carregarPedidosPendentes() {
        modeloTabelaPendentes.setRowCount(0);
        
        List<Pedido> pendentes = pedidoService.listarPedidosPendentes();
        
        for (Pedido p : pendentes) {
            if (p.getCliente() != null) {
                modeloTabelaPendentes.addRow(new Object[]{
                    p.getCodigo(),
                    p.getCliente().getNome(),
                    p.getDataEntrega().format(dateFormatter),
                    p.isEntregaEmPorta() ? "Sim" : "Não",
                    p.isAtrasado() ? "ATRASADO" : "PENDENTE",
                    String.format("R$ %.2f", p.getValor()),
                    truncarDescricao(p.getDescricao(), 30)
                });
            }
        }
        
        atualizarContadores();
    }
    
    private String truncarDescricao(String descricao, int tamanho) {
        if (descricao == null || descricao.isEmpty()) {
            return "Sem descrição";
        }
        if (descricao.length() <= tamanho) {
            return descricao;
        }
        return descricao.substring(0, tamanho) + "...";
    }
    
    private void carregarClientes() {
        modeloTabelaClientes.setRowCount(0);
        
        List<Cliente> clientes = clienteService.listarClientes();
        for (Cliente c : clientes) {
            modeloTabelaClientes.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getTelefone(),
                c.getEmail() != null ? c.getEmail() : "",
                c.getEndereco() != null ? c.getEndereco() : ""
            });
        }
    }
    
    private void atualizarContadores() {
        List<Pedido> pendentes = pedidoService.listarPedidosPendentes();
        long atrasados = pendentes.stream().filter(Pedido::isAtrasado).count();
        
        labelContadorPendentes.setText("Pendentes: " + pendentes.size());
        labelContadorAtrasados.setText("Atrasados: " + atrasados);
    }
    
    private void iniciarAtualizacaoAutomatica() {
        timerAtualizacao = new Timer(60000, e -> {
            if (isVisible()) {
                SwingUtilities.invokeLater(() -> {
                    carregarPedidosPendentes();
                });
            }
        });
        timerAtualizacao.start();
    }
    
    private void finalizarPedidoSelecionado() {
        int linha = tabelaPendentes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido para finalizar!", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = (String) tabelaPendentes.getValueAt(linha, 0);
        String cliente = (String) tabelaPendentes.getValueAt(linha, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente finalizar o pedido " + codigo + " de " + cliente + "?", 
                "Confirmar Finalização", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            pedidoService.finalizarPedido(codigo);
            carregarPedidosPendentes();
            
            JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
            tabbedPane.setComponentAt(2, criarPainelHistorico());
            
            JOptionPane.showMessageDialog(this, "Pedido finalizado com sucesso!");
        }
    }
    
    private void removerClienteSelecionado() {
        int linha = tabelaClientes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para remover!", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) tabelaClientes.getValueAt(linha, 0);
        String nome = (String) tabelaClientes.getValueAt(linha, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente remover o cliente " + nome + "?\n" +
                "Esta ação não pode ser desfeita!", 
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            clienteService.removerCliente(id);
            carregarClientes();
            JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
        }
    }
    
    private void abrirDialogoNovoPedido() {
        JDialog dialog = new JDialog(this, "Novo Pedido", true);
        dialog.setContentPane(criarPainelNovoPedido());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void abrirDialogoNovoCliente() {
        JDialog dialog = new JDialog(this, "Novo Cliente", true);
        dialog.setContentPane(criarPainelNovoCliente());
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void abrirDialogoFinalizarPedido() {
        String codigo = JOptionPane.showInputDialog(this, 
                "Digite o código do pedido a finalizar:", 
                "Finalizar Pedido", JOptionPane.QUESTION_MESSAGE);
        
        if (codigo != null && !codigo.trim().isEmpty()) {
            pedidoService.finalizarPedido(codigo);
            carregarPedidosPendentes();
            
            JTabbedPane tabbedPane = (JTabbedPane) getContentPane().getComponent(0);
            tabbedPane.setComponentAt(2, criarPainelHistorico());
            
            JOptionPane.showMessageDialog(this, "Pedido finalizado com sucesso!");
        }
    }
    
    private void mostrarSobre() {
        JOptionPane.showMessageDialog(this, 
                "Sistema de Lavanderia\nVersão 1.0\n\n" +
                "Funcionalidades:\n" +
                "✓ Pedidos pendentes com alerta de atraso\n" +
                "✓ Visualização de descrição dos pedidos\n" +
                "✓ Histórico com auto-exclusão em 7 dias\n" +
                "✓ Cadastro completo de clientes\n" +
                "✓ Salvamento automático", 
                "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }
}