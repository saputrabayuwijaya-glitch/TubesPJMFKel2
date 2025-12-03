package com.tubespjmfkel2.service;

import com.tubespjmfkel2.domain.Graph;
import com.tubespjmfkel2.domain.Vertex;

public class GraphService {

    private Graph graph = new Graph();

    public Graph getGraph() {
        return graph;
    }

    public String addVertex(String vertexName) {
        if (vertexName == null || vertexName.isBlank()) return "Nama tidak boleh kosong!";
        if (findVertex(vertexName) != null) return "Vertex sudah ada!";

        Vertex vertex = new Vertex();
        vertex.setName(vertexName);
        graph.addVertex(vertex);

        return null;
    }

    public String addEdge(String source, String destination, int weight) {
        Vertex vertexSource = findVertex(source);
        Vertex vertexDestination = findVertex(destination);

        if (vertexSource == null) return "Vertex asal tidak ditemukan!";
        if (vertexDestination == null) return "Vertex tujuan tidak ditemukan!";
        if (source.equals(destination)) return "Tidak boleh menuju dirinya!";
        if (weight <= 0) return "Bobot harus > 0!";

        graph.addEdge(vertexSource, vertexDestination, weight);
        return null;
    }

    public Vertex findVertex(String name) {
        return graph.getVertices().stream()
                .filter(v -> v.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void reset() {
        graph.clear();
    }
}
