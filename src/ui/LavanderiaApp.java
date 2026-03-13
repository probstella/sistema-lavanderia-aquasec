package ui;

import javax.swing.*;

public class LavanderiaApp {

    public static void main(String[] args) {

        SplashScreen splash = new SplashScreen();
        splash.mostrar();

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // aplica o tema personalizado
            TemaLavanderia.aplicarTema();

            Thread.sleep(2000);

        } catch (Exception e) {
            System.out.println("Erro ao aplicar tema: " + e.getMessage());
        }

        splash.fechar();

        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal().setVisible(true);
        });
    }
}