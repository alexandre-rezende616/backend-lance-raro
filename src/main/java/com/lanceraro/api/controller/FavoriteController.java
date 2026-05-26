package com.lanceraro.api.controller;

import com.lanceraro.api.model.Favorite;
import com.lanceraro.api.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @GetMapping("/check")
    public boolean checkIsFavorite(@RequestParam Long userId, @RequestParam Long relicId) {
        Favorite fav = favoriteRepository.findByUserIdAndRelicId(userId, relicId);
        return fav != null;
    }

    @GetMapping("/user/{userId}")
    public List<Favorite> getUserFavorites(@PathVariable Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @PostMapping("/toggle")
    public boolean toggleFavorite(@RequestBody Favorite favorite) {
        Favorite existingFav = favoriteRepository.findByUserIdAndRelicId(favorite.getUserId(), favorite.getRelicId());
        if (existingFav != null) {
            favoriteRepository.delete(existingFav);
            return false;
        } else {
            favoriteRepository.save(favorite);
            return true;
        }
    }
}
