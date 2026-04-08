package com.relictales.content.rock;

/**
 * Rock type enum defining archaeological rock layers.
 * Each type has different Y-level ranges and difficulty.
 */
public enum RockType {
    LIMESTONE("limestone", 5, 30, 2, 2),      // Shallow: 2 hits + 2 brush
    SHALE("shale", -10, 20, 3, 3),            // Mid-depth: 3 hits + 3 brush (future)
    SLATE("slate", -30, 0, 4, 3),             // Mid-deep: 4 hits + 3 brush (future)
    DARKGLIMMER("darkglimmer", -60, -40, 4, 3), // Deep: 4 hits + 3 brush
    PHANTOMITE("phantomite", -40, -25, 5, 4);  // Deepest: 5 hits + 4 brush (future)

    public final String name;
    public final int minY;
    public final int maxY;
    public final int chiselHits;
    public final int brushSwings;

    RockType(String name, int minY, int maxY, int chiselHits, int brushSwings) {
        this.name = name;
        this.minY = minY;
        this.maxY = maxY;
        this.chiselHits = chiselHits;
        this.brushSwings = brushSwings;
    }
}
