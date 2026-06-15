package pbodatabase.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pbodatabase.database.Database;
import pbodatabase.enums.UserStatus;
import pbodatabase.enums.UserType;
import pbodatabase.models.Mahasiswa;

public class MahasiswaRepository {
    private static final Connection conn;

    static {
        conn = Database.connect();
    }

    private static Mahasiswa mapRow(ResultSet rs) throws SQLException {
        return new Mahasiswa(
                rs.getString("nomor_induk"),
                rs.getString("nama"),
                rs.getString("email"),
                rs.getString("password"),
                UserStatus.valueOf(rs.getString("status")),
                UserType.valueOf(rs.getString("type")),
                rs.getString("tanggal_masuk"),
                rs.getString("tanggal_keluar"),
                rs.getString("kode_jurusan"));
    }

    // CREATE
    public static void addMahasiswa(Mahasiswa mhs) {
        String sql = """
                INSERT INTO "user" (
                    "nomor_induk", "email", "password", "status", "type", "nama",
                    "tanggal_masuk", "tanggal_keluar", "kode_jurusan"
                ) VALUES (?, ?, ?, ?::user_status, ?::user_type, ?, ?, ?, ?)
                """;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mhs.nomor_induk);
            stmt.setString(2, mhs.email);
            stmt.setString(3, mhs.password);
            stmt.setString(4, mhs.status.name());
            stmt.setString(5, mhs.type.name());
            stmt.setString(6, mhs.nama);
            stmt.setDate(7, java.sql.Date.valueOf(mhs.tanggal_masuk));
            stmt.setDate(8, mhs.tanggal_keluar != null ? java.sql.Date.valueOf(mhs.tanggal_keluar) : null);
            stmt.setString(9, mhs.kode_jurusan);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public static List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> result = new ArrayList<>();
        String sql = """
                SELECT "nomor_induk", "email", "password", "status", "type", "nama",
                       "tanggal_masuk", "tanggal_keluar", "kode_jurusan"
                FROM "user"
                WHERE "type" = 'MAHASISWA'::user_type
                """;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // READ BY NIM
    public static Mahasiswa getMahasiswaByNim(String nim) {
        String sql = """
                SELECT "nomor_induk", "email", "password", "status", "type", "nama",
                       "tanggal_masuk", "tanggal_keluar", "kode_jurusan"
                FROM "user"
                WHERE "nomor_induk" = ? AND "type" = 'MAHASISWA'::user_type
                """;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public static void updateMahasiswa(Mahasiswa mhs) {
        String sql = """
                UPDATE "user"
                SET "email" = ?, "password" = ?, "status" = ?::user_status, "nama" = ?,
                    "tanggal_masuk" = ?, "tanggal_keluar" = ?, "kode_jurusan" = ?
                WHERE "nomor_induk" = ? AND "type" = 'MAHASISWA'::user_type
                """;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mhs.email);
            stmt.setString(2, mhs.password);
            stmt.setString(3, mhs.status.name());
            stmt.setString(4, mhs.nama);
            stmt.setDate(5, java.sql.Date.valueOf(mhs.tanggal_masuk));
            stmt.setDate(6, mhs.tanggal_keluar != null ? java.sql.Date.valueOf(mhs.tanggal_keluar) : null);
            stmt.setString(7, mhs.kode_jurusan);
            stmt.setString(8, mhs.nomor_induk);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public static void deleteMahasiswa(String nim) {
        String sql = """
                DELETE FROM "user"
                WHERE "nomor_induk" = ? AND "type" = 'MAHASISWA'::user_type
                """;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}