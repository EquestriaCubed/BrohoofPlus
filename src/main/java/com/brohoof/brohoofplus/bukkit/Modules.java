package com.brohoof.brohoofplus.bukkit;

import java.util.Locale;
import java.util.function.Function;

public enum Modules {
    // @formatter:off
    BLOCK_POTIONS_AND_ARROWS(BlockPotionsAndArrows::new),
    HEROCHAT_FANCYNAME(HerochatFancyname::new),
    CANCELLED_CHAT(CancelledChat::new),
    FIRST_JOINED(FirstJoined::new),
    GIFT_ITEM(GiftItem::new),
    ITEM_NOPE(ItemNope::new),
    //LIGHT(Light::new),
    LORE(Lore::new),
    MOUNT(Mount::new),
    RACE_MODE(RaceMode::new),
    FLIGHT(Flight::new),
    ENCHANT(Enchant::new),
    INFINITE_ITEM(InfiniteItem::new);
    // @formatter:on

    private final Function<BrohoofPlusPlugin, Module> constructor;

    Modules(final Function<BrohoofPlusPlugin, Module> constructor) {
        this.constructor = constructor;
    }

    public Function<BrohoofPlusPlugin, Module> getConstructor() {
        return constructor;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.US).replace("_", "");
    }
}
