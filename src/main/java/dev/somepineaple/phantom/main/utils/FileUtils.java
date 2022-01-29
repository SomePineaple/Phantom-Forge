package dev.somepineaple.phantom.main.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import dev.somepineaple.phantom.Phantom;

public class FileUtils {
	private static File BASE_DIR = new File(Phantom.NAME + Phantom.VERSION);
	private static File CONF_DIR = new File(BASE_DIR, "configs");
	
	public static void init() {
		if (!BASE_DIR.exists())
			BASE_DIR.mkdirs();
		if (!CONF_DIR.exists())
			CONF_DIR.mkdirs();
	}
	
	public static void writeStringToFile(String path, String newData) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(BASE_DIR.getName() + "/" + path));
			writer.write(newData);
			writer.close();
		} catch (IOException e) {
			System.out.println("Failed to write to file " + path);
			e.printStackTrace();
		}
	}
	
	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();
		
		try (Stream<String> stream = Files.lines( Paths.get(BASE_DIR.getName() + "/" + path), StandardCharsets.UTF_8)) {
            stream.forEach(s -> builder.append(s).append("\n"));
        } catch (IOException e) {
        	System.out.println("Failed to load " + path + " to a string");
        	e.printStackTrace();
        }
		
		return builder.toString();
	}
}
