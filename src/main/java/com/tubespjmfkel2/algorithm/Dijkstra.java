package com.tubespjmfkel2.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tubespjmfkel2.domain.Edge;
import com.tubespjmfkel2.domain.Vertex;

/**
 * Kelas ini mengimplementasikan algoritma Dijkstra untuk mencari
 * rute terpendek dari satu titik (source vertex) menuju seluruh
 * vertex lain di dalam sebuah graph berarah atau tak berarah
 * dengan edge berbobot positif.
 * <p>
 * Cara kerja algoritma:
 * <ul>
 * <li>Menetapkan vertex sumber (source) sebagai jarak 0</li>
 * <li>Memilih vertex dengan jarak sementara (distance) terendah</li>
 * <li>Melakukan relaksasi jarak terhadap vertex tetangga</li>
 * <li>Memindahkan vertex tersebut ke dalam himpunan yang telah diproses</li>
 * <li>Mengulangi hingga seluruh vertex selesai diproses</li>
 * </ul>
 */
public class Dijkstra {

    /**
     * Menjalankan algoritma Dijkstra untuk menghitung jarak terpendek
     * dari vertex sumber (source) menuju seluruh vertex dalam graph.
     *
     * @param source vertex awal (jarak awal = 0).
     */
    public static void calculateShortestPathFromSource(Vertex source) {

        // Set jarak vertex sumber menjadi 0 (titik awal perjalanan)
        source.setDistance(0);

        // Inisialisasi himpunan vertex yang sudah final jaraknya (tidak akan berubah lagi)
        Set<Vertex> settledVertices = new HashSet<>();

        // Inisialisasi himpunan vertex yang masih perlu dievaluasi jaraknya (masih sementara)
        Set<Vertex> unsettledVertices = new HashSet<>();

        // Masukkan vertex sumber ke dalam unsettled agar mulai diproses
        unsettledVertices.add(source);

        // Loop selama masih ada vertex yang belum dievaluasi
        while (!unsettledVertices.isEmpty()) {

            // Ambil vertex dengan jarak sementara paling kecil dari unsettled
            Vertex currentVertex = getLowestDistanceVertex(unsettledVertices);

            // Hapus dari unsettled karena akan segera difinalisasi
            unsettledVertices.remove(currentVertex);

            // Loop semua edge yang terhubung ke currentVertex dari adjacency list
            for (Edge edge : currentVertex.getNeighbors()) {

                // Tentukan neighbor (neighbor vertex), karena edge disimpan 2 arah
                Vertex neighbor = edge.getSource().equals(currentVertex)
                        ? edge.getDestination() // Jika current = source, maka neighbor = destination
                        : edge.getSource(); // Jika current = destination, maka neighbor = source

                // Ambil bobot jarak dari edge tersebut
                int weight = edge.getWeight();

                // Jika neighbor belum diproses final jaraknya, lakukan relaksasi
                if (!settledVertices.contains(neighbor)) {

                    // Hitung & update jarak minimum jika lebih kecil dari sebelumnya
                    calculateMinimumDistance(neighbor, weight, currentVertex);

                    // Masukkan neighbor ke unsettled agar ikut diproses di iterasi berikutnya
                    unsettledVertices.add(neighbor);
                }
            }

            // Tandai currentVertex sebagai settled (jaraknya sudah final)
            settledVertices.add(currentVertex);
        }


    }


    /**
     * Mengambil vertex dengan jarak (distance) terkecil dari himpunan vertex
     * yang belum dipastikan jaraknya.
     *
     * @param unsettledVertices daftar vertex yang masih perlu dihitung
     * @return vertex dengan jarak terpendek
     */
    private static Vertex getLowestDistanceVertex(Set<Vertex> unsettledVertices) {

        // belum ada vertex terbaik dan di set null
        Vertex lowestDistanceVertex = null;
        // untuk nilai sementara adalah infinity
        int lowestDistance = Integer.MAX_VALUE;

        // Telusuri semua vertex untuk menentukan jarak paling kecil
        for (Vertex vertex : unsettledVertices) {
            // ambil distance vertex
            int currentDistance = vertex.getDistance();

            if (currentDistance < lowestDistance) {
                // masukan vertex kedalam lowest distance
                lowestDistance = currentDistance;
                // masukan vertex yang di loop kedalam lowest distance vertex
                lowestDistanceVertex = vertex;
            }
        }
        // balikan nilai hasil dari lowestDistanceVertex (current vertex)
        return lowestDistanceVertex;
    }

    /**
     * Melakukan proses relaksasi jarak terhadap vertex tetangga.
     * Jika jarak (distance) baru lebih kecil dari jarak sebelumnya,
     * maka jarak vertex di-update serta jalur terpendek diperbarui.
     *
     * @param evaluationVertex vertex tetangga yang sedang dievaluasi
     * @param edgeWeight       bobot edge dari sumber menuju vertex tetangga
     * @param currentVertex    vertex sumber saat ini
     */
    private static void calculateMinimumDistance(
            Vertex evaluationVertex,
            Integer edgeWeight,
            Vertex currentVertex) {

        Integer sourceDistance = currentVertex.getDistance();

        // Jika jarak baru lebih kecil, lakukan update
        if (sourceDistance + edgeWeight < evaluationVertex.getDistance()) {

            evaluationVertex.setDistance(sourceDistance + edgeWeight);

            // Salin path dari sumber dan tambahkan vertex saat ini
            List<Vertex> shortestPath = new ArrayList<>(currentVertex.getShortestPath());
            shortestPath.add(currentVertex);

            evaluationVertex.setShortestPath(shortestPath);
        }
    }

}
