package com.tubespjmfkel2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        super("Selamat Datang");
        setSize(1100, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel utama dengan gradient background
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        add(mainPanel);

        // ====== Panel Box Judul ======
        JPanel titleBox = new RoundedPanel(30);
        titleBox.setBackground(new Color(255, 255, 255, 210)); // semi-transparent white
        titleBox.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        titleBox.setLayout(new BorderLayout());

        JLabel title = new JLabel(
                "<html><center>Aplikasi Pencarian Rute Terpendek Menuju Bengkel<br>"
                        + "Menggunakan Algoritma Dijkstra</center></html>",
                SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(40, 40, 40));

        titleBox.add(title, BorderLayout.CENTER);
        mainPanel.add(titleBox, BorderLayout.NORTH);

        // ====== Panel Tombol ======
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(60, 10, 60, 10));

        JButton btnImport = createModernButton("Import CSV");
        JButton btnMasuk = createModernButton("Masuk Ke Aplikasi");

        btnPanel.add(btnImport);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(btnMasuk);

        mainPanel.add(btnPanel, BorderLayout.CENTER);

        // ====== Action Tombol ======
        btnMasuk.addActionListener(e -> {
            new MainFrame();
            dispose();
        });

        btnImport.addActionListener(e -> {
            String pathCsv = JOptionPane.showInputDialog("Masukkan path CSV:");

            if (pathCsv == null || pathCsv.isBlank())
                return;

            try {
                MainFrame mf = new MainFrame();
                boolean ok = mf.importCSV(pathCsv);

                if (!ok) {
                    mf.dispose();
                    JOptionPane.showMessageDialog(
                            this,
                            "Import CSV gagal. Periksa file!",
                            "Gagal",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Terjadi kesalahan: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // =============================================
    // == COMPONENT MODERN: GRADIENT BACKGROUND ==
    // =============================================
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth();
            int h = getHeight();

            Color top = new Color(230, 240, 255);
            Color bottom = new Color(200, 220, 255);

            GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }
    }

    // =============================================
    // == ROUNDED PANEL DENGAN SUDUT MELENGKUNG ==
    // =============================================
    class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =============================================
    // == TOMBOL MODERN DENGAN HOVER EFFECT ==
    // =============================================
    private JButton createModernButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.white);

        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(50, 110, 160));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(70, 130, 180));
            }
        });

        return btn;
    }
}
