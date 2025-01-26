import pygame
import time
import sys
import imageio

def read_frames(filename):
    with open(filename, 'r') as f:
        content = f.read().strip()

    frames = []
    current_frame = []

    for line in content.split('\n'):
        if line.strip():
            current_frame.append(list(line))
        else:
            if current_frame:
                frames.append(current_frame)
                current_frame = []

    if current_frame:
        frames.append(current_frame)

    return frames


def draw_frame(screen, frame, cell_size, regular_font, bold_font, large_font, frame_index, header_height):
    colors = {
    '.': (50, 50, 50),      # Deep slate for empty spaces
    '@': (0, 255, 0),       # Bright green for robot '@'
    '#': (255, 99, 71),     # Tomato red for walls '#'
    '[': (255, 223, 0),     # Yellow-Gold for box left edge
    ']': (255, 223, 0),     # Yellow-Gold for box right edge
    }
    screen.fill((25, 20, 20))   # Rich Dark Brown (background)

    # Find @ coordinates
    at_coords = None
    for y, row in enumerate(frame):
        for x, char in enumerate(row):
            if char == '@':
                at_coords = (x, y)
                break
        if at_coords:
            break

    info_text = f"Frame: {frame_index}"
    if at_coords:
        info_text += f"  @: ({at_coords[0]}, {at_coords[1]})"
    text = regular_font.render(info_text, True, (255, 255, 255))
    text_rect = text.get_rect(center=(screen.get_width() // 2, header_height // 2))
    screen.blit(text, text_rect)

    for y, row in enumerate(frame):
        for x, char in enumerate(row):
            if char in colors:
                if char == '@':
                    font = large_font
                elif char in ['[', ']']:
                    font = bold_font
                else:
                    font = regular_font

                text = font.render(char, True, colors[char])
                text_rect = text.get_rect(center=(x * cell_size + cell_size/2,
                                                y * cell_size + cell_size/2 + header_height))
                screen.blit(text, text_rect)

def main():
    frames = read_frames(sys.argv[1])

    pygame.init()

    frame_height = len(frames[0])
    frame_width = len(frames[0][0])
    print(frame_height)
    print(frame_width)

    window_width = 1380
    header_height = 40
    base_cell_size = min(window_width // frame_width,
                        (window_width // frame_width * frame_height) // frame_height)
    cell_size = int(base_cell_size * 1.15)
    window_height = cell_size * frame_height + header_height

    screen = pygame.display.set_mode((frame_width * cell_size, window_height))
    pygame.display.set_caption("Warehouse Woes!")

    font_size = int(cell_size * 0.8)
    # Other font options include 'Arial', 'Helvetica', 'Comic Sans MS', 'Courier New'
    regular_font = pygame.font.SysFont('Arial', font_size)
    bold_font = pygame.font.SysFont('Arial', font_size, bold=True)
    large_font = pygame.font.SysFont('Arial', int(font_size * 1.1), bold=True)

    clock = pygame.time.Clock()
    frame_index = 0
    running = True

    video_frames = []

    for frame in frames:

        if not running:
            break

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

        draw_frame(screen, frame, cell_size, regular_font, bold_font, large_font, frame_index, header_height)
        pygame.display.flip()

        frame_data = pygame.surfarray.array3d(screen)
        video_frames.append(frame_data.transpose([1, 0, 2]))

        frame_index += 1
        # clock.tick(5)
        # clock.tick(30)
        clock.tick(50)

    pygame.quit()

    imageio.mimsave('warehouse.mp4', video_frames, fps=20)
    print("Animation saved to 'warehouse.mp4'")

if __name__ == '__main__':
    main()
