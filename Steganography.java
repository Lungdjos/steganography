import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Steganography {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter image file path: ");
        String imagePath = scanner.nextLine();
        BufferedImage image = ImageIO.read(new File(imagePath));

        // hide secret message in the image
        hideMessage(image, scanner);

        // retrieve secret message from the image
        retrieveMessage(image, scanner);
    }

    private static void hideMessage(BufferedImage image, Scanner scanner) throws IOException {
        System.out.print("Enter secret message: ");
        String message = scanner.nextLine();

        // convert message to binary
        StringBuilder binaryMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            String binary = Integer.toBinaryString(c);
            while (binary.length() < 8) {
                binary = "0" + binary;
            }
            binaryMessage.append(binary);
        }

        // check if message can fit in the image
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int maxMessageLength = imageWidth * imageHeight * 3 / 8;
        if (binaryMessage.length() > maxMessageLength) {
            System.out.println("Message is too long to hide in the image");
            return;
        }

        // hide the binary message in the LSB of each pixel
        int index = 0;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                if (index < binaryMessage.length()) {
                    red = setLSB(red, binaryMessage.charAt(index++));
                }
                if (index < binaryMessage.length()) {
                    green = setLSB(green, binaryMessage.charAt(index++));
                }
                if (index < binaryMessage.length()) {
                    blue = setLSB(blue, binaryMessage.charAt(index++));
                }

                int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newPixel);
            }
        }

        // save the image with the hidden message
        System.out.print("Enter output file path: ");
        String outputImagePath = scanner.nextLine();
        ImageIO.write(image, "png", new File(outputImagePath));
        System.out.println("Message hidden in the image successfully");
    }

    private static void retrieveMessage(BufferedImage image, Scanner scanner) {
                // retrieve binary message from the LSB of each pixel
                String binaryMessage = "";
                int imageWidth = image.getWidth();
                int imageHeight = image.getHeight();
                for (int y = 0; y < imageHeight; y++) {
                    for (int x = 0; x < imageWidth; x++) {
                        Color pixel = new Color(image.getRGB(x, y));
                        int red = pixel.getRed();
                        int green = pixel.getGreen();
                        int blue = pixel.getBlue();
        
                        binaryMessage += GetLSB(red);
                        binaryMessage += GetLSB(green);
                        binaryMessage += GetLSB(blue);
                    }
                }
        
                // convert binary message to string
                String message = "";
                for (int i = 0; i < binaryMessage.length(); i += 8) {
                    String binary = binaryMessage.substring(i, i + 8);
                    char c = (char) Integer.parseInt(binary, 2);
                    message += c;
                }
        
                System.out.println("Secret message retrieved from the image:");
                System.out.println(message);
            }
        
            private static String GetLSB(int value) {
                return Integer.toString(value & 1);
            }

            private static int setLSB(int blue, char charAt) {
                return 0;
            }
}


