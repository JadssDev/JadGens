package dev.jadss.jadgens.api;

public enum ShopEconomy {
    EXPERIENCE("exp"),
    ECONOMY("eco"),
    POINTS("pts");

    public final String alias;

    ShopEconomy(String alias) {
        this.alias = alias;
    }
}
