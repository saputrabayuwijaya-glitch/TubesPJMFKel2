# Tubes PJM F Kel 2 – Pencarian Rute Terpendek

Proyek ini adalah aplikasi **GUI untuk mencari rute terpendek menuju bengkel**, menggunakan **algoritma Dijkstra**
dengan menerapkan mvc arhitecture.

reference algorithm from https://www.baeldung.com/java-dijkstra

requirement: jdk 17 + jgraphx 3.9.3

# 🧠 Pola Arsitektur

Struktur ini mengikuti **pattern MVC (Model–View–Controller)**:

- **Model**
  Berisi data inti dan algoritma
- **View**
  Menampilkan hasil ke pengguna melalui tampilan grafis
- **Controller**
  Mengatur aliran data dan eksekusi logika

---

# 📦 Struktur Project

```
tubespjmfkel2
├── controller
│   ├── DijkstraController.java       # Menjalankan Dijkstra + komunikasi Model ↔ View
│   └── GraphController.java          # Mengatur graph (vertex, edge)
├── dto
│   └── DijkstraResult.java           # DTO hasil perhitungan (path + distance)
├── Main.java                         # Entry point aplikasi
├── model
│   ├── algorithm
│   │   └── Dijkstra.java             # Algoritma Dijkstra murni
│   └── entity
│       ├── Edge.java                 # Representasi edge & bobot
│       ├── Graph.java                # Struktur graf: adjacency, daftar vertex
│       └── Vertex.java               # Representasi vertex
└── view
    ├── GraphFrame.java               # Frame utama aplikasi
    └── GraphPanel.java               # Panel visual graf + background maps
```

## 1. `Main.java`

Merupakan kelas awal eksekusi aplikasi (`entry point`).
Kelas ini hanya berisi method:

```java
public static void main(String[] args)
```

yang bertanggung jawab memulai tampilan utama (`GUI`).

---

## 2. Package `controller`

Berisi kelas–kelas yang menjalankan logika penghubung antara **model** dan **view**.
Controller bertindak sebagai jembatan yang memproses perintah pengguna, memanggil model, dan mengirimkan hasil kembali
ke UI.


---

## 3. Package `dto`

Digunakan untuk menyimpan hasil eksekusi algoritma Dijkstra dan Bersifat **immutable**

---

## 4. Package `model`

Berisi seluruh **data dan logika murni aplikasi**, tanpa ketergantungan pada UI.


---

## 5. Package `view`

Berisi seluruh tampilan pengguna (UI), komponen–komponen grafis, dan utilitas tampilan.



---

