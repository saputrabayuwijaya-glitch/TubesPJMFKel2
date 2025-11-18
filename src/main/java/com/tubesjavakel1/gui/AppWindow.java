package com.tubesjavakel1.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tubesjavakel1.algorithm.Dijkstra;
import com.tubesjavakel1.gui.graph.GraphManager;
import com.tubesjavakel1.gui.graph.GraphPanel;
import com.tubesjavakel1.gui.graph.PathHighlighter;

import java.util.List;

public class AppWindow extends JFrame {

    private GraphManager gm = new GraphManager();

    public AppWindow() {
        super("Pencarian Rute Terpendek Menuju Bengkel");

        JButton btnAddNode = new JButton("Tambah Titik Lokasi");
        JButton btnAddEdge = new JButton("Tambah Rute");
        JButton btnFindPath = new JButton("Cari Rute Terpendek");

        btnAddNode.addActionListener(e -> addNode());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());

        JPanel top = new JPanel();
        top.add(btnAddNode);
        top.add(btnAddEdge);
        top.add(btnFindPath);

        add(top, BorderLayout.NORTH);
        add(new GraphPanel(gm), BorderLayout.CENTER);

        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addNode() {
        String name = JOptionPane.showInputDialog("Titik Lokasi:");
        if (name != null && !name.isEmpty()) {
            gm.addNode(name);
        }
    }

    private void addEdge() {
        String from = JOptionPane.showInputDialog("Dari:");
        String to = JOptionPane.showInputDialog("Menuju:");
        int w = Integer.parseInt(JOptionPane.showInputDialog("Panjang km:"));
        gm.addEdge(from, to, w);
    }

    private void findPath() {
        String start = JOptionPane.showInputDialog("Mulai dari:");
        String end = JOptionPane.showInputDialog("Menuju:");

        List<String> path = Dijkstra.run(gm.adj, start, end);
        if (path == null) {
            JOptionPane.showMessageDialog(this, "Rute tidak ditemukan!");
            return;
        }

        PathHighlighter.highlight(gm.graph, gm.parent, gm, path);
    }
}
