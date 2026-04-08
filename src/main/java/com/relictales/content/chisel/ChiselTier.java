package com.relictales.content.chisel;

/**
 * Chisel tier enum.
 * Currently only one tier exists, designed for extensibility.
 */
public enum ChiselTier {
    STANDARD(120); // Copper tool tier, 120 durability

    public final int durability;

    ChiselTier(int durability) {
        this.durability = durability;
    }
}
