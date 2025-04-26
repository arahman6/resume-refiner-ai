package edu.miu.cs.cs489.resumerefinerai.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DockerLatexCompiler {

    public static boolean compileWithDocker(Path mainTexFile) {
        String texFileName = mainTexFile.getFileName().toString();
        Path parentDir = mainTexFile.getParent();

        try {
            Process process = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "-v", parentDir.toAbsolutePath() + ":/data",
                    "custom-latex",
                    "pdflatex", "-output-directory=/data", texFileName  // <-- no quotes
            ).directory(parentDir.toFile())
                    .redirectErrorStream(true)
                    .start();

            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println("❌ LaTeX ERROR OUTPUT:");
                System.err.println(output);

                // Read the log file if available
                Path logFile = parentDir.resolve(texFileName.replace(".tex", ".log"));
                if (Files.exists(logFile)) {
                    System.err.println("\n LaTeX LOG CONTENT:");
                    System.err.println(Files.readString(logFile));
                }

                return false;
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Docker LaTeX compilation failed", e);
        }
    }

}
