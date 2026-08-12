package com.fire.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Service
public class FireDetectionService {

    public Map<String, Object> detect(MultipartFile file) throws Exception {

        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IllegalArgumentException("Unsupported image format.");
        }

        long firePixels = 0;

        int step = Math.max(
                1,
                Math.min(image.getWidth(), image.getHeight()) / 120
        );

        for (int y = 0; y < image.getHeight(); y += step) {
            for (int x = 0; x < image.getWidth(); x += step) {

                int rgb = image.getRGB(x, y);

                int r = (rgb >> 16) & 255;
                int g = (rgb >> 8) & 255;
                int b = rgb & 255;

                if (r > 180 && r > g * 1.25 && g > b * 1.15) {
                    firePixels++;
                }
            }
        }

        double ratio = (double) firePixels /
                Math.max(1, (image.getWidth() * image.getHeight())
                        / (step * step));

        boolean fire = ratio > 0.035;

        int confidence = (int) Math.round(
                Math.min(99, Math.max(70, 70 + ratio * 800))
        );

        Map<String, Object> result = new HashMap<>();

        result.put(
                "result",
                fire ? "Fire Detected" : "No Fire"
        );

        result.put("confidence", confidence);
        result.put(
                "status",
                fire ? "Danger" : "Safe"
        );

        result.put(
                "filename",
                file.getOriginalFilename()
        );

        return result;
    }
}
