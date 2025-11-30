package com.tubespjmfkel2.view;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mxgraph.swing.mxGraphComponent;
import com.tubespjmfkel2.controller.DijkstraController;
import com.tubespjmfkel2.controller.GraphController;
import com.tubespjmfkel2.dto.DijkstraResult;
import com.mxgraph.view.mxGraph;

/**
 * Kelas utama antarmuka grafis (GUI) aplikasi pencarian rute terpendek
 * menggunakan algoritma Dijkstra.
 */
public class GUI extends JFrame {

    /**
     * Controller untuk mengelola graph baik dari sisi model maupun UI graph mapping.
     * Instance ini juga digunakan sebagai basis data vertex dan edge dalam perhitungan Dijkstra.
     */
    private final GraphController graphController = new GraphController();

    /**
     * Controller untuk menjalankan perhitungan rute terpendek menggunakan algoritma Dijkstra.
     * Class ini bergantung pada {@link GraphController} untuk mendapatkan data graph.
     */
    private final DijkstraController dijkstraController = new DijkstraController(graphController);

    /**
     * Komponen visualisasi graph dari library JGraphX.
     * Komponen ini akan melakukan rendering graph yang disediakan oleh
     * {@link GraphController#getUiGraph()}.
     */
    private final mxGraphComponent graphComponent = new mxGraphComponent(graphController.getUiGraph());

    /**
     * Konstruktor utama jendela GUI.
     * Inisialisasi meliputi pembuatan tombol menu, layouting komponen,
     * serta pengaturan frame agar tampil di tengah layar.
     */
    public GUI() {
        super("Pencarian Rute Terpendek Menuju Bengkel");

        JButton btnAddVertex = new JButton("Tambah Titik Tempat");
        JButton btnAddEdge = new JButton("Tambah Jarak");
        JButton btnFindPath = new JButton("Cari Rute Terpendek");
        JButton btnReset = new JButton("Reset Semua");

        btnAddVertex.addActionListener(e -> addVertex());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());
        btnReset.addActionListener(e -> resetAll());

        JPanel headerPanel = new JPanel();
        headerPanel.add(btnAddVertex);
        headerPanel.add(btnAddEdge);
        headerPanel.add(btnFindPath);
        headerPanel.add(btnReset);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(graphComponent, BorderLayout.CENTER);

        graphComponent.setConnectable(false);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Menampilkan dialog untuk menambah vertex baru ke graph.
     *
     * <p>Melakukan validasi melalui GraphController dan refresh UI jika selesai.</p>
     */
    private void addVertex() {
        String vertexNameInput = JOptionPane.showInputDialog("Nama Titik Tempat:");
        String error = graphController.addVertex(vertexNameInput);
        if (error != null) JOptionPane.showMessageDialog(null, error);
        refreshGraph();
    }

    /**
     * Menampilkan dialog untuk menambah edge baru beserta bobot jaraknya.
     *
     * <p>Menghubungkan 2 vertex yang sudah ada dengan weight bertipe numerik (km).</p>
     */
    private void addEdge() {
        String vertexSourceInput = JOptionPane.showInputDialog("Dari Titik Tempat:");
        String vertexDestinationInput = JOptionPane.showInputDialog("Menuju Titik Tempat:");
        String weightInput = JOptionPane.showInputDialog("Jarak (km):");
        String error = graphController.addEdge(vertexSourceInput, vertexDestinationInput, weightInput);
        if (error != null) JOptionPane.showMessageDialog(null, error);
        refreshGraph();
    }

    /**
     * Menjalankan proses pencarian rute terpendek dari start ke end.
     *
     * <p>
     * Jika rute tersedia:
     * <ul>
     *   <li>Menandai path di graph UI</li>
     *   <li>Menampilkan total jarak</li>
     *   <li>Menampilkan urutan vertex</li>
     * </ul>
     * </p>
     *
     * <p>Jika tidak, menampilkan pesan error.</p>
     */
    private void findPath() {
        String vertexStartInput = JOptionPane.showInputDialog("Dari Titik Tempat:");
        String vertexEndInput = JOptionPane.showInputDialog("Menuju Titik Tempat:");

        DijkstraResult result = dijkstraController.findShortestPath(vertexStartInput, vertexEndInput);
        if (result == null) {
            JOptionPane.showMessageDialog(null, "Rute tidak ditemukan!");
            return;
        }

        List<String> path = result.getPath();
        int distance = result.getDistance();

        highlightPath(graphController.getUiGraph(), graphController, path);

        StringBuilder sb = new StringBuilder();
        sb.append("Rute Terpendek\n")
                .append("Dari: ").append(vertexStartInput).append("\n")
                .append("Menuju: ").append(vertexEndInput).append("\n")
                .append("Total Jarak: ").append(distance).append(" km\n\n")
                .append("Urutan Titik:\n");

        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1) sb.append(" → ");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    /**
     * Method static untuk menandai (highlight) sebuah jalur vertex pada graph UI.
     *
     * <p>
     * Langkah yang dilakukan:
     * <ul>
     *   <li>Me-reset seluruh edge ke style default (garis merah tipis)</li>
     *   <li>Mewarnai edge yang dilewati rute terpendek ke warna hijau</li>
     *   <li>Menebalkan edge rute terpendek (strokeWidth = 3)</li>
     * </ul>
     * </p>
     *
     * @param graph           Graph UI yang akan dimodifikasi ({@link mxGraph}).
     * @param graphController Controller penyedia mapping key edge→object cell UI.
     * @param path            Daftar nama vertex yang membentuk rute.
     */
    public static void highlightPath(mxGraph graph, GraphController graphController, List<String> path) {
        graph.getModel().beginUpdate();
        try {
            graphController.getUiEdgeMap().forEach((k, e) -> {
                graph.setCellStyle("strokeColor=red;strokeWidth=1;endArrow=none;", new Object[]{e});
            });

            for (int i = 0; i < path.size() - 1; i++) {
                colorEdge(graph, graphController, path.get(i), path.get(i + 1));
            }

        } finally {
            graph.getModel().endUpdate();
        }
    }

    /**
     * Mengubah warna dan tampilan edge tertentu pada graph UI menjadi hijau dan lebih tebal.
     *
     * @param graph             Graph UI target.
     * @param graphController   Penyedia mapping edge.
     * @param vertexSource      Nama vertex asal.
     * @param vertexDestination Nama vertex tujuan.
     */
    private static void colorEdge(mxGraph graph, GraphController graphController, String vertexSource, String vertexDestination) {
        Object edge = graphController.getUiEdgeMap().get(vertexSource + "->" + vertexDestination);
        if (edge != null) {
            graph.setCellStyle("strokeColor=green;strokeWidth=3;endArrow=none;", new Object[]{edge});
        }
    }

    /**
     * Mereset seluruh data graph ke kondisi awal setelah konfirmasi user.
     */
    private void resetAll() {

        graphController.resetGraph();
        refreshGraph();

    }

    /**
     * Me-refresh visual graph pada UI agar setiap perubahan vertex/edge terlihat.
     */
    private void refreshGraph() {
        graphComponent.refresh();
        repaint();
        revalidate();
    }
}
