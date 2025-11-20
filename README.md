# Tubes PJM F Kel 2 – Pencarian Rute Terpendek

Proyek ini adalah aplikasi **GUI untuk mencari rute terpendek menuju bengkel**, menggunakan **algoritma Dijkstra** dengan menerapkan mvc arhitecture.

reference algorithm from https://www.baeldung.com/java-dijkstra

# Struktur Project

```
└── tubespjmfkel2
    ├── controller
    │   ├── DijkstraController.java     # Mengatur logika algoritma Dijkstra, komunikasi antara model dan view
    │   └── GraphController.java        # Mengatur graf secara umum, menyimpan node, edge, dan menyediakan data untuk UI
    ├── dto
    │   └── DijkstraResult.java         # Data Transfer Object untuk menampung hasil algoritma Dijkstra (path dan jarak)
    ├── Main.java                        # Titik masuk aplikasi, menjalankan AppWindow pada Event Dispatch Thread
    ├── model
    │   ├── Dijkstra.java                # Implementasi algoritma Dijkstra murni (logika perhitungan path terpendek)
    │   ├── Graph.java                   # Representasi struktur graf (nodes, edges)
    │   └── Node.java                    # Representasi node pada graf
    └── view
        ├── AppWindow.java               # Frame utama aplikasi, menampung GraphPanel dan kontrol GUI
        ├── GraphPanel.java              # Panel yang menampilkan graf menggunakan JGraphX
        └── PathHighlighter.java         # Utility untuk menyorot jalur tertentu pada graf (highlight path)

```

requirement: jdk 17 + jgraphx 3.9.3

# task

ui

1. tambah maps sebagai background
2. dll

laporan
