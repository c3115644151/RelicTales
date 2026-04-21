from PIL import Image

def generate_chisel():
    img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    pixels = img.load()

    # 颜色定义
    OUTLINE = (45, 30, 20, 255)    # 深褐色/轮廓
    WOOD_D = (90, 60, 40, 255)     # 深木色
    WOOD_L = (130, 95, 65, 255)    # 亮木色
    COPPER_D = (130, 70, 45, 255)  # 暗铜
    COPPER_M = (184, 115, 51, 255) # 基础铜
    COPPER_L = (230, 160, 110, 255)# 亮铜
    METAL_TIP = (240, 240, 240, 255)# 亮银色/磨损刃

    # 绘制对角线逻辑 (从左下 1,14 到 右上 14,1)

    # 1. 宽手柄 (1,14) 到 (6,9) 区域
    pixels[1, 14] = OUTLINE
    pixels[2, 14] = OUTLINE
    pixels[1, 13] = OUTLINE
    pixels[2, 13] = WOOD_D
    pixels[3, 13] = WOOD_D
    pixels[2, 12] = WOOD_D
    pixels[3, 12] = WOOD_L
    pixels[4, 12] = WOOD_L
    pixels[3, 11] = WOOD_L
    pixels[4, 11] = COPPER_D # 金属交界
    pixels[5, 11] = OUTLINE

    # 2. 这里的核心金属杆部 (6,10 到 10,5)
    for i in range(5, 11):
        pixels[i, 15-i] = COPPER_M       # 中轴
        pixels[i+1, 15-i] = COPPER_D     # 下阴影
        pixels[i, 15-i-1] = COPPER_L     # 上高光
        # 边缘轮廓
        if (i, 15-i+1) not in pixels: pixels[i, 15-i+1] = OUTLINE

    # 3. 增强的凿头 (11,4 到 14,1)
    pixels[11, 4] = COPPER_M
    pixels[12, 4] = COPPER_D
    pixels[11, 3] = COPPER_L
    pixels[12, 3] = COPPER_M
    pixels[13, 3] = COPPER_D
    pixels[12, 2] = COPPER_L
    pixels[13, 2] = METAL_TIP # 最亮的刃
    pixels[14, 2] = OUTLINE
    pixels[14, 1] = OUTLINE
    pixels[13, 1] = METAL_TIP
    pixels[12, 1] = OUTLINE

    # 整体轮廓加固 (补齐可能的空隙)
    for x in range(16):
        for y in range(16):
            if pixels[x, y] != (0, 0, 0, 0) and pixels[x, y] != OUTLINE:
                for dx, dy in [(-1,0), (1,0), (0,-1), (0,1)]:
                    if 0 <= x+dx < 16 and 0 <= y+dy < 16:
                        if pixels[x+dx, y+dy] == (0, 0, 0, 0):
                            pixels[x+dx, y+dy] = OUTLINE

    img.save("src/main/resources/assets/relictales/textures/item/copper_chisel.png")
    print("Texture REDESIGNED: copper_chisel.png")

if __name__ == "__main__":
    generate_chisel()
