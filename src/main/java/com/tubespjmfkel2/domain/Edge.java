package com.tubespjmfkel2.domain;


public class Edge {

    private Vertex source;

    private Vertex destination;

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

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public Vertex getOpposite(Vertex currentVertex) {
        return currentVertex.equals(source) ? destination : source;
    }

}
