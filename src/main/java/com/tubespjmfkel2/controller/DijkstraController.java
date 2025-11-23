package com.tubespjmfkel2.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tubespjmfkel2.dto.DijkstraResult;
import com.tubespjmfkel2.model.algorithm.Dijkstra;
import com.tubespjmfkel2.model.entity.Vertex;

/**
 * Controller yang bertanggung jawab menjalankan proses perhitungan
 * shortest path menggunakan algoritma Dijkstra.
 *
 * <p>
 * DijkstraController tidak menyimpan struktur graph sendiri,
 * melainkan memanfaatkan GraphController sebagai sumber data graf
 * dan utilitas pengelolaannya. Class ini hanya fokus pada logika
 * menjalankan algoritma dan memproses hasilnya menjadi DTO yang
 * siap dipakai oleh GUI atau lapisan presentasi lainnya.
 * </p>
 */
public class DijkstraController {

    private final GraphController graphController;

    /**
     * Konstruktor DijkstraController.
     *
     * @param graphController controller yang menyediakan graph dan operasi
     *                        pendukungnya.
     */
    public DijkstraController(GraphController graphController) {
        this.graphController = graphController;
    }

    /**
     * Menjalankan algoritma Dijkstra dari vertex awal hingga vertex tujuan.
     *
     * <p>
     * Metode ini akan:
     * <ol>
     * <li>Memvalidasi input nama vertex start dan end.</li>
     * <li>Mengambil objek Vertex dari Graph melalui GraphController.</li>
     * <li>Melakukan reset seluruh vertex pada graph agar siap dihitung.</li>
     * <li>Menjalankan algoritma Dijkstra.</li>
     * <li>Jika ada jalur, mengumpulkan path menjadi daftar nama vertex.</li>
     * <li>Mengembalikan hasil dalam bentuk DTO {@link DijkstraResult}.</li>
     * </ol>
     * </p>
     *
     * @param inputStartVertex nama vertex awal
     * @param inputEndVertex   nama vertex tujuan
     * @return objek {@link DijkstraResult} berisi path dan total jarak,
     *         atau <code>null</code> jika vertex tidak valid atau tidak ada jalur.
     */
    public DijkstraResult runDijkstra(String inputStartVertex, String inputEndVertex) {

        Vertex startVertex = graphController.findVertexModel(inputStartVertex);
        Vertex endVertex = graphController.findVertexModel(inputEndVertex);

        if (startVertex == null || endVertex == null)
            return null;

        // Kasus start == end
        if (inputStartVertex.equals(inputEndVertex))
            return new DijkstraResult(Arrays.asList(inputStartVertex), 0);

        // Reset dahulu semua vertex
        graphController.getCoreGraph().resetAllVertices();

        // Jalankan Dijkstra
        Dijkstra.calculateShortestPathFromSource(
                graphController.getCoreGraph(),
                startVertex);

        // Bentuk path
        List<String> path = new ArrayList<>();
        for (Vertex v : endVertex.getShortestPath())
            path.add(v.getVertex());

        if (!path.contains(inputEndVertex))
            path.add(inputEndVertex);

        return new DijkstraResult(path, endVertex.getDistance());
    }
}
