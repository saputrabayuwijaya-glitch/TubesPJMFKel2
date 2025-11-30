package com.tubespjmfkel2.model.entity;

/**
 * Kelas Edge merepresentasikan sebuah sisi (link) dalam graf berarah.
 * Setiap edge menghubungkan satu simpul asal (source) ke simpul tujuan (destination)
 * dengan bobot tertentu.
 * <p>
 * Edge digunakan oleh algoritma pencarian jalur seperti Dijkstra untuk
 * menghitung jarak terpendek antar simpul di dalam graf.
 */

public class Edge {

    /**
     * Simpul asal dari edge
     */
    private Vertex source;

    /**
     * Simpul tujuan dari edge
     */
    private Vertex destination;

    /**
     * Bobot atau jarak yang merepresentasikan biaya dari source ke destination
     */
    private int weight;

    /**
     * Membuat sebuah edge baru yang menghubungkan dua simpul dengan bobot tertentu.
     *
     * @param source      Simpul asal edge
     * @param destination Simpul tujuan edge
     * @param weight      Bobot/biaya perjalanan dari source ke destination
     */
    public Edge(Vertex source, Vertex destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Mengembalikan simpul asal edge.
     *
     * @return Vertex sumber
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * Mengembalikan simpul tujuan edge.
     *
     * @return Vertex tujuan
     */
    public Vertex getDestination() {
        return destination;
    }

    /**
     * Mengembalikan bobot perjalanan dari source ke destination.
     *
     * @return Bobot edge
     */
    public int getWeight() {
        return weight;
    }

}
