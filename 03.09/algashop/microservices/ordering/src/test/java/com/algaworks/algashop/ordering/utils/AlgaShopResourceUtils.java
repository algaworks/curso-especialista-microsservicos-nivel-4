package com.algaworks.algashop.ordering.utils;

import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AlgaShopResourceUtils {

	public static String readContent(String resourceName) {
		try (var inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(resourceName)) {
			if (inputStream == null) {
				throw new RuntimeException(new FileNotFoundException(resourceName));
			}
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}