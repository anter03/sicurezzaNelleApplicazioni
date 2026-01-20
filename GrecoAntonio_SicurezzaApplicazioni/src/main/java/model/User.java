package model;

import java.io.Serializable;

public class User implements Serializable {
    // Req 3.7: Riduzione scope variabili (private) e information hiding
    private int id;
    private String email;
    private String passwordHash;
    private String salt;

    public User() {}

    // Costruttore difensivo: inizializza lo stato in modo consistente
    public User(String email, String passwordHash, String salt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    // Getter e Setter standard...
    public String getEmail() { return email; }

    // NOTA: Non inserire mai la password in chiaro nel modello, nemmeno temporaneamente.
    public void setEmail(String email) { this.email = email; }

    // ... altri getter/setter
}