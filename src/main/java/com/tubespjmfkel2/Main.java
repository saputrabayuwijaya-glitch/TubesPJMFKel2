package com.tubespjmfkel2;

import javax.swing.SwingUtilities;

import com.tubespjmfkel2.view.GUI;

/**
 * Titik masuk (entry point) dari aplikasi.
 *
 * <p>
 * Class ini hanya berfungsi untuk menjalankan aplikasi Swing
 * dengan membuat instance {@link GUI} pada Event Dispatch Thread (EDT).
 * </p>
 *
 * <p>
 * Penting: Semua operasi GUI Swing harus dijalankan pada EDT
 * untuk menghindari masalah concurrency dan rendering.
 * </p>
 */
public class Main {

    /**
     * Metode utama (entry point) dari aplikasi.
     *
     * @param args Argumen baris perintah (tidak digunakan dalam aplikasi ini).
     */
    public static void main(String[] args) {
        // Menjalankan GUI pada Event Dispatch Thread
        SwingUtilities.invokeLater(GUI::new);
    }
}
