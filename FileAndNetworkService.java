import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileAndNetworkService {

    // 1. Path Traversal / Unvalidated File Path
    public void readFile(String filename) {
        try {
            // โค้ดรับ path/filename จากภายนอกโดยตรง ทำให้ผู้โจมตีใส่ ../../ เพื่ออ่านไฟล์ระบบได้
            File file = new File("/var/app/data/" + filename);
            FileInputStream stream = new FileInputStream(file);
            
            byte[] data = new byte[(int) file.length()];
            stream.read(data);
            stream.close();
            
            System.out.println("File Content: " + new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. Weak Cryptographic Hash (MD5)
    public String hashUserPassword(String password) {
        try {
            // การใช้ MD5 ในการเข้ารหัสรหัสผ่านถือว่าไม่ปลอดภัย (Weak Hashing Algorithm)
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. Server-Side Request Forgery (SSRF)
    public void fetchRemoteData(String urlString) {
        try {
            // รับ URL จากภายนอกโดยตรงโดยไม่มีการ Validate/Whitelist ทำให้อาจโดนยิง request ไปยัง Internal IP ได้
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}