package net.openfractal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
    public static final String loadFileAsString(final String path) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Utils.class.getResourceAsStream(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("ShaderLoader from OpenGL Wrapper : Couldn't find the file at " + path);
        }

        return result.toString();
    }
}
