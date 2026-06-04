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

    private void createCat(String name, String type, Long tcgId) {
        Category c = new Category();
        c.setName(name);
        c.setType(type);
        c.setTcgId(tcgId);
        categoryRepository.save(c);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User u1 = new User();
            u1.setEmail("usuario@gmail.com");
            u1.setPassword("123");
            u1.setNickname("Aventureiro Teste");
            u1.setRole("user");
            u1.setPhone("5511999999999"); // <--- O WhatsApp voltou!

            User u2 = new User();
            u2.setEmail("adm@gmail.com");
            u2.setPassword("123");
            u2.setNickname("Mestre da Taverna");
            u2.setRole("admin");
            u2.setPhone("5511988888888"); // <--- O WhatsApp voltou!

            userRepository.saveAll(Arrays.asList(u1, u2));
            System.out.println("Aventureiros fundadores chegaram à Taverna!");
        }

        if (tcgGroupRepository.count() == 0) {
            TcgGroup tcg1 = new TcgGroup(); tcg1.setName("Pokémon TCG");
            TcgGroup tcg2 = new TcgGroup(); tcg2.setName("Magic: The Gathering");
            TcgGroup tcg3 = new TcgGroup(); tcg3.setName("Yu-Gi-Oh! TCG");
            TcgGroup tcg4 = new TcgGroup(); tcg4.setName("One Piece Card Game");
            TcgGroup tcg5 = new TcgGroup(); tcg5.setName("Disney Lorcana");
            TcgGroup tcg6 = new TcgGroup(); tcg6.setName("Flesh and Blood");
            TcgGroup tcg7 = new TcgGroup(); tcg7.setName("Star Wars: Unlimited");

            tcgGroupRepository.saveAll(Arrays.asList(tcg1, tcg2, tcg3, tcg4, tcg5, tcg6, tcg7));

            createCat("Inglês", "language", null);
            createCat("Português", "language", null);
            createCat("Japonês", "language", null);

            createCat("Mint (M)", "condition", null);
            createCat("Near Mint (NM)", "condition", null);
            createCat("Slightly Played (SP/LP)", "condition", null);
            createCat("Moderately Played (MP)", "condition", null);
            createCat("Heavily Played (HP)", "condition", null);
            createCat("Damaged (DM)", "condition", null);

            Long idPkmn = tcg1.getId();
            createCat("Common (●)", "rarity", idPkmn); createCat("Uncommon (◆)", "rarity", idPkmn); createCat("Rare (★)", "rarity", idPkmn);
            createCat("Double Rare (★★)", "rarity", idPkmn); createCat("Ultra Rare", "rarity", idPkmn); createCat("Illustration Rare", "rarity", idPkmn);
            createCat("Special Illustration Rare", "rarity", idPkmn); createCat("Hyper Rare", "rarity", idPkmn); createCat("ACE SPEC", "rarity", idPkmn);
            createCat("Non-Foil", "finish", idPkmn); createCat("Holofoil", "finish", idPkmn); createCat("Reverse Holo", "finish", idPkmn); createCat("Texturizado (Embossing)", "finish", idPkmn);

            Long idMtg = tcg2.getId();
            createCat("Common", "rarity", idMtg); createCat("Uncommon", "rarity", idMtg); createCat("Rare", "rarity", idMtg); createCat("Mythic Rare", "rarity", idMtg);
            createCat("Normal", "finish", idMtg); createCat("Foil Tradicional", "finish", idMtg); createCat("Etched Foil", "finish", idMtg);
            createCat("Borderless", "finish", idMtg); createCat("Extended Art", "finish", idMtg); createCat("Showcase Frames", "finish", idMtg);

            Long idYgo = tcg3.getId();
            createCat("Common", "rarity", idYgo); createCat("Rare (Nome Prata)", "rarity", idYgo); createCat("Super Rare", "rarity", idYgo);
            createCat("Ultra Rare", "rarity", idYgo); createCat("Secret Rare", "rarity", idYgo); createCat("Ultimate Rare", "rarity", idYgo);
            createCat("Quarter Century", "rarity", idYgo); createCat("Starlight Rare", "rarity", idYgo);
            createCat("Padrão", "finish", idYgo);

            Long idOp = tcg4.getId();
            createCat("Common (C)", "rarity", idOp); createCat("Uncommon (UC)", "rarity", idOp); createCat("Rare (R)", "rarity", idOp);
            createCat("Leader (L)", "rarity", idOp); createCat("Super Rare (SR)", "rarity", idOp); createCat("Secret Rare (SEC)", "rarity", idOp);
            createCat("Normal", "finish", idOp); createCat("Parallel Art (Alt-Art)", "finish", idOp); createCat("Special Rare (SP)", "finish", idOp);
            createCat("Treasure Rare (TR)", "finish", idOp); createCat("Manga Rare", "finish", idOp);

            Long idLor = tcg5.getId();
            createCat("Common", "rarity", idLor); createCat("Uncommon", "rarity", idLor); createCat("Rare", "rarity", idLor); createCat("Super Rare", "rarity", idLor); createCat("Legendary", "rarity", idLor);
            createCat("Foil", "finish", idLor); createCat("Enchanted (Inkwash)", "finish", idLor); createCat("Epic", "finish", idLor); createCat("Iconic", "finish", idLor);

            Long idFab = tcg6.getId();
            createCat("Common", "rarity", idFab); createCat("Rare", "rarity", idFab); createCat("Majestic", "rarity", idFab); createCat("Legendary", "rarity", idFab); createCat("Fabled", "rarity", idFab); createCat("Marvel", "rarity", idFab);
            createCat("Rainbow Foil", "finish", idFab); createCat("Cold Foil", "finish", idFab);

            Long idSw = tcg7.getId();
            createCat("Leader/Base", "rarity", idSw); createCat("Common", "rarity", idSw); createCat("Uncommon", "rarity", idSw); createCat("Rare", "rarity", idSw); createCat("Legendary", "rarity", idSw); createCat("Special", "rarity", idSw);
            createCat("Foil", "finish", idSw); createCat("Hyperspace", "finish", idSw); createCat("Showcase", "finish", idSw);

            System.out.println("Catálogo Taxonômico Mestre inserido com SUCESSO!");
        }

        if (relicRepository.count() == 0) {
            Relic r1 = new Relic();
            r1.setTitle("Charizard Base Set - Holográfica");
            r1.setPrice(1500.00);
            r1.setImageUrl("https://example.com/charizard.jpg");
            r1.setUserId(1L);
            r1.setTcgId(1L);
            r1.setLanguageId(1L);
            r1.setConditionId(5L);
            r1.setLocation("São Paulo, SP");

            Relic r2 = new Relic();
            r2.setTitle("Black Lotus - Alpha");
            r2.setPrice(50000.00);
            r2.setImageUrl("https://example.com/lotus.jpg");
            r2.setUserId(2L);
            r2.setTcgId(2L);
            r2.setLanguageId(1L);
            r2.setConditionId(4L);
            r2.setLocation("Curitiba, PR");

            relicRepository.saveAll(Arrays.asList(r1, r2));
            System.out.println("Taverna abastecida! Cartas de teste foram adicionadas ao banco de dados e localizadas.");
        } else {
            System.out.println("O banco de dados já possui cartas, ignorando o abastecimento inicial.");
        }
    }
}
