package fr.fms.banque_mongo.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

// Cette classe force Spring à utiliser la bonne base MongoDB
@Configuration
public class MongoConfig {

    // Récupère l'URL MongoDB depuis application.properties
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    // Récupère le nom de la base depuis application.properties
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    // Crée la connexion vers MongoDB Atlas
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    // Force MongoTemplate à travailler sur la base banque_tp
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, databaseName);
    }
}