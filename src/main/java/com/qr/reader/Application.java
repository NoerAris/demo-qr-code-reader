package com.qr.reader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@SpringBootApplication
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		try {
			//Read file from /src/main/resources/static
			//In real application you can upload file then decode QR Code or using scanner
			Resource resource = new ClassPathResource("/static/single.png");
			File file = resource.getFile();
			String decoded = decodeQr(file);
			if (decoded != null) {
				log.info("Decoded text = " + decoded);
			} else {
				log.error("No QR Code found in the image");
			}
		} catch (Exception e) {
			log.error("Could not decode QR Code, IOException :: " + e.getMessage());
		}
	}

	private static String decodeQr(File qrImage) throws IOException {
		BufferedImage bufferedImage =  ImageIO.read(qrImage);
		LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			Result result = new MultiFormatReader().decode(bitmap);
			return result.getText();
		} catch (Exception e) {
			log.error("There is no QR code in the image");
			return null;
		}
	}
}
