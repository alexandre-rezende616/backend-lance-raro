package com.lanceraro.api.config;

import com.lanceraro.api.model.Relic;
import com.lanceraro.api.repository.RelicRepository;
import com.lanceraro.api.model.User;
import com.lanceraro.api.repository.UserRepository;
import com.lanceraro.api.model.TcgGroup;
import com.lanceraro.api.repository.TcgGroupRepository;
import com.lanceraro.api.model.Category;
import com.lanceraro.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RelicRepository relicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TcgGroupRepository tcgGroupRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Criando os usuários base se a taverna for nova
        if (userRepository.count() == 0) {
            User u1 = new User();
            u1.setEmail("usuario@gmail.com");
            u1.setPassword("123");
            u1.setNickname("Aventureiro Teste");
            u1.setRole("user");

            User u2 = new User();
            u2.setEmail("adm@gmail.com");
            u2.setPassword("123");
            u2.setNickname("Mestre da Taverna");
            u2.setRole("admin");

            userRepository.saveAll(Arrays.asList(u1, u2));
            System.out.println("Aventureiros fundadores chegaram à Taverna!");
        }

        // Semeando os Filtros de Taxonomia (Jogos e Categorias)
        if (tcgGroupRepository.count() == 0) {
            TcgGroup tcg1 = new TcgGroup(); tcg1.setName("Pokémon TCG");
            TcgGroup tcg2 = new TcgGroup(); tcg2.setName("Magic: The Gathering");
            tcgGroupRepository.saveAll(Arrays.asList(tcg1, tcg2));

            Category c1 = new Category(); c1.setName("Near Mint"); c1.setType("condition");
            Category c2 = new Category(); c2.setName("Português"); c2.setType("language");
            Category c3 = new Category(); c3.setName("Holográfica"); c3.setType("finish");
            Category c4 = new Category(); c4.setName("Ultra Rara"); c4.setType("rarity"); c4.setTcgId(1L); // Restrito a Pokémon
            Category c5 = new Category(); c5.setName("Mítica"); c5.setType("rarity"); c5.setTcgId(2L); // Restrito a MTG

            categoryRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
            System.out.println("Categorias e Grupos TCG adicionados à Taverna!");
        }

        if (relicRepository.count() == 0) {

            // Criando a primeira relíquia
            Relic r1 = new Relic();
            r1.setTitle("Charizard Base Set - Holográfica");
            r1.setPrice(1500.00);
            r1.setImageUrl("https://example.com/charizard.jpg");
            r1.setUserId(1L);
            r1.setTcgId(1L);
            r1.setConditionId(1L);
            r1.setLanguageId(2L);
            r1.setFinishId(3L);
            r1.setRarityId(4L);

            // Criando a segunda relíquia
            Relic r2 = new Relic();
            r2.setTitle("Black Lotus - Alpha");
            r2.setPrice(50000.00);
            r2.setImageUrl("https://example.com/lotus.jpg");
            r2.setUserId(2L);
            r2.setTcgId(2L);
            r2.setConditionId(1L);
            r2.setLanguageId(2L);
            r2.setRarityId(5L);

            // Salvando tudo no banco de dados
            relicRepository.saveAll(Arrays.asList(r1, r2));
            System.out.println("Taverna abastecida! Cartas de teste foram adicionadas ao banco de dados.");
        } else {
            System.out.println("O banco de dados já possui cartas, ignorando o abastecimento inicial.");
        }
    }
}
