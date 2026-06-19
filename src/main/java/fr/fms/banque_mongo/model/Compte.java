package fr.fms.banque_mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entité représentant un compte bancaire stocké dans la collection "comptes".
 */
@Document(collection = "comptes")
public class Compte {

    // Identifiant du document (ex: "CPT001")
    @Id
    private String id;

    // Numéro du compte (ex: "FR001")
    private String numeroCompte;

    // Type de compte (ex: "COURANT", "EPARGNE")
    private String typeCompte;

    // Solde du compte
    private double solde;

    // Référence vers l'identifiant du client
    private String clientId;

    // Constructeur vide requis par Spring Data
    public Compte() {
    }

    // Constructeur complet
    public Compte(String id, String numeroCompte, String typeCompte, double solde, String clientId) {
        this.id = id;
        this.numeroCompte = numeroCompte;
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.clientId = clientId;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
