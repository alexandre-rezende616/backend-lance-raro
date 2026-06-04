package com.lanceraro.api.controller;

import com.lanceraro.api.model.Relic;
import com.lanceraro.api.model.Notification;
import com.lanceraro.api.repository.RelicRepository;
import com.lanceraro.api.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relics")
public class RelicController {

    @Autowired
    private RelicRepository relicRepository; // Trazemos nossa "varinha mágica" para cá

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping
    public List<Relic> listarTodas() {
        List<Relic> relics = relicRepository.findAll();
        java.time.LocalDateTime agora = java.time.LocalDateTime.now();

        // Robô limpador: Se a validade expirou, a carta volta ao normal!
        for (Relic r : relics) {
            if (r.getBoostLevel() != null && r.getBoostLevel() > 0 && r.getBoostExpiresAt() != null && r.getBoostExpiresAt().isBefore(agora)) {
                r.setBoostLevel(0);
                r.setBoostExpiresAt(null);
                relicRepository.save(r);
            }
        }
        return relicRepository.findAll();
    }

    @GetMapping("/{id}")
    public Relic buscarPorId(@PathVariable Long id) {
        return relicRepository.findById(id).orElse(null); // Busca uma carta específica
    }

    @GetMapping("/user/{userId}")
    public List<Relic> buscarPorUsuario(@PathVariable Long userId) {
        return relicRepository.findByUserId(userId); // Busca cartas de um usuário
    }

    @PostMapping
    public Relic anunciarCarta(@RequestBody Relic novaReliquia) {
        return relicRepository.save(novaReliquia); // Salva uma carta nova no banco!
    }

    @DeleteMapping("/{id}")
    public void deletarCarta(@PathVariable Long id) {
        relicRepository.deleteById(id); // Deleta a carta no banco!
    }

    @PutMapping("/{id}/buy")
    public Relic comprarCarta(@PathVariable Long id) {
        Relic relic = relicRepository.findById(id).orElse(null);
        if (relic != null) {
            relic.setInStock(false); // Marca como fora de estoque (vendida)
            return relicRepository.save(relic);
        }
        return null;
    }

    @PutMapping("/{id}/boost")
    public Relic impulsionarCarta(@PathVariable Long id, @RequestParam Integer level) {
        Relic relic = relicRepository.findById(id).orElse(null);
        if (relic != null) {
            relic.setBoostLevel(level); // Evolui a carta para Bronze, Prata ou Ouro!
            // Bronze = 3 dias | Prata = 7 dias | Ouro = 15 dias
            int dias = (level == 3) ? 15 : ((level == 2) ? 7 : 3);
            relic.setBoostExpiresAt(java.time.LocalDateTime.now().plusDays(dias));

            // Envia um Corvo confirmando a compra!
            Notification notif = new Notification();
            notif.setUserId(relic.getUserId());
            notif.setTitle("Destaque VIP Ativado! ✨");
            notif.setMessage("A relíquia '" + relic.getTitle() + "' brilhará no topo do mural por " + dias + " dias.");
            notificationRepository.save(notif);

            return relicRepository.save(relic);
        }
        return null;
    }
}