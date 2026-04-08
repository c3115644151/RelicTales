package com.relictales.content.brush;

/**
 * Brush tier enum with durability and efficiency values.
 * Higher tier = more durability and better efficiency.
 */
public enum BrushTier {
    COPPER(64, 1.0f),
    IRON(250, 1.5f),
    GOLD(32, 2.0f),
    DIAMOND(512, 2.5f),
    NETHERITE(2031, 3.0f);

    public final int durability;
    public final float efficiency;

    BrushTier(int durability, float efficiency) {
        this.durability = durability;
        this.efficiency = efficiency;
    }
}
