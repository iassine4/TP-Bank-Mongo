package fr.fms.banque_mongo.console;

import fr.fms.banque_mongo.model.Client;
import fr.fms.banque_mongo.model.Compte;
import fr.fms.banque_mongo.model.Operation;
import fr.fms.banque_mongo.repository.ClientRepository;
import fr.fms.banque_mongo.repository.CompteRepository;
import fr.fms.banque_mongo.repository.OperationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

@Component
public class ConsoleMenuRunner implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;
    private final OperationRepository operationRepository;

    public ConsoleMenuRunner(ClientRepository clientRepository,
            CompteRepository compteRepository,
            OperationRepository operationRepository) {
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean quit = false;
            while (!quit) {
                showMainMenu();
                int choix = readInt(scanner, "Choisissez une option : ");
                switch (choix) {
                    case 1:
                        handleClientMenu(scanner);
                        break;
                    case 2:
                        handleCompteMenu(scanner);
                        break;
                    case 3:
                        handleOperationMenu(scanner);
                        break;
                    case 4:
                    case 0:
                        System.out.println("Fin de l'application.");
                        quit = true;
                        break;
                    default:
                        System.out.println("Option invalide, veuillez réessayer.");
                }
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n===== MENU BANQUE =====");
        System.out.println("1. Gestion des clients");
        System.out.println("2. Gestion des comptes");
        System.out.println("3. Gestion des opérations");
        System.out.println("4. Quitter");
    }

    private void handleClientMenu(Scanner scanner) {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des clients ---");
            System.out.println("1. Créer un client");
            System.out.println("2. Rechercher un client par id");
            System.out.println("3. Modifier un client");
            System.out.println("4. Supprimer un client");
            System.out.println("5. Lister les clients");
            System.out.println("6. Retour");
            int choix = readInt(scanner, "Choisissez une option : ");
            switch (choix) {
                case 1:
                    createClient(scanner);
                    break;
                case 2:
                    searchClient(scanner);
                    break;
                case 3:
                    updateClient(scanner);
                    break;
                case 4:
                    deleteClient(scanner);
                    break;
                case 5:
                    listClients();
                    break;
                case 6:
                    retour = true;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private void handleCompteMenu(Scanner scanner) {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des comptes ---");
            System.out.println("1. Créer un compte");
            System.out.println("2. Consulter un compte par id");
            System.out.println("3. Modifier un compte");
            System.out.println("4. Supprimer un compte");
            System.out.println("5. Lister les comptes");
            System.out.println("6. Lister les comptes d'un client");
            System.out.println("7. Retour");
            int choix = readInt(scanner, "Choisissez une option : ");
            switch (choix) {
                case 1:
                    createCompte(scanner);
                    break;
                case 2:
                    searchCompte(scanner);
                    break;
                case 3:
                    updateCompte(scanner);
                    break;
                case 4:
                    deleteCompte(scanner);
                    break;
                case 5:
                    listComptes();
                    break;
                case 6:
                    listComptesByClient(scanner);
                    break;
                case 7:
                    retour = true;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private void handleOperationMenu(Scanner scanner) {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- Gestion des opérations ---");
            System.out.println("1. Effectuer un dépôt");
            System.out.println("2. Effectuer un retrait");
            System.out.println("3. Effectuer un virement");
            System.out.println("4. Consulter l'historique des opérations d'un compte");
            System.out.println("5. Lister toutes les opérations");
            System.out.println("6. Retour");
            int choix = readInt(scanner, "Choisissez une option : ");
            switch (choix) {
                case 1:
                    effectuerDepot(scanner);
                    break;
                case 2:
                    effectuerRetrait(scanner);
                    break;
                case 3:
                    effectuerVirement(scanner);
                    break;
                case 4:
                    historiqueCompte(scanner);
                    break;
                case 5:
                    listOperations();
                    break;
                case 6:
                    retour = true;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private void createClient(Scanner scanner) {
        System.out.println("\n--- Créer un client ---");
        String id = readLine(scanner, "Id du client : ");
        if (clientRepository.existsById(id)) {
            System.out.println("Un client avec cet id existe déjà.");
            return;
        }
        String nom = readLine(scanner, "Nom : ");
        String email = readLine(scanner, "Email : ");
        String telephone = readLine(scanner, "Téléphone : ");
        Client client = new Client(id, nom, email, telephone);
        clientRepository.save(client);
        System.out.println("Client créé avec succès.");
    }

    private void searchClient(Scanner scanner) {
        System.out.println("\n--- Rechercher un client ---");
        String id = readLine(scanner, "Id du client : ");
        Optional<Client> optional = clientRepository.findById(id);
        if (optional.isPresent()) {
            printClient(optional.get());
        } else {
            System.out.println("Client non trouvé.");
        }
    }

    private void updateClient(Scanner scanner) {
        System.out.println("\n--- Modifier un client ---");
        String id = readLine(scanner, "Id du client : ");
        Optional<Client> optional = clientRepository.findById(id);
        if (optional.isEmpty()) {
            System.out.println("Client non trouvé.");
            return;
        }
        Client client = optional.get();
        client.setNom(readLine(scanner, "Nouveau nom [" + client.getNom() + "] : "));
        client.setEmail(readLine(scanner, "Nouvel email [" + client.getEmail() + "] : "));
        client.setTelephone(readLine(scanner, "Nouveau téléphone [" + client.getTelephone() + "] : "));
        clientRepository.save(client);
        System.out.println("Client mis à jour.");
    }

    private void deleteClient(Scanner scanner) {
        System.out.println("\n--- Supprimer un client ---");
        String id = readLine(scanner, "Id du client : ");
        if (!clientRepository.existsById(id)) {
            System.out.println("Client non trouvé.");
            return;
        }
        clientRepository.deleteById(id);
        System.out.println("Client supprimé.");
    }

    private void listClients() {
        System.out.println("\n--- Liste des clients ---");
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouvé.");
            return;
        }
        for (Client client : clients) {
            printClient(client);
        }
    }

    private void createCompte(Scanner scanner) {
        System.out.println("\n--- Créer un compte ---");
        String id = readLine(scanner, "Id du compte : ");
        if (compteRepository.existsById(id)) {
            System.out.println("Un compte avec cet id existe déjà.");
            return;
        }
        String numeroCompte = readLine(scanner, "Numéro de compte : ");
        String typeCompte = readLine(scanner, "Type de compte : ");
        double solde = readDouble(scanner, "Solde initial : ");
        String clientId = readLine(scanner, "Id du client : ");
        if (!clientRepository.existsById(clientId)) {
            System.out.println("Client inconnu, création impossible.");
            return;
        }
        Compte compte = new Compte(id, numeroCompte, typeCompte, solde, clientId);
        compteRepository.save(compte);
        System.out.println("Compte créé avec succès.");
    }

    private void searchCompte(Scanner scanner) {
        System.out.println("\n--- Consulter un compte ---");
        String id = readLine(scanner, "Id du compte : ");
        Optional<Compte> optional = compteRepository.findById(id);
        if (optional.isPresent()) {
            printCompte(optional.get());
        } else {
            System.out.println("Compte non trouvé.");
        }
    }

    private void updateCompte(Scanner scanner) {
        System.out.println("\n--- Modifier un compte ---");
        String id = readLine(scanner, "Id du compte : ");
        Optional<Compte> optional = compteRepository.findById(id);
        if (optional.isEmpty()) {
            System.out.println("Compte non trouvé.");
            return;
        }
        Compte compte = optional.get();
        compte.setNumeroCompte(readLine(scanner, "Nouveau numéro de compte [" + compte.getNumeroCompte() + "] : "));
        compte.setTypeCompte(readLine(scanner, "Nouveau type de compte [" + compte.getTypeCompte() + "] : "));
        compte.setSolde(readDouble(scanner, "Nouveau solde [" + compte.getSolde() + "] : "));
        String clientId = readLine(scanner, "Nouveau clientId [" + compte.getClientId() + "] : ");
        if (!clientRepository.existsById(clientId)) {
            System.out.println("Client inconnu, mise à jour interrompue.");
            return;
        }
        compte.setClientId(clientId);
        compteRepository.save(compte);
        System.out.println("Compte mis à jour.");
    }

    private void deleteCompte(Scanner scanner) {
        System.out.println("\n--- Supprimer un compte ---");
        String id = readLine(scanner, "Id du compte : ");
        if (!compteRepository.existsById(id)) {
            System.out.println("Compte non trouvé.");
            return;
        }
        compteRepository.deleteById(id);
        System.out.println("Compte supprimé.");
    }

    private void listComptes() {
        System.out.println("\n--- Liste des comptes ---");
        List<Compte> comptes = compteRepository.findAll();
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte trouvé.");
            return;
        }
        for (Compte compte : comptes) {
            printCompte(compte);
        }
    }

    private void listComptesByClient(Scanner scanner) {
        System.out.println("\n--- Comptes d'un client ---");
        String clientId = readLine(scanner, "Id du client : ");
        if (!clientRepository.existsById(clientId)) {
            System.out.println("Client non trouvé.");
            return;
        }
        List<Compte> comptes = compteRepository.findByClientId(clientId);
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte trouvé pour ce client.");
            return;
        }
        for (Compte compte : comptes) {
            printCompte(compte);
        }
    }

    private void effectuerDepot(Scanner scanner) {
        System.out.println("\n--- Dépôt ---");
        String compteId = readLine(scanner, "Id du compte : ");
        Optional<Compte> optional = compteRepository.findById(compteId);
        if (optional.isEmpty()) {
            System.out.println("Compte non trouvé.");
            return;
        }
        double montant = readDouble(scanner, "Montant à déposer : ");
        if (montant <= 0) {
            System.out.println("Le montant doit être strictement positif.");
            return;
        }
        String description = readLine(scanner, "Description : ");
        Compte compte = optional.get();
        compte.setSolde(compte.getSolde() + montant);
        compteRepository.save(compte);
        Operation operation = new Operation();
        operation.setType("DEPOT");
        operation.setMontant(montant);
        operation.setDateOperation(LocalDateTime.now());
        operation.setCompteId(compteId);
        operation.setCompteSourceId(null);
        operation.setCompteDestinataireId(null);
        operation.setDescription(description);
        operationRepository.save(operation);
        System.out.println("Dépôt effectué.");
    }

    private void effectuerRetrait(Scanner scanner) {
        System.out.println("\n--- Retrait ---");
        String compteId = readLine(scanner, "Id du compte : ");
        Optional<Compte> optional = compteRepository.findById(compteId);
        if (optional.isEmpty()) {
            System.out.println("Compte non trouvé.");
            return;
        }
        double montant = readDouble(scanner, "Montant à retirer : ");
        if (montant <= 0) {
            System.out.println("Le montant doit être strictement positif.");
            return;
        }
        Compte compte = optional.get();
        if (compte.getSolde() < montant) {
            System.out.println("Solde insuffisant.");
            return;
        }
        String description = readLine(scanner, "Description : ");
        compte.setSolde(compte.getSolde() - montant);
        compteRepository.save(compte);
        Operation operation = new Operation();
        operation.setType("RETRAIT");
        operation.setMontant(montant);
        operation.setDateOperation(LocalDateTime.now());
        operation.setCompteId(compteId);
        operation.setCompteSourceId(null);
        operation.setCompteDestinataireId(null);
        operation.setDescription(description);
        operationRepository.save(operation);
        System.out.println("Retrait effectué.");
    }

    private void effectuerVirement(Scanner scanner) {
        System.out.println("\n--- Virement ---");
        String sourceId = readLine(scanner, "Id du compte source : ");
        String destId = readLine(scanner, "Id du compte destinataire : ");
        if (sourceId.equals(destId)) {
            System.out.println("Les comptes source et destinataire doivent être différents.");
            return;
        }
        Optional<Compte> optionalSource = compteRepository.findById(sourceId);
        Optional<Compte> optionalDest = compteRepository.findById(destId);
        if (optionalSource.isEmpty() || optionalDest.isEmpty()) {
            System.out.println("Un des comptes est introuvable.");
            return;
        }
        double montant = readDouble(scanner, "Montant du virement : ");
        if (montant <= 0) {
            System.out.println("Le montant doit être strictement positif.");
            return;
        }
        Compte source = optionalSource.get();
        Compte dest = optionalDest.get();
        if (source.getSolde() < montant) {
            System.out.println("Solde insuffisant sur le compte source.");
            return;
        }
        String description = readLine(scanner, "Description : ");
        source.setSolde(source.getSolde() - montant);
        dest.setSolde(dest.getSolde() + montant);
        compteRepository.save(source);
        compteRepository.save(dest);
        Operation operation = new Operation();
        operation.setType("VIREMENT");
        operation.setMontant(montant);
        operation.setDateOperation(LocalDateTime.now());
        operation.setCompteId(null);
        operation.setCompteSourceId(sourceId);
        operation.setCompteDestinataireId(destId);
        operation.setDescription(description);
        operationRepository.save(operation);
        System.out.println("Virement effectué.");
    }

    private void historiqueCompte(Scanner scanner) {
        System.out.println("\n--- Historique des opérations ---");
        String compteId = readLine(scanner, "Id du compte : ");
        if (!compteRepository.existsById(compteId)) {
            System.out.println("Compte non trouvé.");
            return;
        }
        List<Operation> operations = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        ajouterOperations(operations, seen, operationRepository.findByCompteId(compteId));
        ajouterOperations(operations, seen, operationRepository.findByCompteSourceId(compteId));
        ajouterOperations(operations, seen, operationRepository.findByCompteDestinataireId(compteId));
        if (operations.isEmpty()) {
            System.out.println("Aucune opération trouvée pour ce compte.");
            return;
        }
        operations.forEach(this::printOperation);
    }

    private void listOperations() {
        System.out.println("\n--- Liste des opérations ---");
        List<Operation> operations = operationRepository.findAll();
        if (operations.isEmpty()) {
            System.out.println("Aucune opération trouvée.");
            return;
        }
        for (Operation operation : operations) {
            printOperation(operation);
        }
    }

    private void ajouterOperations(List<Operation> operations, Set<String> seen, List<Operation> nouvelles) {
        for (Operation operation : nouvelles) {
            if (operation.getId() != null && !seen.contains(operation.getId())) {
                operations.add(operation);
                seen.add(operation.getId());
            }
        }
    }

    private int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Entrée non valide. Veuillez saisir un nombre entier.");
            }
        }
    }

    private double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Entrée non valide. Veuillez saisir un nombre.");
            }
        }
    }

    private String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void printClient(Client client) {
        System.out.println("Id: " + client.getId() + ", Nom: " + client.getNom()
                + ", Email: " + client.getEmail() + ", Téléphone: " + client.getTelephone());
    }

    private void printCompte(Compte compte) {
        System.out.println("Id: " + compte.getId() + ", Numéro: " + compte.getNumeroCompte()
                + ", Type: " + compte.getTypeCompte() + ", Solde: " + compte.getSolde()
                + ", ClientId: " + compte.getClientId());
    }

    private void printOperation(Operation operation) {
        System.out.println("Id: " + operation.getId()
                + ", Type: " + operation.getType()
                + ", Montant: " + operation.getMontant()
                + ", Date: " + operation.getDateOperation()
                + ", CompteId: " + operation.getCompteId()
                + ", Source: " + operation.getCompteSourceId()
                + ", Destinataire: " + operation.getCompteDestinataireId()
                + ", Description: " + operation.getDescription());
    }
}
