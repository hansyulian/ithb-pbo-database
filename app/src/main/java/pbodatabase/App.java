package pbodatabase;

import java.util.List;
import java.util.Scanner;

import pbodatabase.enums.UserStatus;
import pbodatabase.enums.UserType;
import pbodatabase.models.Mahasiswa;
import pbodatabase.repository.MahasiswaRepository;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan NIM: ");
        String nim = scanner.nextLine();

        System.out.print("Masukkan Nama: ");
        String nama = scanner.nextLine();

        System.out.print("Masukkan Email: ");
        String email = scanner.nextLine();

        System.out.print("Masukkan Password: ");
        String password = scanner.nextLine();

        System.out.print("Masukkan Kode Jurusan: ");
        String kodeJurusan = scanner.nextLine();

        System.out.print("Masukkan Tanggal Masuk (YYYY-MM-DD): ");
        String tanggalMasuk = scanner.nextLine();

        Mahasiswa mhs = new Mahasiswa(
                nim, nama, email, password,
                UserStatus.AKTIF, UserType.MAHASISWA,
                tanggalMasuk, null,
                kodeJurusan);
        MahasiswaRepository.addMahasiswa(mhs);

        List<Mahasiswa> allMahasiswa = MahasiswaRepository.getAllMahasiswa();
        System.out.println("\n📋 Daftar Mahasiswa:");
        for (Mahasiswa m : allMahasiswa) {
            System.out.println(m.nomor_induk + " - " + m.nama + " (" + m.kode_jurusan + ")");
        }

        scanner.close();
    }
}