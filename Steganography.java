import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Steganography {
    public static void main(String[] args) throws IOException {
        // Get image file path and secret message from user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter image file path: ");
        String imagePath = scanner.nextLine();
        System.out.print("Enter secret message: ");
        String secretMessage = scanner.nextLine();

        // Hide secret message in image file
        hideMessage(imagePath, secretMessage);

        // Retrieve hidden message from image file
        String retrievedMessage = retrieveMessage(imagePath);
        System.out.println("Retrieved message: " + retrievedMessage);
    }


    public static void hideMessage(String imagePath, String secretMessage) throws IOException {
        // Load image file
        BufferedImage image = ImageIO.read(new File(imagePath));
        int width = image.getWidth();
        int height = image.getHeight();

        // Convert secret message to binary string
        StringBuilder binaryMessage = new StringBuilder();
        for (char c : secretMessage.toCharArray()) {
            String binaryString = Integer.toBinaryString(c);
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }
            binaryMessage.append(binaryString);
        }

        // Add null terminator to end of message
        binaryMessage.append("00000000");

        // Check if image file can hold secret message
        if (binaryMessage.length() > (width * height)) {
            throw new IllegalArgumentException("Image file is too small to hold secret message.");
        }

        // Hide secret message in image file
        int x = 0;
        int y = 0;
        for (int i = 0; i < binaryMessage.length(); i++) {
            int rgb = image.getRGB(x, y);

            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            // Set least significant bit of red, green, or blue to the corresponding bit of the secret message
            if (i < binaryMessage.length()) {
                char bit = binaryMessage.charAt(i);
                if (bit == '0') {
                    red = red & 0xFE;
                } else {
                    red = red | 0x01;
                }
                i++;
            }
            if (i < binaryMessage.length()) {
                char bit = binaryMessage.charAt(i);
                if (bit == '0') {
                    green = green & 0xFE;
                } else {
                    green = green | 0x01;
                }
                i++;
            }
            if (i < binaryMessage.length()) {
                char bit = binaryMessage.charAt(i);
                if (bit == '0') {
                    blue = blue & 0xFE;
                } else {
                    blue = blue | 0x01;
                }
                i++;
            }

            // Set new RGB value
            rgb = (red << 16) | (green << 8) | blue;
            image.setRGB(x, y, rgb);

            // Move to next pixel
            x++;
            if (x >= width) {
                x = 0;
                y++;
                if (y >= height) {
                    throw new IllegalArgumentException("Image file is too small to hold secret message.");
                }
            }
        }

        // Save modified image file
        File outputImage = new File(imagePath);
        ImageIO.write(image, "png", outputImage);
    }

    private static String retrieveMessage(String imagePath) {
        return null;
    }
}

