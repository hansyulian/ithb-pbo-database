-- ============================================================
-- Enums
-- ============================================================

CREATE TYPE "user_type" AS ENUM (
    'MAHASISWA',
    'DOSEN_TETAP',
    'DOSEN_HONORER',
    'STAFF'
);

CREATE TYPE "user_status" AS ENUM (
    'AKTIF',
    'NONAKTIF',
    'CUTI'
);


-- ============================================================
-- Tables
-- ============================================================

CREATE TABLE IF NOT EXISTS "jurusan" (
    "kode_jurusan"  VARCHAR(10)     NOT NULL,
    "nama"          VARCHAR(100)    NOT NULL,
    PRIMARY KEY ("kode_jurusan")
);

CREATE TABLE IF NOT EXISTS "user" (
    "nomor_induk"       VARCHAR(20)     NOT NULL,
    "email"             VARCHAR(255)    NOT NULL,
    "password"          VARCHAR(255)    NOT NULL,
    "status"            "user_status"   NOT NULL DEFAULT 'AKTIF',
    "type"              "user_type"     NOT NULL,
    "nama"              VARCHAR(100)    NOT NULL,
    "tanggal_masuk"     DATE            NOT NULL,
    "tanggal_keluar"    DATE            NULL DEFAULT NULL,

    -- karyawan fields (dosen_tetap, dosen_honorer, staff)
    "gaji_pokok"        DECIMAL(15,2)   NULL DEFAULT NULL,  -- dosen_tetap, staff
    "honor_per_sks"     DECIMAL(15,2)   NULL DEFAULT NULL,  -- dosen_tetap, dosen_honorer

    -- mahasiswa fields
    "kode_jurusan"      VARCHAR(10)     NULL DEFAULT NULL,

    PRIMARY KEY ("nomor_induk"),
    UNIQUE ("email"),
    CONSTRAINT "fk_user_jurusan" FOREIGN KEY ("kode_jurusan")
        REFERENCES "jurusan" ("kode_jurusan")
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT "chk_tanggal" CHECK (
        "tanggal_keluar" IS NULL OR "tanggal_keluar" > "tanggal_masuk"
    ),
    CONSTRAINT "chk_mahasiswa_fields" CHECK (
        "type" != 'MAHASISWA' OR "kode_jurusan" IS NOT NULL
    ),
    CONSTRAINT "chk_dosen_tetap_fields" CHECK (
        "type" != 'DOSEN_TETAP' OR ("gaji_pokok" IS NOT NULL AND "honor_per_sks" IS NOT NULL)
    ),
    CONSTRAINT "chk_staff_fields" CHECK (
        "type" != 'STAFF' OR "gaji_pokok" IS NOT NULL
    ),
    CONSTRAINT "chk_dosen_honorer_fields" CHECK (
        "type" != 'DOSEN_HONORER' OR "honor_per_sks" IS NOT NULL
    )
);

CREATE TABLE IF NOT EXISTS "matakuliah" (
    "kode_matakuliah"   VARCHAR(10)     NOT NULL,
    "nama"              VARCHAR(100)    NOT NULL,
    "sks"               INTEGER         NOT NULL,
    "kode_jurusan"      VARCHAR(10)     NULL DEFAULT NULL,
    PRIMARY KEY ("kode_matakuliah"),
    CONSTRAINT "fk_matakuliah_jurusan" FOREIGN KEY ("kode_jurusan")
        REFERENCES "jurusan" ("kode_jurusan")
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS "mahasiswa_matakuliah" (
    "nomor_induk"       VARCHAR(20)     NOT NULL,
    "kode_matakuliah"   VARCHAR(10)     NOT NULL,
    "nilai"             DECIMAL(5,2)    NULL DEFAULT NULL,
    PRIMARY KEY ("nomor_induk", "kode_matakuliah"),
    CONSTRAINT "fk_mhs_mk_user" FOREIGN KEY ("nomor_induk")
        REFERENCES "user" ("nomor_induk") ON UPDATE NO ACTION ON DELETE CASCADE,
    CONSTRAINT "fk_mhs_mk_matakuliah" FOREIGN KEY ("kode_matakuliah")
        REFERENCES "matakuliah" ("kode_matakuliah") ON UPDATE NO ACTION ON DELETE CASCADE
);


-- ============================================================
-- Seed Data
-- ============================================================

INSERT INTO "jurusan" ("kode_jurusan", "nama") VALUES
    ('IF', 'Informatika'),
    ('SI', 'Sistem Informasi');

INSERT INTO "matakuliah" ("kode_matakuliah", "nama", "sks", "kode_jurusan") VALUES
    ('IF-101', 'Algoritma dan Pemrograman',      3, 'IF'),
    ('IF-102', 'Struktur Data',                  3, 'IF'),
    ('IF-103', 'Basis Data',                     3, 'IF'),
    ('IF-104', 'Pemrograman Berorientasi Objek', 3, 'IF'),
    ('SI-201', 'Sistem Informasi Manajemen',     3, 'SI'),
    ('SI-202', 'Analisis Proses Bisnis',         3, 'SI');

INSERT INTO "user" (
    "nomor_induk", "email", "password", "status", "type", "nama",
    "tanggal_masuk", "tanggal_keluar", "gaji_pokok", "honor_per_sks", "kode_jurusan"
) VALUES
    ('1125001', 'andi@kampus.ac.id',   'password', 'AKTIF', 'MAHASISWA',     'Andi Setiawan',   '2021-09-01', NULL, NULL,       NULL,      'IF'),
    ('1125002', 'budi@kampus.ac.id',   'password', 'AKTIF', 'MAHASISWA',     'Budi Santoso',    '2021-09-01', NULL, NULL,       NULL,      'IF'),
    ('1125004', 'dewi@kampus.ac.id',   'password', 'AKTIF', 'MAHASISWA',     'Dewi Kartika',    '2021-09-01', NULL, NULL,       NULL,      'SI'),
    ('NIK-001', 'rahman@kampus.ac.id', 'password', 'AKTIF', 'DOSEN_TETAP',   'Dr. Rahman',      '2015-01-01', NULL, 5000000.00, 150000.00, NULL),
    ('NIK-002', 'sari@kampus.ac.id',   'password', 'AKTIF', 'DOSEN_HONORER', 'Sari Dewi M.Kom', '2020-03-01', NULL, NULL,       100000.00, NULL),
    ('NIK-003', 'hendra@kampus.ac.id', 'password', 'AKTIF', 'STAFF',         'Hendra Wijaya',   '2018-06-01', NULL, 3000000.00, NULL,      NULL);

INSERT INTO "mahasiswa_matakuliah" ("nomor_induk", "kode_matakuliah", "nilai") VALUES
    ('1125001', 'IF-101', 90),
    ('1125001', 'IF-102', 73),
    ('1125001', 'IF-103', 85),
    ('1125002', 'IF-101', 56),
    ('1125002', 'IF-103', 23),
    ('1125002', 'IF-104', 40),
    ('1125004', 'SI-201', 66),
    ('1125004', 'SI-202', 55);