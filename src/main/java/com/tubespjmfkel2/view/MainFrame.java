package com.tubespjmfkel2.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import com.tubespjmfkel2.service.GraphService;
import com.tubespjmfkel2.service.DijkstraService;
import com.tubespjmfkel2.dto.DijkstraResult;

import org.apache.commons.csv.CSVFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

    private GraphService graphService = new GraphService();

    private DijkstraService dijkstraService = new DijkstraService(graphService);

    private mxGraph uiGraph = new mxGraph();

    private Map<String, Object> uiVertexMap = new HashMap<>();

    private Map<String, Object> uiEdgeMap = new HashMap<>();

    private mxGraphComponent graphComponent = new mxGraphComponent(uiGraph);

    private Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println("Gagal load image: " + e.getMessage());
            return null;
        }
    }

    public MainFrame() {

        super("Pencarian Rute Terpendek Menuju Bengkel");

        JButton btnAddVertex = new JButton("➕ Tambah Titik Tempat");
        JButton btnAddEdge = new JButton("➕ Tambah Jarak");
        JButton btnFindPath = new JButton("🔎 Cari Rute Terpendek");
        JButton btnResetGraph = new JButton("➖ Reset Semua");

        btnAddVertex.addActionListener(e -> addVertex());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());
        btnResetGraph.addActionListener(e -> resetGraph());

        // =============================
        // PANEL TOMBOL (DIPERBAIKI POSISI)
        // =============================
        JPanel headerPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.add(btnAddVertex);
        leftPanel.add(btnAddEdge);

        JPanel centerPanel = new JPanel();
        centerPanel.add(btnFindPath);

        JPanel rightPanel = new JPanel();
        rightPanel.add(btnResetGraph);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // =============================
        // BACKGROUND IMAGE
        // =============================
        Image bgImage = loadImage(
                "C:/Users/Saputra Bayu Wijaya/Downloads/tubes2/TubesPJMFKel2/src/main/java/com/tubespjmfkel2/view/asset/gambar.png");

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(graphComponent, BorderLayout.CENTER);

        graphComponent.setOpaque(false);
        graphComponent.getViewport().setOpaque(false);
        graphComponent.getGraphControl().setOpaque(false);

        setContentPane(backgroundPanel);

        setSize(getMaximumSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean importCSV(String path) {
        try (var reader = new java.io.FileReader(path)) {

            var csv = CSVFormat.DEFAULT.builder()
                    .setHeader("type", "source", "destination", "weight")
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);

            int v = 0, e = 0;

            for (var row : csv) {
                String type = row.get("type");
                String source = row.get("source");
                String destination = row.get("destination");
                String weight = row.get("weight");

                if (type.equalsIgnoreCase("V")) {
                    String error = graphService.addVertex(source);
                    if (error == null) {
                        addVertexUI(source);
                        v++;
                    }
                }

                if (type.equalsIgnoreCase("E")) {
                    String error = graphService.addEdge(source, destination, Integer.parseInt(weight));
                    if (error == null) {
                        addEdgeUI(source, destination, Integer.parseInt(weight));
                        e++;
                    }
                }
            }

            refreshGraph();
            JOptionPane.showMessageDialog(null, "✔ Vertex: " + v + "\n✔ Edge: " + e);

            return true;

        } catch (Exception error) {
            return false;
        }
    }

    private void addVertex() {
        String vertexName = JOptionPane.showInputDialog("Nama Titik Tempat:");
        String error = graphService.addVertex(vertexName);

        if (error != null) {
            JOptionPane.showMessageDialog(null, error);
            return;
        }

        addVertexUI(vertexName);
        refreshGraph();
    }

    private void addVertexUI(String vertexName) {
        uiGraph.getModel().beginUpdate();
        try {
            Object uiVertex = uiGraph.insertVertex(
                    uiGraph.getDefaultParent(),
                    null,
                    vertexName,
                    100, 100,
                    100, 100,
                    "shape=ellipse;strokeWidth=3;fontSize=20;strokeColor=white;strokeColorValue=3;fontColor=black");
            uiVertexMap.put(vertexName, uiVertex);
        } finally {
            uiGraph.getModel().endUpdate();
        }

    }

    private void addEdge() {
        String source = JOptionPane.showInputDialog("Dari Titik Tempat:");
        String destination = JOptionPane.showInputDialog("Menuju Titik Tempat:");
        String weight = JOptionPane.showInputDialog("Jarak (km):");

        String error = graphService.addEdge(source, destination, Integer.parseInt(weight));

        if (error != null) {
            JOptionPane.showMessageDialog(null, error);
            return;
        }

        addEdgeUI(source, destination, Integer.parseInt(weight));
        refreshGraph();
    }

    private void addEdgeUI(String source, String destination, int weight) {

        Object vertexSource = uiVertexMap.get(source);
        Object vertexDestination = uiVertexMap.get(destination);

        uiGraph.getModel().beginUpdate();
        try {
            Object edge = uiGraph.insertEdge(
                    uiGraph.getDefaultParent(),
                    null,
                    weight,
                    vertexSource,
                    vertexDestination,
                    "endArrow=none;strokeColor=white;rounded=true;strokeWidth=3;fontColor=yellow;fontSize=20");

            uiEdgeMap.put(source + "->" + destination, edge);
            uiEdgeMap.put(destination + "->" + source, edge);

        } finally {
            uiGraph.getModel().endUpdate();
        }
    }

    private void findPath() {
        String start = JOptionPane.showInputDialog("Dari Titik:");
        String end = JOptionPane.showInputDialog("Menuju Titik:");

        DijkstraResult result = dijkstraService.findShortestPath(start, end);

        if (result == null) {
            JOptionPane.showMessageDialog(null, "Rute tidak ditemukan!");
            return;
        }

        highlightPath(result.getPath());

        StringBuilder sb = new StringBuilder();
        sb.append("Rute Terpendek\n")
                .append("Dari: ").append(start).append("\n")
                .append("Menuju: ").append(end).append("\n")
                .append("Total Jarak: ").append(result.getDistance()).append(" km\n\n")
                .append("Urutan Titik:\n");

        List<String> path = result.getPath();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1)
                sb.append(" → ");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void highlightPath(List<String> path) {

        uiGraph.getModel().beginUpdate();
        try {
            uiEdgeMap.forEach((k, e) -> uiGraph.setCellStyle(
                    "strokeColor=red;strokeWidth=3;endArrow=none;rounded=true;fontColor=white;fontSize=20",
                    new Object[] { e }));

            for (int i = 0; i < path.size() - 1; i++) {
                colorEdge(path.get(i), path.get(i + 1));
            }

        } finally {
            uiGraph.getModel().endUpdate();
        }
    }

    private void colorEdge(String source, String destination) {
        Object edge = uiEdgeMap.get(source + "->" + destination);
        if (edge != null) {
            uiGraph.setCellStyle(
                    "strokeColor=green;strokeWidth=5;endArrow=none;rounded=true;fontColor=white;fontSize=20",
                    new Object[] { edge });
        }
    }

    private void resetGraph() {
        graphService.reset();
        uiVertexMap.clear();
        uiEdgeMap.clear();

        uiGraph.getModel().beginUpdate();
        try {
            uiGraph.removeCells(uiGraph.getChildCells(uiGraph.getDefaultParent(), true, true), true);
        } finally {
            uiGraph.getModel().endUpdate();
        }

        refreshGraph();
    }

    private void refreshGraph() {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(uiGraph);
        layout.setOrientation(SwingConstants.WEST);
        layout.setIntraCellSpacing(70);
        layout.execute(uiGraph.getDefaultParent());
        autoCenterGraph(uiGraph, graphComponent);
        graphComponent.refresh();
        repaint();
        revalidate();

    }

    public void autoCenterGraph(mxGraph uiGraph, mxGraphComponent graphComponent) {
        SwingUtilities.invokeLater(() -> {
            Dimension size = graphComponent.getSize();
            if (size.width == 0 || size.height == 0)
                return;

            mxRectangle bounds = uiGraph.getGraphBounds();
            if (bounds == null)
                return;

            double dx = (size.getWidth() - bounds.getWidth()) / 2 - bounds.getX();
            double dy = (size.getHeight() - bounds.getHeight()) / 2 - bounds.getY();

            Object parent = uiGraph.getDefaultParent();
            Object[] cells = uiGraph.getChildCells(parent, true, true);

            uiGraph.getModel().beginUpdate();
            try {
                uiGraph.moveCells(cells, dx, dy);
            } finally {
                uiGraph.getModel().endUpdate();
            }

            uiGraph.refresh();
        });
    }

}
