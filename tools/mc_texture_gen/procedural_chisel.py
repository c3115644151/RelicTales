from PIL import Image

def build_chisel(color_head, color_stick, color_outline):
    # Create an empty 16x16 image with transparent background
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    pixels = img.load()

    # Minecraft Tool Matrix (0=Transparent, 1=Outline, 2=Stick, 3=StickHighlight, 4=HeadOutline, 5=Head, 6=HeadHighlight)
    matrix = [
        [0,0,0,0,0,0,0,0,0,0,0,4,4,4,4,0],
        [0,0,0,0,0,0,0,0,0,0,4,6,6,5,4,4],
        [0,0,0,0,0,0,0,0,0,4,6,5,5,5,5,4],
        [0,0,0,0,0,0,0,0,4,6,5,5,5,5,4,0],
        [0,0,0,0,0,0,0,4,6,5,5,5,5,4,0,0],
        [0,0,0,0,0,0,4,6,5,5,5,4,4,0,0,0],
        [0,0,0,0,0,4,6,5,5,4,4,0,0,0,0,0],
        [0,0,0,0,4,6,5,4,4,0,0,0,0,0,0,0],
        [0,0,0,1,1,4,4,1,0,0,0,0,0,0,0,0],
        [0,0,1,3,2,1,1,0,0,0,0,0,0,0,0,0],
        [0,1,3,2,1,0,0,0,0,0,0,0,0,0,0,0],
        [1,3,2,1,0,0,0,0,0,0,0,0,0,0,0,0],
        [1,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0],
        [1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
        [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    ]

    palette = {
        0: (0, 0, 0, 0),         # Transparent
        1: (30, 20, 20, 255),    # Stick Outline
        2: (86, 52, 30, 255),    # Wood Dark
        3: (136, 85, 45, 255),   # Wood Light
        4: (34, 48, 40, 255),    # Copper Outline (Dark Patina)
        5: (76, 150, 107, 255),  # Copper Base (Green Patina)
        6: (116, 204, 153, 255), # Copper Highlight
    }

    for y in range(16):
        for x in range(16):
            if matrix[y][x] != 0:
                pixels[x, y] = palette[matrix[y][x]]

    return img

img = build_chisel(None, None, None)
img.save("src/main/resources/assets/relictales/textures/item/copper_chisel.png")
print("Procedural Pixel Array Generated.")
