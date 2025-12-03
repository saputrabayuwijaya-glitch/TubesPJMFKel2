package com.tubespjmfkel2.view;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.hierarchical.*;

import com.tubespjmfkel2.service.GraphService;
import com.tubespjmfkel2.service.DijkstraService;
import com.tubespjmfkel2.dto.DijkstraResult;

import org.apache.commons.csv.CSVFormat;

public class GUI extends JFrame {

    private GraphService graphService = new GraphService();

    private DijkstraService dijkstraService = new DijkstraService(graphService);

    private mxGraph uiGraph = new mxGraph();

    private Map<String, Object> uiVertexMap = new HashMap<>();

    private Map<String, Object> uiEdgeMap = new HashMap<>();

    private mxGraphComponent graphComponent = new mxGraphComponent(uiGraph);


    public GUI() {

        super("Pencarian Rute Terpendek Menuju Bengkel");

        JButton btnAddVertex = new JButton("Tambah Titik Tempat");
        JButton btnAddEdge = new JButton("Tambah Jarak");
        JButton btnFindPath = new JButton("Cari Rute Terpendek");
        JButton btnReset = new JButton("Reset Semua");
        JButton btnImportCSV = new JButton("Import CSV");

        btnAddVertex.addActionListener(e -> addVertex());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());
        btnReset.addActionListener(e -> resetAll());
        btnImportCSV.addActionListener(e -> importCSV());

        JPanel headerPanel = new JPanel();
        headerPanel.add(btnAddVertex);
        headerPanel.add(btnAddEdge);
        headerPanel.add(btnFindPath);
        headerPanel.add(btnReset);
        headerPanel.add(btnImportCSV);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(graphComponent, BorderLayout.CENTER);

        graphComponent.setConnectable(false);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void importCSV() {
        var pathCsv = JOptionPane.showInputDialog("Masukkan path CSV:");
        if (pathCsv == null || pathCsv.isBlank()) return;

        try (var reader = new java.io.FileReader(pathCsv)) {

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

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Import gagal: " + error.getMessage());
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
//        mxgraph


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
                    60, 60,
                    "shape=ellipse"
            );
            uiVertexMap.put(vertexName, uiVertex);
        } finally {
            uiGraph.getModel().endUpdate();
        }


    }


    private void addEdge() {
        String source = JOptionPane.showInputDialog("Dari Titik Tempat:");
        String destination = JOptionPane.showInputDialog("Menuju Titik Tempat:");
        String weight = JOptionPane.showInputDialog("Jarak (km):");

        Integer weightInt = Integer.parseInt(weight);

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
                    "endArrow=none;strokeColor=black;rounded=true;"
            );

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
            if (i < path.size() - 1) sb.append(" → ");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void highlightPath(List<String> path) {

        uiGraph.getModel().beginUpdate();
        try {
            uiEdgeMap.forEach((k, e) -> uiGraph.setCellStyle(
                    "strokeColor=red;strokeWidth=1;endArrow=none;rounded=true;",
                    new Object[]{e}
            ));

            for (int i = 0; i < path.size() - 1; i++) {
                colorEdge(path.get(i), path.get(i + 1));
            }

        } finally {
            uiGraph.getModel().endUpdate();
        }
    }


    private void colorEdge(String src, String dst) {
        Object edge = uiEdgeMap.get(src + "->" + dst);
        if (edge != null) {
            uiGraph.setCellStyle(
                    "strokeColor=green;strokeWidth=3;endArrow=none;rounded=true;",
                    new Object[]{edge}
            );
        }
    }


    private void resetAll() {
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

    public void autoCenterGraph(mxGraph graph, mxGraphComponent graphComponent) {
        SwingUtilities.invokeLater(() -> {
            Dimension size = graphComponent.getSize();
            if (size.width == 0 || size.height == 0) return;

            mxRectangle bounds = graph.getGraphBounds();
            if (bounds == null) return;

            double dx = (size.getWidth() - bounds.getWidth()) / 2 - bounds.getX();
            double dy = (size.getHeight() - bounds.getHeight()) / 2 - bounds.getY();

            Object parent = graph.getDefaultParent();
            Object[] cells = graph.getChildCells(parent, true, true);

            graph.getModel().beginUpdate();
            try {
                graph.moveCells(cells, dx, dy);
            } finally {
                graph.getModel().endUpdate();
            }

            graph.refresh();
        });
    }

}
