package com.lanceraro.api.controller;

import com.lanceraro.api.model.Relic;
import com.lanceraro.api.repository.RelicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relics")
public class RelicController {

    @Autowired
    private RelicRepository relicRepository; // Trazemos nossa "varinha mágica" para cá

    @GetMapping
    public List<Relic> listarTodas() {
        return relicRepository.findAll(); // Busca tudo no banco e devolve pro celular!
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
}
