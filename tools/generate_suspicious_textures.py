"""
Crack overlay texture generator for RelicTales suspicious blocks.

Extracts crack pixel positions from vanilla suspicious_sand/gravel textures
by comparing pixel-by-pixel against their base textures (sand/gravel).
Then applies the extracted crack mask to custom block textures, darkening
crack pixels by 20 RGB per dusted level.

Generates 20 PNG textures:
  - 3 cube_all blocks × 4 levels = 12 textures
  - 1 cube_column block × 2 textures (side, end) × 4 levels = 8 textures
"""

import zipfile
from pathlib import Path
from PIL import Image

PATCHED_JAR = Path("build/moddev/artifacts/minecraft-patched-26.1.1.14-beta-merged.jar")
OUTPUT_DIR = Path("src/main/resources/assets/relictales/textures/block")

VANILLA_BASES = {
    "sand": {
        "base": "assets/minecraft/textures/block/sand.png",
        "suspicious": [f"assets/minecraft/textures/block/suspicious_sand_{i}.png" for i in range(4)],
    },
    "gravel": {
        "base": "assets/minecraft/textures/block/gravel.png",
        "suspicious": [f"assets/minecraft/textures/block/suspicious_gravel_{i}.png" for i in range(4)],
    },
}

# (block_name, texture_suffix, base_texture_in_jar)
# texture_suffix: "all" for cube_all, "side"/"end" for cube_column
CUSTOM_BLOCKS = [
    ("mossy_cobblestone",   "all",  "assets/minecraft/textures/block/mossy_cobblestone.png"),
    ("cracked_stone_bricks", "all", "assets/minecraft/textures/block/cracked_stone_bricks.png"),
    ("mossy_stone_bricks",  "all",  "assets/minecraft/textures/block/mossy_stone_bricks.png"),
    ("chiseled_sandstone",  "side", "assets/minecraft/textures/block/chiseled_sandstone.png"),
    ("chiseled_sandstone",  "end",  "assets/minecraft/textures/block/sandstone_top.png"),
    ("nether_bricks",       "all",  "assets/minecraft/textures/block/nether_bricks.png"),
    ("purpur_block",        "all",  "assets/minecraft/textures/block/purpur_block.png"),
]

DARKEN_AMOUNT = 20


def extract_crack_mask(zf):
    """
    Extract crack pixel positions from vanilla suspicious textures.
    Returns dict: (x, y) -> first_level (0-3) where crack appears.
    """
    crack_first_level = {}

    for base_name, info in VANILLA_BASES.items():
        base_img = Image.open(zf.open(info["base"])).convert("RGBA")
        base_px = base_img.load()
        w, h = base_img.size

        for level in range(4):
            susp_img = Image.open(zf.open(info["suspicious"][level])).convert("RGBA")
            susp_px = susp_img.load()

            for y in range(h):
                for x in range(w):
                    if base_px[x, y][3] > 0 and base_px[x, y] != susp_px[x, y]:
                        if (x, y) not in crack_first_level:
                            crack_first_level[(x, y)] = level

    return crack_first_level


def darken(r, g, b, a, amount):
    return (max(0, r - amount), max(0, g - amount), max(0, b - amount), a)


def generate_all(zf, crack_info):
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

    # Group by block_name to handle multi-texture blocks (chiseled_sandstone)
    blocks = {}
    for block_name, tex_suffix, base_path in CUSTOM_BLOCKS:
        blocks.setdefault(block_name, []).append((tex_suffix, base_path))

    total = 0
    for block_name, textures in blocks.items():
        for level in range(4):
            for tex_suffix, base_path in textures:
                base_img = Image.open(zf.open(base_path)).convert("RGBA")
                img = base_img.copy()
                px = img.load()
                w, h = img.size

                for (cx, cy), first_level in crack_info.items():
                    if first_level <= level and cx < w and cy < h:
                        r, g, b, a = px[cx, cy]
                        px[cx, cy] = darken(r, g, b, a, DARKEN_AMOUNT)

                # Filename: suspicious_<block>[_suffix]_<level>.png
                # Suffix only included for non-"all" textures (chiseled_sandstone side/end)
                if tex_suffix == "all":
                    filename = f"suspicious_{block_name}_{level}.png"
                else:
                    filename = f"suspicious_{block_name}_{tex_suffix}_{level}.png"

                out_path = OUTPUT_DIR / filename
                img.save(out_path)
                print(f"  Wrote {filename}")
                total += 1

    print(f"\nTotal: {total} textures generated in {OUTPUT_DIR}")


def main():
    if not PATCHED_JAR.exists():
        print(f"Error: Patched jar not found at {PATCHED_JAR}")
        print("Run './gradlew build' or './gradlew decompile' first.")
        return 1

    print(f"Extracting crack mask from {PATCHED_JAR}...")
    with zipfile.ZipFile(PATCHED_JAR) as zf:
        crack_info = extract_crack_mask(zf)
        print(f"Found {len(crack_info)} unique crack positions across 4 dusted levels.")
        print("Level distribution:")
        for level in range(4):
            count = sum(1 for lvl in crack_info.values() if lvl == level)
            print(f"  Level {level}: {count} positions")

        print("\nGenerating custom suspicious textures...")
        generate_all(zf, crack_info)

    return 0


if __name__ == "__main__":
    exit(main())
