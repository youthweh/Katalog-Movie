# Katalog Movie 
 Aplikasi Katalog Movie dengan beberapa fitur

Project ini dibuat untuk menjadi salah satu syarat kelulusan pada kelas Dicoding (Menjadi Android Developer Expert). 

Kelas Online ini terdiri dari 5 Module yang setiap modulnya berisi beberapa sub-modul. Sub modul dibagi lagi menjadi penjelasan teori, latihan implementasi, dan kuis di setiap sub-modul. Dan di setiap akhir modul wajib mengirimkan project sesuai ketentuan yang berlaku, sampai pada tahap akhir di Submission Final Project. 
 
## Fitur Aplikasi

Aplikasi Movie Catalogue (Local Storage) dengan menambahkan beberapa fitur:


Configuration Changes
Syarat:
Aplikasi harus bisa menjaga data yang sudah dimuat ketika terjadi pergantian orientasi dari potrait ke landscape atau sebaliknya.


Widget
Syarat:
Pengguna dapat menampilkan widget dari film favorite ke halaman utama smartphone.
Tipe widget yang diterapkan adalah Stack Widget.

Reminder
Syarat:
Daily Reminder, mengirimkan notifikasi ke pengguna untuk kembali ke Aplikasi Movie Catalogue. Daily reminder harus selalu berjalan tiap jam 7 pagi.
Release Today Reminder, mengirimkan notifikasi ke pengguna semua film yang rilis hari ini (wajib menggunakan endpoint seperti yang telah disediakan pada bagian Resources di bawah). Release reminder harus selalu berjalan tiap jam 8 pagi.
Terdapat halaman pengaturan untuk mengaktifkan dan menonaktifkan reminder.

Aplikasi Favorite
Syarat:
Membuat aplikasi atau modul baru yang menampilkan daftar film favorite.
Menggunakan Content Provider sebagai mekanisme untuk mengakses data dari satu aplikasi ke aplikasi lain.

* Daftar Film
    * Syarat:
        * Terdapat 2 (dua) halaman yang menampilkan daftar film (**Movies** dan **Tv Show**).
        * Menggunakan  ``` Fragment ```  untuk menampung halaman **Movies** dan **Tv Show.**
        * Menggunakan  ``` RecyclerView ```  untuk menampilkan daftar film dengan jumlah **minimal 10 item.**
        * Menggunakan  ``` TabLayout ``` ,  ``` BottomNavigationView ``` , atau yang lainnya sebagai navigasi antara halaman **Movies** dan **Tv Show.**   
        * Menampilkan indikator loading ketika data sedang dimuat.

* Detail Film
    * Syarat: 
        * Menampilkan poster dan informasi film pada halaman detail film. 
        * Menggunakan ```  Parcelable ```  sebagai interfaces dari obyek yang akan dikirimkan antar ```  Activity  ``` atau ```  Fragment ``` .
        * Menggunakan ``` ConstraintLayout ``` untuk menyusun layout.   
        * Menampilkan indikator loading ketika data sedang dimuat. 

* Localization
    * Syarat:
        * Aplikasi harus mendukung bahasa Indonesia dan bahasa Inggris.

* Favorite Film
    * Syarat:
        * Dapat menyimpan film ke database favorite. 
        * Dapat menghapus film dari database favorite.
        * Terdapat halaman untuk menampilkan daftar Favorite Movies.
        * Terdapat halaman untuk menampilkan daftar Favorite Tv Show.
        * Menampilkan halaman detail dari daftar Favorite.

* Pencarian Film
    * Syarat:
        * Pengguna dapat melakukan pencarian Movies.
        * Pengguna dapat melakukan pencarian Tv Show.
        
* Pencarian Film
 
### Author
_Fiona Stefani Limin_
