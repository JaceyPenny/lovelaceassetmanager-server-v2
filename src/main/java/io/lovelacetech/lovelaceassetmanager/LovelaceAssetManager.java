package io.lovelacetech.lovelaceassetmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LovelaceAssetManager {
	// I'm adding this comment to test the Codeship auto-deploy
	public static void main(String[] args) {
		SpringApplication.run(LovelaceAssetManager.class, args);
	}
}
