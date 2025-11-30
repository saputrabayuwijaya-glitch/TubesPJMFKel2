package com.tubespjmfkel2.model.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Kelas Graph merepresentasikan struktur graf berarah yang terdiri dari
 * kumpulan simpul (Vertex) dan sisi (Edge).
 * <p>
 * Setiap Vertex dapat dihubungkan melalui satu atau lebih Edge
 * dengan bobot yang menyatakan jarak atau biaya perjalanan.
 * <p>
 * Kelas ini mendukung penambahan simpul, penambahan edge, serta
 * melakukan reset terhadap seluruh vertex sebelum menjalankan algoritma
 * pencarian jalur.
 */
public class Graph {

    /**
     * Kumpulan seluruh simpul dalam graf
     */
    private Set<Vertex> vertices = new HashSet<>();

    /**
     * Kumpulan seluruh edge dalam graf
     */
    private List<Edge> edges = new ArrayList<>();

    /**
     * Mengembalikan seluruh simpul dalam graf.
     *
     * @return Set berisi Vertex
     */
    public Set<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Mengembalikan seluruh edge dalam graf.
     *
     * @return List berisi Edge
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Menambahkan sebuah simpul baru ke dalam graf.
     *
     * @param vertexName Vertex baru yang akan ditambahkan
     */
    public void addVertex(Vertex vertexName) {
        vertices.add(vertexName);
    }

    /**
     * Menambahkan sebuah edge dari source ke destination dengan bobot tertentu.
     * Edge akan disimpan di daftar edge global dan juga ditambahkan
     * sebagai hubungan langsung pada simpul asal.
     *
     * @param source      Simpul asal
     * @param destination Simpul tujuan
     * @param weight      Bobot perjalanan
     */
    public void addEdge(Vertex source, Vertex destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        edges.add(edge);
        source.addEdge(edge); // simpan edge yang sama, bukan buat baru
    }

    /**
     * Mengatur ulang setiap simpul dalam graf sebelum menjalankan algoritma
     * pencarian rute (misalnya Dijkstra). Proses reset mengembalikan nilai
     * jarak ke Infinity dan menghapus path sementara.
     */

    public void clear() {
        vertices.clear();
        edges.clear();
    }

}
