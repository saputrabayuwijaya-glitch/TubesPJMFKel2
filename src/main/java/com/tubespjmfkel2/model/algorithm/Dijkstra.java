package com.tubespjmfkel2.model.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tubespjmfkel2.model.entity.Edge;
import com.tubespjmfkel2.model.entity.Graph;
import com.tubespjmfkel2.model.entity.Vertex;

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
     * @param graph  graph yang berisi vertex, edge, dan bobotnya
     * @param source vertex awal (jarak awal = 0).
     * @return graph yang sudah terisi informasi jarak terpendek pada setiap vertex
     */
    public static Graph calculateShortestPathFromSource(Graph graph, Vertex source) {

        source.setDistance(0);

        Set<Vertex> settledVertices = new HashSet<>();
        Set<Vertex> unsettledVertices = new HashSet<>();

        unsettledVertices.add(source);
        // jima pada saat vertex yang belum mapan itu masih ada
        // akan melakukan yang namanaya evaluasi

        while (!unsettledVertices.isEmpty()) {
            // vertex yang belum mapan itu akan dimasukan kedalam current vertex melalui
            // method getLowsetDistanceVertex
            Vertex currentVertex = getLowestDistanceVertex(unsettledVertices);
            // vertex saat ini dihapus dari unsetlled vertex, jadi tidak perlu di looping
            // lagi
            unsettledVertices.remove(currentVertex);
            // loop edge untuk currentvertexnya
            for (Edge edge : currentVertex.getEdges()) {
                // edge tujuan akan dimasukan kedalam tetangga
                Vertex adjacent = edge.getDestination();
                int weight = edge.getWeight();
                //
                if (!settledVertices.contains(adjacent)) {
                    calculateMinimumDistance(adjacent, weight, currentVertex);
                    unsettledVertices.add(adjacent);
                }
            }

            settledVertices.add(currentVertex);

        }

        return graph;
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
     * @param sourceVertex     vertex sumber saat ini
     */
    private static void calculateMinimumDistance(
            Vertex evaluationVertex,
            Integer edgeWeight,
            Vertex sourceVertex) {

        Integer sourceDistance = sourceVertex.getDistance();

        // Jika jarak baru lebih kecil, lakukan update
        if (sourceDistance + edgeWeight < evaluationVertex.getDistance()) {

            evaluationVertex.setDistance(sourceDistance + edgeWeight);

            // Salin path dari sumber dan tambahkan vertex saat ini
            List<Vertex> shortestPath = new ArrayList<>(sourceVertex.getShortestPath());
            shortestPath.add(sourceVertex);

            evaluationVertex.setShortestPath(shortestPath);
        }
    }

}
