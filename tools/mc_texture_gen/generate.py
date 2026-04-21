import os
import sys
import argparse
import requests
from io import BytesIO
from PIL import Image, ImageEnhance, ImageFilter, ImageOps
import rembg
import urllib.parse

def fetch_image(prompt: str) -> Image.Image:
    print(f"[*] Fetching base concept for: '{prompt}'...")
    encoded_prompt = urllib.parse.quote(prompt)
    url = f"https://image.pollinations.ai/prompt/{encoded_prompt}?nologo=true&enhance=false&width=1024&height=1024"
    response = requests.get(url)
    if response.status_code != 200:
        raise Exception("Fetch failed")
    return Image.open(BytesIO(response.content)).convert("RGBA")

def remove_background(img: Image.Image) -> Image.Image:
    img_byte_arr = BytesIO()
    img.save(img_byte_arr, format='PNG')
    output_bytes = rembg.remove(img_byte_arr.getvalue())
    return Image.open(BytesIO(output_bytes)).convert("RGBA")

def smart_crop(img: Image.Image) -> Image.Image:
    bbox = img.getbbox()
    if not bbox: return img
    
    # We want a tight crop for tools to maximize the 16x16 space
    # but tools are meant to be diagonal. 
    cropped = img.crop(bbox)
    width, height = cropped.size
    size = max(width, height)
    
    # To look like a Minecraft tool, it should span corner to corner.
    # We pad it to a square so resizing maintains aspect ratio.
    square_img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    offset = ((size - width) // 2, (size - height) // 2)
    square_img.paste(cropped, offset)
    return square_img

def convert_to_true_pixel_art(img: Image.Image, size: int = 16) -> Image.Image:
    # 1. Downscale smoothly to preserve the overall silhouette mass
    # Using Lanczos gives a smooth representation of the object's shape in 16x16
    small = img.resize((size, size), resample=Image.Resampling.LANCZOS)
    
    # 2. Hard thresholding the alpha to create the "jaggies" (pixels)
    final_img = Image.new("RGBA", (size, size))
    pixels_in = small.load()
    pixels_out = final_img.load()
    
    # We will build a restricted palette. Copper tools: Dark brown stick, Green/Orange head
    palette = [
        (56, 33, 21),   # Dark wood
        (102, 63, 36),  # Medium wood
        (161, 102, 63), # Light wood
        (43, 61, 51),   # Dark patina outline
        (76, 150, 107), # Mid green patina
        (116, 204, 153),# Bright green patina
        (204, 122, 61), # Copper highlight
        (25, 25, 25)    # Blackish outline
    ]

    def closest_color(r, g, b):
        min_dist = 999999
        best_col = palette[0]
        for c in palette:
            dist = (r-c[0])**2 + (g-c[1])**2 + (b-c[2])**2
            if dist < min_dist:
                min_dist = dist
                best_col = c
        return best_col

    # First pass: establish the shape and map closest colors to the body
    for y in range(size):
        for x in range(size):
            r, g, b, a = pixels_in[x, y]
            if a > 80: # Aggressive threshold for crisp shape
                # Boost saturation slightly before mapping
                cr, cg, cb = closest_color(r, g, b)
                pixels_out[x, y] = (cr, cg, cb, 255)
            else:
                pixels_out[x, y] = (0, 0, 0, 0)

    # Second pass: Classic Minecraft outline
    # In MC, the outline is drawn on the transparent pixels *touching* the opaque ones
    final_outlined = final_img.copy()
    outdata = final_outlined.load()
    
    for y in range(size):
        for x in range(size):
            if pixels_out[x, y][3] == 0: # If transparent
                # Check if it touches an opaque pixel
                touches = False
                for dx, dy in [(-1,0), (1,0), (0,-1), (0,1)]:
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < size and 0 <= ny < size:
                        if pixels_out[nx, ny][3] == 255:
                            touches = True
                            break
                if touches:
                    outdata[x, y] = (30, 20, 20, 255) # Dark border color

    # Final cleanup: Remove corner pixels of the outline to make it less boxy
    # This is a classic pixel art technique (selout)
    for y in range(size):
        for x in range(size):
            if outdata[x, y] == (30, 20, 20, 255):
                # Count opaque non-outline neighbors
                neighbors = 0
                for dx, dy in [(-1,0), (1,0), (0,-1), (0,1)]:
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < size and 0 <= ny < size:
                        if pixels_out[nx, ny][3] == 255:
                            neighbors += 1
                if neighbors == 0:
                    # It's an orphan or edge case
                    pass

    return final_outlined

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("prompt")
    parser.add_argument("output")
    args = parser.parse_args()

    # Prompt engineered for shape, not detail
    full_prompt = f"minimalist simple 2d flat vector graphic logo of a {args.prompt}, distinct simple shape, white background. Tilted 45 degrees from bottom left to top right."

    try:
        img = fetch_image(full_prompt)
        img = remove_background(img)
        img = smart_crop(img)
        final_img = convert_to_true_pixel_art(img, size=16)
        
        os.makedirs(os.path.dirname(os.path.abspath(args.output)), exist_ok=True)
        final_img.save(args.output)
        print("Success")
    except Exception as e:
        print("Failed:", str(e))
        sys.exit(1)

if __name__ == "__main__":
    main()
