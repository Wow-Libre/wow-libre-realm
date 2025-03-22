package com.auth.wow.libre.infrastructure.file;

import com.auth.wow.libre.domain.model.*;
import com.auth.wow.libre.domain.ports.out.file.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Component
public class ConfigsServer implements ObtainConfigsServer, SaveConfigsServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigsServer.class);

    @Override
    public AuthServerConfig getFileConfigServer(String rute, String transactionId) {

        Map<String, String> configuraciones = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rute))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();


                if (linea.isEmpty() || linea.startsWith("#") || linea.startsWith("//")) {
                    continue;
                }


                if (linea.contains("=")) {
                    String[] partes = linea.split("=", 2);
                    String clave = partes[0].trim();
                    String valor = partes[1].trim().replaceAll("(^\"|\"$)", "");

                    configuraciones.put(clave, valor);
                }
            }
            LOGGER.info("Configs: {} ", configuraciones);
            return new AuthServerConfig(configuraciones);
        } catch (IOException e) {
            LOGGER.error("An error occurred while reading the file: {}", e.getMessage());
        }

        return null;
    }


    public void updateConfigFile(String filePath, Map<String, String> replacements) {
        Path backupPath = Paths.get(filePath + ".bak"); // Crear copia de seguridad

        try {
            // Hacer una copia del archivo original
            Files.copy(Paths.get(filePath), backupPath, StandardCopyOption.REPLACE_EXISTING);

            // Leer y modificar el archivo original
            try (BufferedReader br = new BufferedReader(new FileReader(backupPath.toFile()));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

                String line;
                while ((line = br.readLine()) != null) {
                    String trimmedLine = line.trim();

                    // Mantener comentarios y líneas vacías sin cambios
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("#") || trimmedLine.startsWith("//")) {
                        bw.write(line);
                        bw.newLine();
                        continue;
                    }

                    // Procesar líneas con configuraciones (clave=valor)
                    if (trimmedLine.contains("=")) {
                        String[] partes = trimmedLine.split("=", 2);
                        String clave = partes[0].trim();
                        String valorOriginal = partes[1].trim();

                        // Determinar si el valor original tenía comillas
                        boolean tieneComillas = valorOriginal.startsWith("\"") && valorOriginal.endsWith("\"");

                        // Obtener el nuevo valor si existe en replacements, sino usar el original
                        String nuevoValor = replacements.getOrDefault(clave, valorOriginal);

                        // Mantener las comillas si el valor original las tenía
                        if (tieneComillas) {
                            nuevoValor = "\"" + nuevoValor + "\"";
                        }

                        // Escribir la línea modificada
                        bw.write(clave + " = " + nuevoValor);
                    } else {
                        // Escribir la línea sin modificar si no tiene "="
                        bw.write(line);
                    }
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            LOGGER.error("An error occurred while save the file: {}", e.getMessage());

        }
    }
}