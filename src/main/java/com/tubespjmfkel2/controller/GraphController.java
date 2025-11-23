package com.tubespjmfkel2.controller;

import java.util.HashMap;
import java.util.Map;

import com.mxgraph.view.mxGraph;
import com.tubespjmfkel2.model.entity.Graph;
import com.tubespjmfkel2.model.entity.Vertex;

/**
 * Controller yang bertanggung jawab mengelola struktur graph,
 * baik dalam bentuk data inti (backend) maupun visualisasi (frontend/UI)
 * menggunakan mxGraph.
 *
 * <p>
 * GraphController menyediakan operasi dasar seperti:
 * <ul>
 * <li>Menambah vertex</li>
 * <li>Menambah edge dengan bobot</li>
 * <li>Mengambil vertex berdasarkan nama</li>
 * <li>Membersihkan graph</li>
 * </ul>
 *
 * <p>
 * Class ini menghubungkan antara struktur graph untuk
 * perhitungan algoritma (kelas {@link Graph} dan {@link Vertex})
 * serta representasi grafis pada GUI untuk tampilan visual.
 * </p>
 */
public class GraphController {

    /** Struktur graph inti untuk perhitungan algoritmik. */
    private Graph coreGraph = new Graph();

    /** Object graph visual dari mxGraph untuk ditampilkan ke UI. */
    private mxGraph uiGraph = new mxGraph();

    /** Penyimpanan referensi edge UI berdasarkan pasangan vertex 'A->B'. */
    private Map<String, Object> edgeMap = new HashMap<>();

    /**
     * Mengambil objek graph inti yang digunakan algoritma.
     *
     * @return objek {@link Graph} yang menyimpan struktur vertex dan edge.
     */
    public Graph getCoreGraph() {
        return coreGraph;
    }

    /**
     * Mengambil objek visual mxGraph untuk ditampilkan dalam UI.
     *
     * @return instance {@link mxGraph}
     */
    public mxGraph getUiGraph() {
        return uiGraph;
    }

    /**
     * Mengambil map penyimpanan reference edge yang
     * digunakan untuk pewarnaan atau modifikasi tampilan edge.
     *
     * @return Map edge yang dipetakan berdasarkan format "From->To".
     */
    public Map<String, Object> getEdgeMap() {
        return edgeMap;
    }

    /**
     * Menambahkan sebuah vertex baru ke struktur graph dan tampilan UI.
     *
     * <p>
     * Langkah yang dilakukan:
     * <ol>
     * <li>Validasi nama vertex</li>
     * <li>Memastikan vertex belum ada</li>
     * <li>Membuat objek {@link Vertex} dan menambahkannya ke Graph</li>
     * <li>Menggambar vertex pada UI (mxGraph)</li>
     * </ol>
     * </p>
     *
     * @param vertexName nama vertex baru yang ingin ditambahkan
     * @return pesan error jika gagal, atau {@code null} jika berhasil
     */
    public String addVertex(String vertexName) {
        if (vertexName == null || vertexName.trim().isEmpty())
            return "Nama Titik Tempat tidak boleh kosong!";

        if (findVertexModel(vertexName) != null)
            return "Titik Tempat '" + vertexName + "' sudah ada!";

        Vertex vertex = new Vertex(vertexName);
        coreGraph.addVertex(vertex);

        uiGraph.getModel().beginUpdate();
        try {
            uiGraph.insertVertex(
                    uiGraph.getDefaultParent(),
                    null,
                    vertexName,
                    50, 50,
                    60, 60,
                    "shape=ellipse");
        } finally {
            uiGraph.getModel().endUpdate();
        }

        return null;
    }

    /**
     * Menambahkan edge berbobot (arah dari -> ke) ke graph.
     *
     * <p>
     * Langkah yang dilakukan:
     * <ol>
     * <li>Mengecek vertex asal dan tujuan</li>
     * <li>Mencegah self-loop</li>
     * <li>Mengecek bobot > 0</li>
     * <li>Menambah edge pada struktur graph</li>
     * <li>Menggambar edge pada UI</li>
     * <li>Menyimpan referensi edge untuk pengolahan berikutnya</li>
     * </ol>
     *
     * @param from      nama vertex asal
     * @param to        nama vertex tujuan
     * @param weightStr bobot edge (> 0)
     * @return pesan error jika gagal, atau {@code null} jika berhasil
     */
    public String addEdge(String from, String to, String weightStr) {

        if (from == null || to == null || weightStr == null)
            return "Input tidak boleh kosong!";

        // cek validasi angka
        int weight;
        try {
            weight = Integer.parseInt(weightStr);
        } catch (NumberFormatException e) {
            return "Bobot harus angka!";
        }

        Vertex vFrom = findVertexModel(from);
        Vertex vTo = findVertexModel(to);

        if (vFrom == null)
            return "Titik Tempat asal tidak ditemukan!";
        if (vTo == null)
            return "Titik Tempat tujuan tidak ditemukan!";
        if (from.equals(to))
            return "Titik Tempat tidak boleh menuju dirinya sendiri!";
        if (weight <= 0)
            return "Bobot harus lebih besar dari 0!";
        if (edgeMap.containsKey(from + "->" + to))
            return "Rute ini sudah ada!";

        // tambah edge pada graph baru
        coreGraph.addEdge(vFrom, vTo, weight);

        // ADD KE UI
        uiGraph.getModel().beginUpdate();
        try {
            Object uiFrom = findVertexUI(from);
            Object uiTo = findVertexUI(to);

            Object edge = uiGraph.insertEdge(
                    uiGraph.getDefaultParent(),
                    null,
                    weight,
                    uiFrom,
                    uiTo);

            edgeMap.put(from + "->" + to, edge);
        } finally {
            uiGraph.getModel().endUpdate();
        }

        return null;
    }

    /**
     * Mencari objek Vertex pada model Graph berdasarkan nama.
     *
     * @param vertexName nama vertex
     * @return objek {@link Vertex} jika ditemukan, atau {@code null} jika tidak ada
     */
    public Vertex findVertexModel(String vertexName) {
        return coreGraph.getVertices()
                .stream()
                .filter(v -> v.getVertexName().equals(vertexName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Mencari objek vertex di UI (mxGraph) berdasarkan label nama vertex.
     *
     * @param vertexName nama vertex yang dicari
     * @return objek vertex representasi di UI, atau {@code null} jika tidak ada
     */
    private Object findVertexUI(String vertexName) {
        for (Object cell : uiGraph.getChildVertices(uiGraph.getDefaultParent())) {
            if (vertexName.equals(uiGraph.getLabel(cell)))
                return cell;
        }
        return null;
    }

    /**
     * Mereset seluruh graph:
     * <ul>
     * <li>Menghapus seluruh vertex dan edge di struktur algoritmik</li>
     * <li>Menghapus seluruh tampilan vertex dan edge di UI</li>
     * <li>Membersihkan map penyimpanan edge</li>
     * </ul>
     *
     * <p>
     * Digunakan saat memulai graph baru atau mereset visualisasi.
     * </p>
     */
    public void resetGraph() {
        coreGraph = new Graph();
        edgeMap.clear();

        uiGraph.getModel().beginUpdate();
        try {
            Object[] cells = uiGraph.getChildCells(
                    uiGraph.getDefaultParent(),
                    true,
                    true);

            if (cells != null && cells.length > 0)
                uiGraph.removeCells(cells);
        } finally {
            uiGraph.getModel().endUpdate();
        }
    }
}
