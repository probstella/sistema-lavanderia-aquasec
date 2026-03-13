package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen() {
        Color roxoPrincipal = new Color(164, 76, 164);
        Color branco = Color.WHITE;
        
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(branco);
        painel.setBorder(BorderFactory.createLineBorder(roxoPrincipal, 3));
        
        JPanel painelSuperior = new JPanel(new GridBagLayout());
        painelSuperior.setBackground(roxoPrincipal);
        painelSuperior.setPreferredSize(new Dimension(400, 80));
        
        JLabel titulo = new JLabel("LAVANDERIA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(branco);
        painelSuperior.add(titulo);
        
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(branco);
        painelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        
        JLabel icone = new JLabel("🧺");
        icone.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        painelCentral.add(icone, gbc);
        
        gbc.gridy = 1;
        JLabel subtitulo = new JLabel("Sistema de Gestão");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitulo.setForeground(Color.GRAY);
        painelCentral.add(subtitulo, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setBackground(new Color(230, 242, 248));
        progressBar.setForeground(roxoPrincipal);
        progressBar.setPreferredSize(new Dimension(350, 20));
        painelCentral.add(progressBar, gbc);
        
        painel.add(painelSuperior, BorderLayout.NORTH);
        painel.add(painelCentral, BorderLayout.CENTER);
        
        add(painel);
        setSize(400, 350);
        setLocationRelativeTo(null);
    }
    
    public void mostrar() {
        setVisible(true);
    }
    
    public void fechar() {
        setVisible(false);
        dispose();
    }
}