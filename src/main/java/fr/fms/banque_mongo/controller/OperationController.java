package fr.fms.banque_mongo.controller;

import fr.fms.banque_mongo.model.Compte;
import fr.fms.banque_mongo.model.Operation;
import fr.fms.banque_mongo.repository.CompteRepository;
import fr.fms.banque_mongo.repository.OperationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller REST pour gérer les opérations bancaires (DEPOT, RETRAIT,
 * VIREMENT).
 */
@RestController
@RequestMapping("/api/operations")
public class OperationController {

    private final OperationRepository operationRepository;
    private final CompteRepository compteRepository;

    public OperationController(OperationRepository operationRepository, CompteRepository compteRepository) {
        this.operationRepository = operationRepository;
        this.compteRepository = compteRepository;
    }

    // DTO pour les requêtes POST (montant + description)
    public static class OperationRequest {
        private double montant;
        private String description;

        public OperationRequest() {
        }

        public double getMontant() {
            return montant;
        }

        public void setMontant(double montant) {
            this.montant = montant;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    // Lister toutes les opérations
    @GetMapping
    public List<Operation> getAll() {
        return operationRepository.findAll();
    }

    // Compter les opérations (endpoint de debug)
    @GetMapping("/debug/count")
    public long count() {
        return operationRepository.count();
    }

    // Trouver une opération par id
    @GetMapping("/{id}")
    public ResponseEntity<Operation> getById(@PathVariable String id) {
        Optional<Operation> opt = operationRepository.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lister les opérations par type (DEPOT, RETRAIT, VIREMENT)
    @GetMapping("/type/{type}")
    public List<Operation> getByType(@PathVariable String type) {
        return operationRepository.findByType(type);
    }

    // Historique complet d'un compte (toutes les opérations où le compte est
    // impliqué)
    @GetMapping("/compte/{compteId}")
    public ResponseEntity<List<Operation>> getHistoriqueCompte(@PathVariable String compteId) {
        // Vérifier que le compte existe
        if (!compteRepository.existsById(compteId)) {
            return ResponseEntity.notFound().build();
        }

        List<Operation> byCompte = operationRepository.findByCompteId(compteId);
        List<Operation> bySource = operationRepository.findByCompteSourceId(compteId);
        List<Operation> byDest = operationRepository.findByCompteDestinataireId(compteId);

        // Fusionner et dédupliquer par id en conservant l'ordre
        Map<String, Operation> map = new LinkedHashMap<>();
        for (Operation o : byCompte)
            map.put(o.getId(), o);
        for (Operation o : bySource)
            map.put(o.getId(), o);
        for (Operation o : byDest)
            map.put(o.getId(), o);

        return ResponseEntity.ok(new ArrayList<>(map.values()));
    }

    // Effectuer un dépôt
    @PostMapping("/depot/{compteId}")
    public ResponseEntity<?> depot(@PathVariable String compteId, @RequestBody OperationRequest request) {
        Optional<Compte> opt = compteRepository.findById(compteId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte introuvable");
        }
        if (request.getMontant() <= 0) {
            return ResponseEntity.badRequest().body("Le montant doit être strictement positif");
        }

        Compte compte = opt.get();
        compte.setSolde(compte.getSolde() + request.getMontant());
        compteRepository.save(compte);

        Operation op = new Operation();
        op.setType("DEPOT");
        op.setMontant(request.getMontant());
        op.setDateOperation(LocalDateTime.now());
        op.setCompteId(compteId);
        op.setDescription(request.getDescription());
        Operation saved = operationRepository.save(op);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Effectuer un retrait
    @PostMapping("/retrait/{compteId}")
    public ResponseEntity<?> retrait(@PathVariable String compteId, @RequestBody OperationRequest request) {
        Optional<Compte> opt = compteRepository.findById(compteId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte introuvable");
        }
        if (request.getMontant() <= 0) {
            return ResponseEntity.badRequest().body("Le montant doit être strictement positif");
        }

        Compte compte = opt.get();
        if (compte.getSolde() < request.getMontant()) {
            return ResponseEntity.badRequest().body("Solde insuffisant");
        }

        compte.setSolde(compte.getSolde() - request.getMontant());
        compteRepository.save(compte);

        Operation op = new Operation();
        op.setType("RETRAIT");
        op.setMontant(request.getMontant());
        op.setDateOperation(LocalDateTime.now());
        op.setCompteId(compteId);
        op.setDescription(request.getDescription());
        Operation saved = operationRepository.save(op);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Effectuer un virement entre deux comptes
    @PostMapping("/virement/{compteSourceId}/{compteDestinataireId}")
    public ResponseEntity<?> virement(@PathVariable String compteSourceId, @PathVariable String compteDestinataireId,
            @RequestBody OperationRequest request) {
        if (compteSourceId.equals(compteDestinataireId)) {
            return ResponseEntity.badRequest()
                    .body("Le compte source et le compte destinataire doivent être différents");
        }

        Optional<Compte> optSource = compteRepository.findById(compteSourceId);
        Optional<Compte> optDest = compteRepository.findById(compteDestinataireId);
        if (!optSource.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte source introuvable");
        }
        if (!optDest.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte destinataire introuvable");
        }
        if (request.getMontant() <= 0) {
            return ResponseEntity.badRequest().body("Le montant doit être strictement positif");
        }

        Compte source = optSource.get();
        Compte dest = optDest.get();
        if (source.getSolde() < request.getMontant()) {
            return ResponseEntity.badRequest().body("Solde insuffisant sur le compte source");
        }

        // Effectuer les modifications de solde
        source.setSolde(source.getSolde() - request.getMontant());
        dest.setSolde(dest.getSolde() + request.getMontant());

        // Sauvegarder les deux comptes
        compteRepository.save(source);
        compteRepository.save(dest);

        // Créer l'opération de type VIREMENT
        Operation op = new Operation();
        op.setType("VIREMENT");
        op.setMontant(request.getMontant());
        op.setDateOperation(LocalDateTime.now());
        op.setCompteSourceId(compteSourceId);
        op.setCompteDestinataireId(compteDestinataireId);
        op.setDescription(request.getDescription());
        Operation saved = operationRepository.save(op);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Supprimer une opération (ne modifie pas les soldes des comptes)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!operationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        operationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
