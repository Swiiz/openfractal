package net.openfractal;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static OpenFractal openFractal;

    public static void main(String[] args) {
        final Map<String, List<String>> params = parseParameters(args);
        if(
            params == null ||
            params.get("s") == null ||
            params.get("s").get(0) == null
        ) {
            System.err.println("Invalid parameters");
            System.exit(-1);
        }

        openFractal = new OpenFractal(Paths.get("D:\\OpenFractal\\injectedShaders\\mandelbrot.ofglsl"));
        openFractal.start();
    }

    public static void end() {
        System.exit(0);
    }

    private static Map<String, List<String>> parseParameters(String[] args) {
        final Map<String, List<String>> params = new HashMap<>();

        List<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return null;
                }

                options = new ArrayList<>();
                params.put(a.substring(1), options);
            }
            else if (options != null) {
                options.add(a);
            }
            else {
                System.err.println("Illegal parameter usage");
                return null;
            }
        }

        return params;
    }

}
