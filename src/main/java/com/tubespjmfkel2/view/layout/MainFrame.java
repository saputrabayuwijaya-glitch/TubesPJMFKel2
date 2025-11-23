package com.tubespjmfkel2.view.layout;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tubespjmfkel2.controller.DijkstraController;
import com.tubespjmfkel2.controller.GraphController;
import com.tubespjmfkel2.dto.DijkstraResult;
import com.tubespjmfkel2.view.component.GraphPanel;
import com.tubespjmfkel2.view.util.PathHighlighter;

/**
 * Kelas utama antarmuka grafis (GUI) aplikasi pencarian rute terpendek
 * menggunakan algoritma Dijkstra.
 *
 * <p>
 * Kelas ini berfungsi sebagai:
 * <ul>
 * <li>Tempat interaksi pengguna seperti menambah vertex, menambah edge,
 * menjalankan perhitungan rute terpendek, dan mereset grafik</li>
 * <li>Menghubungkan layer UI dengan controller logika aplikasi</li>
 * <li>Menampilkan visualisasi graph melalui {@link GraphPanel}</li>
 * </ul>
 *
 * <p>
 * Menggunakan Swing sebagai framework UI dan JGraphX sebagai library
 * visualisasi graph.
 * </p>
 */
public class MainFrame extends JFrame {

    /** Controller yang menangani operasi graph model dan UI graph. */
    private final GraphController graphController = new GraphController();

    /** Controller yang menangani proses perhitungan algoritma Dijkstra. */
    private final DijkstraController dijkstraController = new DijkstraController(graphController);

    /** Panel utama untuk menampilkan graph dalam bentuk visual. */
    private final GraphPanel graphPanel = new GraphPanel(graphController);

    /**
     * Konstruktor utama yang menginisialisasi jendela, tombol menu, dan panel graf.
     */
    public MainFrame() {
        super("Pencarian Rute Terpendek Menuju Bengkel");

        // Tombol - tombol aksi
        JButton btnAddVertex = new JButton("Tambah Titik Tempat");
        JButton btnAddEdge = new JButton("Tambah Jarak");
        JButton btnFindPath = new JButton("Cari Rute Terpendek");
        JButton btnReset = new JButton("Reset Semua");

        // Listener masing-masing tombol
        btnAddVertex.addActionListener(e -> addVertex());
        btnAddEdge.addActionListener(e -> addEdge());
        btnFindPath.addActionListener(e -> findPath());
        btnReset.addActionListener(e -> resetAll());

        // Panel yang menampung tombol
        JPanel top = new JPanel();
        top.add(btnAddVertex);
        top.add(btnAddEdge);
        top.add(btnFindPath);
        top.add(btnReset);

        // Layout utama jendela
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);

        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ========================================================================
    // Menambah Vertex
    // ========================================================================

    /**
     * Menampilkan dialog input untuk menambah vertex baru pada graf.
     * Jika input valid, vertex ditambahkan ke model dan tampilan diperbarui.
     */
    private void addVertex() {
        String vertexName = JOptionPane.showInputDialog("Nama Titik Tempat:");

        String error = graphController.addVertex(vertexName);

        if (error != null)
            JOptionPane.showMessageDialog(this, error);

        graphPanel.refresh();
    }

    // ========================================================================
    // Menambah Edge
    // ========================================================================

    /**
     * Menambah hubungan (edge) antar dua vertex dengan bobot jarak.
     * Menggunakan dialog untuk menerima input asal, tujuan, dan bobot.
     */
    private void addEdge() {
        String from = JOptionPane.showInputDialog("Dari Titik Tempat:");
        String to = JOptionPane.showInputDialog("Menuju Titik Tempat:");
        String w = JOptionPane.showInputDialog("Jarak (km):");

        String error = graphController.addEdge(from, to, w);

        if (error != null)
            JOptionPane.showMessageDialog(this, error);

        graphPanel.refresh();
    }

    // ========================================================================
    // Menjalankan Dijkstra
    // ========================================================================

    /**
     * Menjalankan perhitungan rute terpendek menggunakan algoritma Dijkstra.
     *
     * <p>
     * Jika rute ditemukan:
     * <ul>
     * <li>Graph ditandai (highlighting) pada jalur yang dilalui</li>
     * <li>Informasi rute dan total jarak ditampilkan dalam dialog</li>
     * </ul>
     *
     * <p>
     * Jika gagal, pengguna diberi pesan bahwa rute tidak tersedia.
     * </p>
     */
    private void findPath() {

        String start = JOptionPane.showInputDialog("Dari Titik Tempat:");
        String end = JOptionPane.showInputDialog("Menuju Titik Tempat:");

        DijkstraResult result = dijkstraController.runDijkstra(start, end);

        if (result == null) {
            JOptionPane.showMessageDialog(this, "Rute tidak ditemukan!");
            return;
        }

        List<String> path = result.getPath();
        int distance = result.getDistance();

        PathHighlighter.highlight(
                graphController.getUiGraph(),
                graphController,
                path);

        StringBuilder sb = new StringBuilder();
        sb.append("Rute Terpendek\n")
                .append("Dari: ").append(start).append("\n")
                .append("Menuju: ").append(end).append("\n")
                .append("Total Jarak: ").append(distance).append(" km\n\n")
                .append("Urutan Titik:\n");

        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1)
                sb.append(" → ");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    // ========================================================================
    // Reset Seluruh Data
    // ========================================================================

    /**
     * Mereset seluruh graph dan tampilan visual menjadi kondisi awal.
     * Akan menampilkan dialog konfirmasi sebelum memproses reset.
     */
    private void resetAll() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin mereset semua?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            graphController.resetGraph();
            graphPanel.refresh();
        }
    }
}
