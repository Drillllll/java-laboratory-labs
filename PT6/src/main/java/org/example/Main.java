package org.example;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.io.IOException;
import java.util.List;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

public class Main {
    static Map<Integer, Float> times;

    public static void readDictionary() {
        // Odczyt z pliku
        times = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    int key = Integer.parseInt(parts[0]);
                    float value = Float.parseFloat(parts[1]);
                    times.put(key, value);
                }
            }
            System.out.println("Słownik został odczytany z pliku.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas odczytu z pliku.");
        }

    }

    public static void saveDictionary() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dictionary.txt"))) {
            for (Map.Entry<Integer, Float> entry : times.entrySet()) {
                int key = entry.getKey();
                float value = entry.getValue();
                String line = key + ":" + value;
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Słownik został zapisany do pliku.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisu do pliku.");
        }

    }

    public static void processPictures( String inputDirectory, String outputDirectory, int nThreads)
            throws IOException, ExecutionException, InterruptedException {
        readDictionary();

        List<Path> files; // lista ścieżek do plików
        Path source = Path.of(inputDirectory);
        Stream<Path> stream = Files.list(source); //tworzenie strumienia ścieżek
        files = stream.collect(Collectors.toList()); // tworzenie listy z elementów strumienia za pomocą koletktora

        ForkJoinPool forkJoinPool = new ForkJoinPool(nThreads); // Utworzenie puli wątków
        long startTime = System.currentTimeMillis();


        forkJoinPool.submit(() ->
                files.parallelStream() // zrównoleglenie strumienia plików
                        .map(path -> { //map przekazanie co ma być wykonywane na objekcie path
                            //tworzenie pary ze ścieżki
                            try {
                                String name = path.getFileName().toString();
                                BufferedImage image = ImageIO.read(new File(path.toString()));
                                return Pair.of(name, image);
                            } catch (IOException e) {
                                System.out.println("Error reading image: " + e.getMessage());
                                return null;
                            }
                        })
                        //filtracja pustych par
                        .filter(pair -> pair != null)
                        //z pary (nazwa pliku, obraz) utworzenie pary (nazwa pliku, przetworzony obraz)
                        .map(pair -> {
                            String name = pair.getLeft();
                            BufferedImage image = pair.getRight();
                            // operacje na obrazie: zmiana składowej zielonej z niebieską
                            // utworzenie kopi obrazu
                            BufferedImage transformedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
                            for (int i = 0; i < image.getWidth(); i++) {
                                for (int j = 0; j < image.getHeight(); j++) {
                                    int rgb = image.getRGB(i, j);
                                    Color color = new Color(rgb);
                                    int red = color.getRed();
                                    int blue = color.getBlue();
                                    int green = color.getGreen();
                                    Color outColor = new Color(blue, red, green);
                                    int outRgb = outColor.getRGB();
                                    transformedImage.setRGB(i, j, outRgb);
                                }
                            }
                            return Pair.of(name, transformedImage);
                        })
                        //dla pary (nazwa pliku, przetworzony obraz)
                        .forEach(pair -> {
                            String name = pair.getLeft();
                            BufferedImage image = pair.getRight();
                            Path outputPath = Path.of(outputDirectory, name);
                            try {
                                ImageIO.write(image, "png", outputPath.toFile());
                                System.out.println("Processed image saved: " + outputPath);
                            } catch (IOException e) {
                                System.out.println("Error saving image");
                            }
                        })
        ).get();

        long endTime = System.currentTimeMillis();
        float time = endTime - startTime;
        times.put(nThreads, time);
        saveDictionary();
    }

    public static void main(String[] args)  {
        //String inputDirectory = args[0];
        //String outputDirectory = args[1];
        String inputDirectory = "C:\\Users\\User\\IdeaProjects\\PT6\\source";
        String outputDirectory = "C:\\Users\\User\\IdeaProjects\\PT6\\destination";

        for(int i=1; i<20; i++) {
            try {
                processPictures(inputDirectory, outputDirectory, i);
            }
            catch (Exception e) {
                System.out.println("error");
            }
        }




    }
}