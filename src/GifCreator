import os
from PIL import Image

def bmp_to_gif(folder_path, output_path, duration=50):
    bmp_files = sorted([f for f in os.listdir(folder_path) if f.lower().endswith('.bmp')])
    image_paths = [os.path.join(folder_path, f) for f in bmp_files]
    images = [Image.open(img_path) for img_path in image_paths]
    
    if images:
        images[0].save(
            output_path,
            save_all = True,
            append_images = images[1:],
            format = "GIF",
            duration = duration,
            loop = 0  # 0 means infinite loop
        )
        print(f"GIF saved to {output_path}")
    else:
        print("No BMP images found in the folder.")

frameTime = 100 # Length of each frame (in ms)

folder = 'renderedImages/' 
output_gif = 'compilationOfRenderedImages.gif'
bmp_to_gif(folder, output_gif, frameTime)