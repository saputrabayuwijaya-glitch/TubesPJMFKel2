package com.tubespjmfkel2.view;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

public class WelcomeFrame extends JFrame {
    
    public WelcomeFrame() {
        super("Selamat Datang");

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Aplikasi Pencarian Rute Terpendek Menuju Bengkel Menggunakan Algoritma Dijkstra", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton btnImport = new JButton("Import CSV");
        JButton btnMasuk = new JButton("Masuk Ke Aplikasi");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnImport);
        buttonPanel.add(btnMasuk);

        add(title, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(1500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        btnMasuk.addActionListener(e -> {
            new MainFrame();
            dispose();
        });

        btnImport.addActionListener(e -> {
            String pathCsv = JOptionPane.showInputDialog("Masukkan path CSV:");

            if (pathCsv == null || pathCsv.isBlank()) return;

            try {
                MainFrame mainFrame = new MainFrame();

                boolean success = mainFrame.importCSV(pathCsv);

                if (!success) {
                    mainFrame.dispose();
                    JOptionPane.showMessageDialog(
                            this,
                            "Import CSV gagal! Periksa file dan coba lagi.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                dispose();

            } catch (Exception error) {
                JOptionPane.showMessageDialog(
                        this,
                        "Import gagal: " + error.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }


}
