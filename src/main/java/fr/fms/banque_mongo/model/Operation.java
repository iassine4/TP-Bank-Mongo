package fr.fms.banque_mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entité représentant une opération bancaire stockée dans la collection
 * "operations".
 */
@Document(collection = "operations")
public class Operation {

    // Identifiant de l'opération (ex: "OP001")
    @Id
    private String id;

    // Type d'opération (DEPOT, RETRAIT, VIREMENT)
    private String type;

    // Montant de l'opération
    private double montant;

    // Date et heure de l'opération
    private LocalDateTime dateOperation;

    // Compte concerné (pour DEPOT/RETRAIT)
    private String compteId;

    // Compte source (pour VIREMENT)
    private String compteSourceId;

    // Compte destinataire (pour VIREMENT)
    private String compteDestinataireId;

    // Description libre de l'opération
    private String description;

    // Constructeur vide requis par Spring Data
    public Operation() {
    }

    // Constructeur complet
    public Operation(String id, String type, double montant, LocalDateTime dateOperation, String compteId,
            String compteSourceId, String compteDestinataireId, String description) {
        this.id = id;
        this.type = type;
        this.montant = montant;
        this.dateOperation = dateOperation;
        this.compteId = compteId;
        this.compteSourceId = compteSourceId;
        this.compteDestinataireId = compteDestinataireId;
        this.description = description;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getCompteId() {
        return compteId;
    }

    public void setCompteId(String compteId) {
        this.compteId = compteId;
    }

    public String getCompteSourceId() {
        return compteSourceId;
    }

    public void setCompteSourceId(String compteSourceId) {
        this.compteSourceId = compteSourceId;
    }

    public String getCompteDestinataireId() {
        return compteDestinataireId;
    }

    public void setCompteDestinataireId(String compteDestinataireId) {
        this.compteDestinataireId = compteDestinataireId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
