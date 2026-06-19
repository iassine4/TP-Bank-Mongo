package fr.fms.banque_mongo.repository;

import fr.fms.banque_mongo.model.Operation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Repository Spring Data pour l'entité Operation.
 */
public interface OperationRepository extends MongoRepository<Operation, String> {

    // Récupère les opérations par type (DEPOT, RETRAIT, VIREMENT)
    List<Operation> findByType(String type);

    // Récupère les opérations liées directement à un compte (compteId)
    List<Operation> findByCompteId(String compteId);

    // Récupère les opérations où le compte est source (virements)
    List<Operation> findByCompteSourceId(String compteSourceId);

    // Récupère les opérations où le compte est destinataire (virements)
    List<Operation> findByCompteDestinataireId(String compteDestinataireId);
}
