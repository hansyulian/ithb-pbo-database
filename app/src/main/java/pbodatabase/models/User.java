package pbodatabase.models;

import pbodatabase.enums.UserStatus;
import pbodatabase.enums.UserType;

public class User {
  public String nomor_induk;
  public String nama;
  public String email;
  public String password;
  public UserStatus status;
  public String tanggal_masuk;
  public String tanggal_keluar;
  public UserType type;

  public User(String nomor_induk, String nama, String email, String password, UserStatus status, UserType type,
      String tanggal_masuk,
      String tanggal_keluar) {
    this.nomor_induk = nomor_induk;
    this.nama = nama;
    this.email = email;
    this.password = password;
    this.status = status;
    this.type = type;
    this.tanggal_masuk = tanggal_masuk;
    this.tanggal_keluar = tanggal_keluar;
  }
}
