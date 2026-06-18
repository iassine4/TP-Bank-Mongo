package fr.fms.banque_mongo.repository;

import fr.fms.banque_mongo.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// Le repository permet de communiquer avec la collection "clients"
public interface ClientRepository extends MongoRepository<Client, String> {

    // Spring crée automatiquement une requête MongoDB à partir du nom de la méthode
    // Recherche automatique par nom
    List<Client> findByNom(String nom);

    List<Client> findAll();
}
