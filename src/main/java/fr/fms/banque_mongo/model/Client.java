package fr.fms.banque_mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Cette classe correspond à la collection MongoDB "clients"
@Document(collection = "clients")
public class Client {

    // Correspond au champ "_id" dans MongoDB
    @Id
    private String id;

    // Correspond au champ "nom"
    private String nom;

    // Correspond au champ "email"
    private String email;

    // Correspond au champ "telephone"
    private String telephone;

    // Constructeur vide obligatoire pour Spring
    public Client() {
    }

    // Constructeur avec tous les champs
    public Client(String id, String nom, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}