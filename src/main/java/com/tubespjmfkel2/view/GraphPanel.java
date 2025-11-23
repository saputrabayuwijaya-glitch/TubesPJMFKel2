package com.tubespjmfkel2.view;

import com.mxgraph.swing.mxGraphComponent;
import com.tubespjmfkel2.controller.GraphController;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * GraphPanel adalah panel khusus untuk menampilkan graf menggunakan
 * library JGraphX (mxGraph). Panel ini mengintegrasikan {@link GraphController}
 * untuk mengontrol data graf dan {@link mxGraphComponent} untuk rendering
 * grafik.
 * 
 * <p>
 * Panel ini menggunakan layout {@link BorderLayout} dan menempatkan
 * {@link mxGraphComponent} di tengah panel.
 * </p>
 */
public class GraphPanel extends JPanel {

    /** Controller yang menangani logika graf dan data graf */
    private final GraphController graphController;

    /** Komponen graf yang menampilkan graf secara visual */
    private final mxGraphComponent graphComponent;

    /**
     * Konstruktor GraphPanel.
     * Kalau nanti Anda menambahkan fitur seperti:
     * 1. Klik vertex untuk menampilkan informasi
     * 2. Klik kanan untuk hapus vertex/edge
     * 3. Panel melakukan update terhadap graph tanpa lewat AppWindow
     * 4. Menangani event keyboard/mouse langsung dari panel
     * 5. maka GraphPanel akan butuh akses ke controller.
     *
     * @param graphController Instance dari {@link GraphController} yang digunakan
     *                        untuk mengelola graf yang akan ditampilkan.
     */
    public GraphPanel(GraphController graphController) {
        this.graphController = graphController;
        setLayout(new BorderLayout());

        // Membuat dan menambahkan komponen graf ke panel
        graphComponent = new mxGraphComponent(graphController.getUiGraph());
        add(graphComponent, BorderLayout.CENTER);
    }

    /**
     * Memperbarui tampilan graf.
     * <p>
     * Metode ini melakukan refresh pada komponen graf dan memanggil
     * repaint serta revalidate agar perubahan graf terlihat di UI.
     * </p>
     */
    public void refresh() {
        graphComponent.refresh();
        repaint();
        revalidate();
    }
}
