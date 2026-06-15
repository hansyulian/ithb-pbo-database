package pbodatabase.models;

import pbodatabase.enums.UserStatus;
import pbodatabase.enums.UserType;

public class Mahasiswa extends User {
  public String nim;
  public String kode_jurusan;

  public Mahasiswa(String nim, String nama, String email, String password, UserStatus status, UserType type,
      String tanggal_masuk, String tanggal_keluar, String kode_jurusan) {
    super(nim, nama, email, password, status, type, tanggal_masuk, tanggal_keluar);
    this.nim = nim;
    this.kode_jurusan = kode_jurusan;
  }
}
