package net.openfractal;

import net.openfractal.graphics.MainFrame;

import java.nio.file.Path;
import java.nio.file.Paths;

public class OpenFractal {
    public Path injectedShaderPath;
    public ConfigLoader configLoader;
    public MainFrame frame;

    public OpenFractal(Path injectedShaderPath) {
        this.injectedShaderPath = injectedShaderPath;
    }

    public void start() {
        configLoader = new ConfigLoader();
        frame = new MainFrame();
    }

    public static OpenFractal get() {
        return Main.openFractal;
    }
}