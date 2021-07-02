package net.openfractal;

import java.io.IOException;
import java.nio.file.*;

public class ConfigLoader extends Thread {

    private WatchService watcher;

    public ConfigLoader() {
        try {
            watcher = FileSystems.getDefault().newWatchService();
            WatchKey key = OpenFractal.get().injectedShaderPath.getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) { e.printStackTrace(); }
        start();
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    @Override
    public void run() {
        WatchKey key;
        try {
            key = watcher.take();
        } catch (InterruptedException x) { return; }

        while (true) {
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                WatchEvent<Path> ev = cast(event);
                Path filename = ev.context();

                if(!OpenFractal.get().injectedShaderPath.endsWith(filename)) continue;
                OpenFractal.get().frame.reloadInjectedShader();
            }
        }
    }
}
