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

    /**
     * Object graph visual dari mxGraph untuk ditampilkan ke UI.
     */
    private mxGraph uiGraph = new mxGraph();
    /**
     * Penyimpanan referensi edge UI berdasarkan pasangan vertex 'A->B'.
     */
    private Map<String, Object> uiEdgeMap = new HashMap<>();
    /**
     * Mapping vertex UI: "A" → UI Vertex Object
     */
    private Map<String, Object> uiVertexMap = new HashMap<>();
    /**
     * Struktur graph inti untuk perhitungan algoritmik.
     */
    private Graph graph = new Graph();


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
    public Map<String, Object> getUiEdgeMap() {
        return uiEdgeMap;
    }

    /**
     * Mencari objek vertex di UI (mxGraph) berdasarkan label nama vertex.
     *
     * @param vertexNameInput nama vertex yang dicari
     * @return objek vertex representasi di UI, atau {@code null} jika tidak ada
     */
    private Object getUiVertex(String vertexNameInput) {
        return uiVertexMap.get(vertexNameInput);
    }

    /**
     * Mengambil objek graph inti yang digunakan algoritma.
     *
     * @return objek {@link Graph} yang menyimpan struktur vertex dan edge.
     */
    public Graph getGraph() {
        return graph;
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
     * @param vertexNameInput nama vertex baru yang ingin ditambahkan
     * @return pesan error jika gagal, atau {@code null} jika berhasil
     */
    public String addVertex(String vertexNameInput) {
        if (vertexNameInput == null || vertexNameInput.trim().isEmpty())
            return "Nama Titik Tempat tidak boleh kosong!";

        if (findVertex(vertexNameInput) != null)
            return "Titik Tempat '" + vertexNameInput + "' sudah ada!";

        Vertex vertex = new Vertex();
        vertex.setName(vertexNameInput);
        graph.addVertex(vertex);

        Object uiVertex;
        uiGraph.getModel().beginUpdate();
        try {
            uiVertex = uiGraph.insertVertex(
                    uiGraph.getDefaultParent(),
                    null,
                    vertexNameInput,
                    100, 100,
                    60, 60,
                    "shape=ellipse");
        } finally {
            uiGraph.getModel().endUpdate();
        }

        uiVertexMap.put(vertexNameInput, uiVertex);

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
     * @param vertexSourceInput      nama vertex asal
     * @param vertexDestinationInput nama vertex tujuan
     * @param weightInput            bobot edge (> 0)
     * @return pesan error jika gagal, atau {@code null} jika berhasil
     */
    public String addEdge(String vertexSourceInput, String vertexDestinationInput, String weightInput) {

        if (vertexSourceInput == null || vertexDestinationInput == null || weightInput == null)
            return "Input tidak boleh kosong!";

        // cek validasi angka
        int weight;
        try {
            weight = Integer.parseInt(weightInput);
        } catch (NumberFormatException e) {
            return "Bobot harus angka!";
        }

        Vertex vertexSource = findVertex(vertexSourceInput);
        Vertex vertexDestination = findVertex(vertexDestinationInput);
        String edgeKey1 = vertexSourceInput + "->" + vertexDestinationInput;
        String edgeKey2 = vertexDestinationInput + "->" + vertexSourceInput;

        if (vertexSource == null)
            return "Titik Tempat asal tidak ditemukan!";
        if (vertexDestination == null)
            return "Titik Tempat tujuan tidak ditemukan!";
        if (vertexSourceInput.equals(vertexDestinationInput))
            return "Titik Tempat tidak boleh menuju dirinya sendiri!";
        if (weight <= 0)
            return "Bobot harus lebih besar dari 0!";
        if (uiEdgeMap.containsKey(edgeKey1) || uiEdgeMap.containsKey(edgeKey2))
            return "Rute ini sudah ada!";

        // tambah edge pada graph baru
        graph.addEdge(vertexSource, vertexDestination, weight);

        // ADD KE UI
        uiGraph.getModel().beginUpdate();
        try {
            Object uiVertexSource = getUiVertex(vertexSourceInput);
            Object uiVertexDestination = getUiVertex(vertexDestinationInput);

            Object uiEdge = uiGraph.insertEdge(
                    uiGraph.getDefaultParent(),
                    null,
                    weight,
                    uiVertexSource,
                    uiVertexDestination,
                    "endArrow=none;strokeColor=black;"

            );

            uiEdgeMap.put(edgeKey1, uiEdge);
            uiEdgeMap.put(edgeKey2, uiEdge);
        } finally {
            uiGraph.getModel().endUpdate();
        }

        return null;
    }

    /**
     * Mencari objek Vertex pada model Graph berdasarkan nama.
     *
     * @param vertexNameInput nama vertex
     * @return objek {@link Vertex} jika ditemukan, atau {@code null} jika tidak ada
     */
    public Vertex findVertex(String vertexNameInput) {
        return graph.getVertices()
                .stream()
                .filter(v -> v.getName().equals(vertexNameInput))
                .findFirst()
                .orElse(null);
    }

    private String generateUndirectedKey(String a, String b) {
        // sorting agar "A-B" selalu sama meskipun inputnya "B,A"
        if (a.compareToIgnoreCase(b) < 0)
            return a + "-" + b;
        else
            return b + "-" + a;
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
        graph.clear();
        uiEdgeMap.clear();
        uiVertexMap.clear();

        uiGraph.getModel().beginUpdate();
        try {
            // Hapus semua vertex & edge di UI
            uiGraph.removeCells(
                    uiGraph.getChildCells(uiGraph.getDefaultParent(), true, true),
                    true);
        } finally {
            uiGraph.getModel().endUpdate();
        }
    }

}
