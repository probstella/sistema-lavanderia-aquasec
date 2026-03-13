package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class TemaLavanderia {

    // Roxos
    public static final Color ROXO_PRINCIPAL = new Color(164, 76, 164);
    public static final Color ROXO_HOVER = new Color(144, 56, 144);
    public static final Color ROXO_CLARO = new Color(218, 162, 218);
    public static final Color ROXO_SUAVE = new Color(237, 221, 237);
    
    // Azuis
    public static final Color AZUL_BEBE = new Color(173, 216, 230);
    public static final Color AZUL_BEBE_SUAVE = new Color(230, 242, 248);
    
    // Brancos e cinzas
    public static final Color BRANCO = Color.WHITE;
    public static final Color BRANCO_GELO = new Color(248, 250, 252);
    public static final Color CINZA_LINHA = new Color(240, 240, 240);
    
    // Alertas
    public static final Color VERMELHO_ALERTA = new Color(244, 67, 54);
    
    // Textos
    public static final Color TEXTO_PRINCIPAL = new Color(33, 33, 33);
    public static final Color TEXTO_SECUNDARIO = new Color(117, 117, 117);
    
    // Fontes
    public static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONTE_NORMAL_NEGRITO = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);

    public static void aplicarTema() {
        // APENAS cores de fundo - NADA de botões, tabelas ou cabeçalhos aqui!
        UIManager.put("Panel.background", BRANCO_GELO);
        UIManager.put("TabbedPane.background", BRANCO_GELO);
        UIManager.put("OptionPane.background", BRANCO_GELO);
        
        // NÃO configurar menus aqui - já está sendo feito no TelaPrincipal
        // NÃO configurar botões aqui - já está sendo feito no TelaPrincipal
        // NÃO configurar tabelas aqui - já está sendo feito no TelaPrincipal
    }

    public static void estilizarTabela(JTable tabela) {

    // Estilo das linhas
    tabela.setFont(FONTE_NORMAL);
    tabela.setRowHeight(30);
    tabela.setGridColor(ROXO_CLARO);
    tabela.setSelectionBackground(ROXO_SUAVE);
    tabela.setSelectionForeground(TEXTO_PRINCIPAL);

    JTableHeader header = tabela.getTableHeader();

    // Renderer personalizado do cabeçalho
    header.setDefaultRenderer(new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            JLabel label = new JLabel(value.toString());

            label.setOpaque(true);
            label.setBackground(ROXO_PRINCIPAL);
            label.setForeground(BRANCO);
            label.setFont(FONTE_NORMAL_NEGRITO);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

            return label;
        }
    });
}
}