package com.tubespjmfkel2.model.entity;

import java.util.*;

/**
 * Representasi dari sebuah simpul (vertex) dalam graph.
 *
 * Setiap vertex memiliki:
 * <ul>
 * <li>Nama unik</li>
 * <li>Jarak saat ini dari vertex sumber (distance)</li>
 * <li>Daftar vertex tetangga beserta bobot edge menuju tetangga tersebut</li>
 * <li>Daftar jalur terpendek yang mengarah ke vertex ini</li>
 * </ul>
 *
 * Kelas ini digunakan sebagai struktur data utama untuk menjalankan
 * algoritma Dijkstra.
 */
public class Vertex {

    /** Nama atau label unik dari vertex */
    private final String vertexName;

    /** Daftar vertex yang menyusun jalur terpendek menuju vertex ini */
    private List<Vertex> shortestPath = new LinkedList<>();

    /** Jarak dari vertex sumber. Default = tak hingga */
    private Integer distance = Integer.MAX_VALUE;

    private List<Edge> edges = new ArrayList<>();

    /**
     * Membuat instance vertex baru dengan nama tertentu.
     *
     * @param vertexName nama unik vertex
     */
    public Vertex(String vertexName) {
        this.vertexName = vertexName;
    }

    public void addEdge(Vertex destination, int weight) {
        edges.add(new Edge(this, destination, weight));
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public String getVertexName() {
        return vertexName;
    }

    public List<Vertex> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Vertex> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     * Mengembalikan status vertex ke kondisi awal sebelum perhitungan,
     * yaitu jarak tak hingga dan path kosong.
     */
    public void reset() {
        this.distance = Integer.MAX_VALUE;
        this.shortestPath.clear();
    }
}
