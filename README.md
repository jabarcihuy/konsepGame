# Syntax Blade : C++ Logic Terminal 💻🤖

Syntax Blade adalah game kuis logika pemrograman murni yang dibangun menggunakan Java Swing. Game ini memiliki estetika terminal siberpunk retro dan menguji pengetahuan pemrograman C++ Anda secara *real-time* di bawah tekanan!

## 🚀 Fitur

- **Teka-teki Logika C++ Dinamis:** 
  - **Tier 1:** Logika Boolean Dasar, Aritmatika, Modulus, dan Operator Bitwise.
  - **Tier 2:** Pointer, Reference, dan Perulangan (`for`, `while`).
  - **Tier 3:** Memori Dinamis (`new`/`delete`), Vector, Struct, String, dan Array 2D.
- **Antarmuka (UI) Cyberpunk / Hacker:** 
  - *Rendering* khusus menggunakan `Graphics2D` dengan efek *scanline* monitor CRT retro.
  - Mesin *Syntax Highlighting* kustom yang dibangun langsung ke dalam Java Swing.
  - Kursor terminal yang berkedip dan palet warna neon yang imersif.
- **Arsitektur Multi-Frame:** 
  - Transisi yang mulus antara layar Menu Utama, Lingkungan Eksekusi (Game), dan layar Game Over menggunakan `CardLayout`.
- **Sistem Nyawa (Lives) & Skor:** 
  - Anda memulai dengan **3 Nyawa (♥)**.
  - Menjawab dengan benar akan memberikan poin berdasarkan tingkat kesulitan (tier).
  - Menjawab salah atau membiarkan waktu habis akan mengurangi 1 nyawa. 

## 🛠️ Persyaratan
- Java Development Kit (JDK) 8 atau lebih baru.
- Linux / Windows / macOS (Aplikasi Java Swing lintas platform).

## 🎮 Cara Menjalankan

Anda dapat melakukan kompilasi dan menjalankan proyek ini dengan mudah menggunakan skrip bawaan:

**Untuk Windows:**
Klik ganda (*double-click*) pada file `run.bat`.

**Untuk Linux / macOS:**
Buka terminal dan jalankan:
```bash
chmod +x run.sh
./run.sh
```

### ☕ Menjalankan di Apache NetBeans (IDE)
Karena proyek ini diinisialisasi menggunakan Apache NetBeans, proyek ini 100% kompatibel dan siap dijalankan tanpa konfigurasi tambahan (Plug and Play).
1. Buka IDE Apache NetBeans.
2. Pilih **File > Open Project** (atau tekan `Ctrl+Shift+O`).
3. Pilih folder `AlgoRythm` (NetBeans akan mengenalinya secara otomatis melalui direktori `nbproject`).
4. Pada jendela *Projects*, klik kanan pada proyek **AlgoRythm** lalu pilih **Clean and Build**.
5. Klik tombol hijau **Run Project** (atau tekan `F6`). GUI Cyberpunk akan langsung terbuka!

## 📂 Arsitektur Proyek (MVC & Design Patterns)

- **Factory Pattern (`PuzzleFactory.java`):** Secara dinamis menghasilkan objek teka-teki polimorfik (`BooleanPuzzle`, `LoopPuzzle`, `ArrayPuzzle`) berdasarkan skor Anda saat ini.
- **State Pattern (`LogicPhaseState.java`):** Mengenkapsulasi *timer* fase logika, *buffer* input, dan validasi *state*.
- **Pemisahan Model-View:** Inti logika (`engine.logic`) sepenuhnya dipisahkan dari tampilan dan kontroler (`algorythm.swing`).

---
*Dibuat untuk Tugas Mata Kuliah High-Level OOP.*
