package fr.fms.banque_mongo.repository;

import fr.fms.banque_mongo.model.Compte;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Repository Spring Data pour l'entité Compte.
 */
public interface CompteRepository extends MongoRepository<Compte, String> {

    // Récupère tous les comptes d'un client
    List<Compte> findByClientId(String clientId);

    // Récupère tous les comptes d'un certain type
    List<Compte> findByTypeCompte(String typeCompte);
}
