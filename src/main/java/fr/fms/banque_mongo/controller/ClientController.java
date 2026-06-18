package fr.fms.banque_mongo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.fms.banque_mongo.model.Client;
import fr.fms.banque_mongo.repository.ClientRepository;

import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Cette annotation indique que cette classe reçoit des requêtes HTTP
@RestController

// Toutes les routes de ce controller commenceront par /api/clients
@RequestMapping("/api/clients")
public class ClientController {

    // Le repository permet d'accéder à MongoDB
    private final ClientRepository clientRepository;

    private final MongoTemplate mongoTemplate;

    private final Environment environment;

    // Constructeur : Spring injecte automatiquement le repository ici
    public ClientController(ClientRepository clientRepository, MongoTemplate mongoTemplate, Environment environment) {
        this.clientRepository = clientRepository;
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
    }

    // GET /api/clients
    // Cette méthode retourne tous les clients
    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // GET /api/clients/debug/count
    // Cette méthode compte le nombre de clients vus par Spring
    @GetMapping("/debug/count")
    public long countClients() {
        return clientRepository.count();
    }

    // GET /api/clients/debug
    // Cette route montre ce que Spring voit réellement dans MongoDB
    @GetMapping("/debug")
    public Map<String, Object> debugMongo() {
        Map<String, Object> debug = new HashMap<>();

        String mongoUri = environment.getProperty("spring.data.mongodb.uri");

        debug.put("databaseName", mongoTemplate.getDb().getName());
        debug.put("collections", mongoTemplate.getCollectionNames());
        debug.put("repositoryCount", clientRepository.count());
        debug.put("rawClientsCount", mongoTemplate.getCollection("clients").countDocuments());

        debug.put("databaseProperty", environment.getProperty("spring.data.mongodb.database"));
        debug.put("mongoUriPresent", mongoUri != null);
        debug.put("mongoUriContainsBanqueTp", mongoUri != null && mongoUri.contains("/banque_tp"));

        return debug;
    }

    // GET /api/clients/{id}
    // Cette méthode retourne un client par son identifiant
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        return clientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/clients
    // Cette méthode crée un nouveau client
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    // PUT /api/clients/{id}
    // Cette méthode modifie un client existant
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable String id, @RequestBody Client newClientData) {
        return clientRepository.findById(id)
                .map(existingClient -> {
                    existingClient.setNom(newClientData.getNom());
                    existingClient.setEmail(newClientData.getEmail());
                    existingClient.setTelephone(newClientData.getTelephone());

                    Client updatedClient = clientRepository.save(existingClient);
                    return ResponseEntity.ok(updatedClient);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/clients/{id}
    // Cette méthode supprime un client par son identifiant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}