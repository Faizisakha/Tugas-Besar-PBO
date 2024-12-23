// Koneksi ke Database
import java.sql.*;
import java.util.*;

// Kelas utama
public class manajemen_taman {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TamanManager tamanManager = new TamanManager();
        PerawatanManager perawatanManager = new PerawatanManager();

        System.out.println("Sistem Manajemen Jadwal Perawatan Taman");
        int pilihan;

        do {
            System.out.println("\nPilih opsi:");
            System.out.println("1. Tambah Taman");
            System.out.println("2. Lihat Semua Taman");
            System.out.println("3. Edit Taman");
            System.out.println("4. Hapus Taman");
            System.out.println("5. Tambah Jadwal Perawatan");
            System.out.println("6. Lihat Jadwal Perawatan");
            System.out.println("7. Keluar");
            System.out.print("Pilihan Anda: ");
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    System.out.print("Nama Taman: ");
                    String namaTaman = scanner.next();
                    System.out.print("Lokasi Taman: ");
                    String lokasi = scanner.next();
                    System.out.print("Luas Taman (m2): ");
                    double luas = scanner.nextDouble();
                    tamanManager.tambahTaman(new Taman(namaTaman, lokasi, luas));
                    break;

                case 2:
                    tamanManager.lihatSemuaTaman();
                    break;

                case 3:
                    System.out.print("ID Taman yang akan diedit: ");
                    int idEdit = scanner.nextInt();
                    System.out.print("Nama Taman Baru: ");
                    String namaBaru = scanner.next();
                    System.out.print("Lokasi Taman Baru: ");
                    String lokasiBaru = scanner.next();
                    System.out.print("Luas Taman Baru (m2): ");
                    double luasBaru = scanner.nextDouble();
                    tamanManager.editTaman(idEdit, namaBaru, lokasiBaru, luasBaru);
                    break;

                case 4:
                    System.out.print("ID Taman yang akan dihapus: ");
                    int idHapus = scanner.nextInt();
                    tamanManager.hapusTaman(idHapus);
                    break;

                case 5:
                    System.out.print("ID Taman: ");
                    int idTaman = scanner.nextInt();
                    System.out.print("Jenis Perawatan: ");
                    String jenisPerawatan = scanner.next();
                    System.out.print("Tanggal Perawatan (YYYY-MM-DD): ");
                    String tanggal = scanner.next();
                    perawatanManager.tambahJadwal(idTaman, jenisPerawatan, tanggal);
                    break;

                case 6:
                    perawatanManager.lihatJadwal();
                    break;

                case 7:
                    System.out.println("Keluar dari sistem.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 7);

        scanner.close();
    }
}

// Kelas Taman
class Taman {
    private String nama;
    private String lokasi;
    private double luas;

    public Taman(String nama, String lokasi, double luas) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.luas = luas;
    }

    public String getNama() {
        return nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public double getLuas() {
        return luas;
    }
}

// Kelas untuk manajemen taman
class TamanManager {
    public void tambahTaman(Taman taman) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO taman (nama_taman, lokasi, luas) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, taman.getNama());
            stmt.setString(2, taman.getLokasi());
            stmt.setDouble(3, taman.getLuas());
            stmt.executeUpdate();
            System.out.println("Taman berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan taman: " + e.getMessage());
        }
    }

    public void lihatSemuaTaman() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM taman";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Daftar Taman:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id_taman") + ", Nama: " + rs.getString("nama_taman") +
                        ", Lokasi: " + rs.getString("lokasi") + ", Luas: " + rs.getDouble("luas") + " m2");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data taman: " + e.getMessage());
        }
    }

    public void editTaman(int id, String namaBaru, String lokasiBaru, double luasBaru) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE taman SET nama_taman = ?, lokasi = ?, luas = ? WHERE id_taman = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, namaBaru);
            stmt.setString(2, lokasiBaru);
            stmt.setDouble(3, luasBaru);
            stmt.setInt(4, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Taman berhasil diperbarui.");
            } else {
                System.out.println("Taman dengan ID tersebut tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengedit taman: " + e.getMessage());
        }
    }

    public void hapusTaman(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM taman WHERE id_taman = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Taman berhasil dihapus.");
            } else {
                System.out.println("Taman dengan ID tersebut tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghapus taman: " + e.getMessage());
        }
    }
}

// Kelas untuk manajemen perawatan
class PerawatanManager {
    public void tambahJadwal(int idTaman, String jenisPerawatan, String tanggal) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO jadwal (id_taman, jenis_perawatan, tanggal) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTaman);
            stmt.setString(2, jenisPerawatan);
            stmt.setString(3, tanggal);
            stmt.executeUpdate();
            System.out.println("Jadwal perawatan berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan jadwal: " + e.getMessage());
        }
    }

    public void lihatJadwal() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM jadwal";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Daftar Jadwal Perawatan:");
            while (rs.next()) {
                System.out.println("ID Jadwal: " + rs.getInt("id_jadwal") + ", ID Taman: " + rs.getInt("id_taman") +
                        ", Jenis Perawatan: " + rs.getString("jenis_perawatan") + ", Tanggal: " + rs.getString("tanggal"));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data jadwal: " + e.getMessage());
        }
    }
}

// Kelas untuk koneksi database
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/manajemen_taman";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
