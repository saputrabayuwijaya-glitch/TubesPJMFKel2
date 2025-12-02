package com.tubespjmfkel2.service;

import java.util.ArrayList;

import java.util.List;

import com.tubespjmfkel2.dto.DijkstraResult;
import com.tubespjmfkel2.algorithm.Dijkstra;
import com.tubespjmfkel2.domain.Vertex;

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
public class DijkstraService {

    private GraphService graphService;

    /**
     * Konstruktor DijkstraController.
     *
     * @param graphService controller yang menyediakan graph dan operasi
     *                     pendukungnya.
     */
    public DijkstraService(GraphService graphService) {
        this.graphService = graphService;
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
     * @param vertexStartInput nama vertex awal
     * @param vertexEndInput   nama vertex tujuan
     * @return objek {@link DijkstraResult} berisi path dan total jarak,
     * atau <code>null</code> jika vertex tidak valid atau tidak ada jalur.
     */
    public DijkstraResult findShortestPath(String vertexStartInput, String vertexEndInput) {

        Vertex vertexStart = graphService.findVertex(vertexStartInput);
        Vertex vertexEnd = graphService.findVertex(vertexEndInput);

        if (vertexStart == null || vertexEnd == null)
            return null;

        // Kasus start == end
        if (vertexStartInput.equals(vertexEndInput))
            return new DijkstraResult(List.of(vertexStartInput), 0);

        // Reset dahulu distance dan shortestpathnya
        for (Vertex vertex : graphService.getGraph().getVertices()) {
            vertex.setDistance(Integer.MAX_VALUE);
            vertex.getShortestPath().clear();
        }
        // Jalankan Dijkstra
        Dijkstra.calculateShortestPathFromSource(vertexStart);

        // Jika tidak ada jalur (distance tetap MAX_VALUE)
        if (vertexEnd.getDistance() == Integer.MAX_VALUE) {
            return null;
        }

        // Bentuk path
        List<String> path = new ArrayList<>();

        // Masukkan node hasil shortestPath
        for (Vertex vertex : vertexEnd.getShortestPath()) {
            path.add(vertex.getName());
        }
        path.add(vertexEnd.getName());
        return new DijkstraResult(path, vertexEnd.getDistance());
    }
}
