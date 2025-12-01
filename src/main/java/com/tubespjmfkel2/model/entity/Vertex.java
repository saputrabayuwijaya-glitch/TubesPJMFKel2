package com.tubespjmfkel2.model.entity;

import java.util.ArrayList;

import java.util.List;

/**
 * Representasi dari sebuah simpul (vertex) dalam graph.
 * <p>
 * Setiap vertex memiliki:
 * <ul>
 * <li>Nama unik</li>
 * <li>Jarak saat ini dari vertex sumber (distance)</li>
 * <li>Daftar vertex tetangga beserta bobot edge menuju tetangga tersebut</li>
 * <li>Daftar jalur terpendek yang mengarah ke vertex ini</li>
 * </ul>
 * <p>
 * Kelas ini digunakan sebagai struktur data utama untuk menjalankan
 * algoritma Dijkstra.
 */
public class Vertex {

    /**
     * Nama atau label unik dari vertex
     */
    private String name;
    /**
     * Daftar tetangga vertex
     */
    private List<Edge> adjacentEdges = new ArrayList<>();
    /**
     * Daftar vertex yang menyusun jalur terpendek menuju vertex ini
     */
    private List<Vertex> shortestPath = new ArrayList<>();
    /**
     * Jarak dari vertex sumber. Default = tak hingga
     */
    private Integer distance = Integer.MAX_VALUE;

    /**
     * Membuat instance vertex baru dengan nama tertentu.
     *
     * @param name nama unik vertex
     */
    public Vertex(String name) {
        this.name = name;
    }

    public void addAdjacentEdge(Edge edge) {
        adjacentEdges.add(edge);
    }

    public List<Edge> getAdjacentEdges() {
        return adjacentEdges;
    }

    public String getName() {
        return name;
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
}
