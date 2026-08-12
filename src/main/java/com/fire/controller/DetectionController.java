package com.fire.controller;

import com.fire.service.FireDetectionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DetectionController {

    private final FireDetectionService detector;

    public DetectionController(FireDetectionService detector) {
        this.detector = detector;
    }

    @PostMapping(value = "/detect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> detect(
            @RequestParam("image") MultipartFile image) throws Exception {

        if (image.isEmpty()) {
            throw new IllegalArgumentException("Please upload an image.");
        }

        return detector.detect(image);
    }
}
