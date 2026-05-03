# Kunci Jawaban AlgoRythm (Syntax Blade) 🔑

Dokumen ini berisi daftar soal tetap dan kunci jawaban untuk setiap tier dalam game **AlgoRythm**.

---

## 🟢 TIER 1: Basic Logic & Arithmetic
Fokus pada operasi dasar C++, alur logika boolean, dan aritmatika.

| No | Soal (C++) | Kunci Jawaban | Penjelasan |
|:---:|:---|:---:|:---|
| 1 | `int a = 10, b = 3; std::cout << (a + b);` | **13** | Penjumlahan integer 10 + 3. |
| 2 | `int a = 15, b = 4; std::cout << (a % b);` | **3** | Operasi Modulus (Sisa bagi dari 15/4). |
| 3 | `bool x = true, y = false; std::cout << (x && y);` | **0** | Logic AND: true (1) && false (0) = 0. |
| 4 | `int x = 16; std::cout << (x >> 2);` | **4** | Right Shift: 16 / (2^2) = 4. |
| 5 | `int a = 7, b = 2; std::cout << (a / b);` | **3** | Pembagian integer membuang angka di belakang koma. |

---

## 🟡 TIER 2: Pointers, Loops & Arrays
Fokus pada manajemen memori dasar, perulangan, dan akses array.

| No | Soal (C++) | Kunci Jawaban | Penjelasan |
|:---:|:---|:---:|:---|
| 1 | `int x = 10; int& ref = x; ref = 25; std::cout << x;` | **25** | Reference bertindak sebagai alias untuk variabel asli. |
| 2 | `int a = 5; int* ptr = &a; *ptr = *ptr + 10; std::cout << a;` | **15** | Dereferencing pointer untuk mengubah nilai variabel asal. |
| 3 | `int sum = 0; for(int i=0; i<4; ++i) { sum += i; }` | **6** | Iterasi: 0 + 1 + 2 + 3 = 6. |
| 4 | `int arr[3] = {10, 20, 30}; std::cout << arr[1];` | **20** | Index array dimulai dari 0. Index 1 adalah elemen kedua. |
| 5 | `int x = 0; while(x < 10) x += 3; std::cout << x;` | **12** | Loop berhenti saat x=12 (karena 12 tidak lagi < 10). |

---

## 🔴 TIER 3: Data Structures & Dynamic Memory
Fokus pada STL Vector, Dynamic Allocation, Struct, dan Array Multi-dimensi.

| No | Soal (C++) | Kunci Jawaban | Penjelasan |
|:---:|:---|:---:|:---|
| 1 | `std::vector<int> v; v.push_back(10); v.push_back(20); std::cout << v.size();` | **2** | Menghitung jumlah elemen dalam vector. |
| 2 | `int* p = new int(50); int v = *p / 2; delete p; std::cout << v;` | **25** | Alokasi dinamis dan operasi matematika pada nilainya. |
| 3 | `struct Box { int w; }; Box b; b.w = 99; Box* p = &b; std::cout << p->w;` | **99** | Menggunakan operator panah (->) untuk akses member lewat pointer. |
| 4 | `std::string s = "ALGO"; std::cout << s[2];` | **G** | Mengambil karakter ke-3 (index 2) dari string. |
| 5 | `int a[2][2] = {{1,2},{3,4}}; std::cout << a[0][1];` | **2** | Akses array 2D: Baris 0, Kolom 1. |

---

*Catatan: Masukkan jawaban tanpa spasi tambahan. Jawaban huruf (seperti pada Tier 3 No 4) bersifat Case-Sensitive.*
