package utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordUtils {

    private static final int ITERATIONS = 65536; // Rende l'attacco brute-force costoso
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    // Genera un Salt casuale crittograficamente sicuro (Req 3.6)
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Genera l'Hash della password usando il Salt
    public static String hashPassword(String password, String salt) {
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Base64.getDecoder().decode(salt);

        PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_LENGTH);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Req 3.7: Gestione errori senza esporre stack trace sensibili all'utente
            throw new RuntimeException("Errore critico nella sicurezza delle password", e);
        } finally {
            // Best Practice: Pulizia della memoria (opzionale ma consigliata per char[])
            spec.clearPassword();
        }
    }

    // Verifica password (usata nel Login RF2)
    public static boolean verifyPassword(String providedPassword, String storedHash, String storedSalt) {
        String calculatedHash = hashPassword(providedPassword, storedSalt);
        return calculatedHash.equals(storedHash);
    }
}