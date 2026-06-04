package com.lanceraro.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "relics")
public class Relic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double price;

    private String imageUrl;

    // Relacionamentos simples por enquanto
    private Long userId;
    private Long tcgId;

    private Long conditionId;
    private Long rarityId;
    private Long finishId;
    private Long languageId;

    private Boolean inStock = true;

    // NOVO CAMPO: Localização
    private String location;

    // NOVO CAMPO: Nível de Impulsionamento VIP (0 = Normal, 1 = Bronze, 2 = Prata, 3 = Ouro)
    private Integer boostLevel = 0;

    // NOVO CAMPO: Validade do Impulsionamento
    private java.time.LocalDateTime boostExpiresAt;

    public Relic() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTcgId() { return tcgId; }
    public void setTcgId(Long tcgId) { this.tcgId = tcgId; }

    public Long getConditionId() { return conditionId; }
    public void setConditionId(Long conditionId) { this.conditionId = conditionId; }

    public Long getRarityId() { return rarityId; }
    public void setRarityId(Long rarityId) { this.rarityId = rarityId; }

    public Long getFinishId() { return finishId; }
    public void setFinishId(Long finishId) { this.finishId = finishId; }

    public Long getLanguageId() { return languageId; }
    public void setLanguageId(Long languageId) { this.languageId = languageId; }

    public Boolean getInStock() { return inStock; }
    public void setInStock(Boolean inStock) { this.inStock = inStock; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getBoostLevel() { return boostLevel; }
    public void setBoostLevel(Integer boostLevel) { this.boostLevel = boostLevel; }

    public java.time.LocalDateTime getBoostExpiresAt() { return boostExpiresAt; }
    public void setBoostExpiresAt(java.time.LocalDateTime boostExpiresAt) { this.boostExpiresAt = boostExpiresAt; }
}