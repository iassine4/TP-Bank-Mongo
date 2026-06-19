package fr.fms.banque_mongo.controller;

import fr.fms.banque_mongo.model.Compte;
import fr.fms.banque_mongo.repository.CompteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST pour gérer les comptes.
 */
@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteRepository compteRepository;

    public CompteController(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    // Lister tous les comptes
    @GetMapping
    public List<Compte> getAll() {
        return compteRepository.findAll();
    }

    // Compter les comptes (endpoint de debug)
    @GetMapping("/debug/count")
    public long count() {
        return compteRepository.count();
    }

    // Trouver un compte par son id
    @GetMapping("/{id}")
    public ResponseEntity<Compte> getById(@PathVariable String id) {
        Optional<Compte> opt = compteRepository.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lister les comptes d'un client
    @GetMapping("/client/{clientId}")
    public List<Compte> getByClientId(@PathVariable String clientId) {
        return compteRepository.findByClientId(clientId);
    }

    // Créer un compte
    @PostMapping
    public ResponseEntity<Compte> create(@RequestBody Compte compte) {
        Compte saved = compteRepository.save(compte);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Mettre à jour un compte existant
    @PutMapping("/{id}")
    public ResponseEntity<Compte> update(@PathVariable String id, @RequestBody Compte compte) {
        Optional<Compte> opt = compteRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Compte existing = opt.get();
        existing.setNumeroCompte(compte.getNumeroCompte());
        existing.setTypeCompte(compte.getTypeCompte());
        existing.setSolde(compte.getSolde());
        existing.setClientId(compte.getClientId());
        compteRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    // Supprimer un compte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!compteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        compteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
