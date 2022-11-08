/*
 * Copyright (C) 2022 Sebastian Krieter, Elias Kuiter
 *
 * This file is part of util.
 *
 * util is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with util. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-util> for further information.
 */
package de.featjar.base.bin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utilities for handling Java archives.
 *
 * @author Elias Kuiter
 */
public class JARs {
    /**
     * Extracts a resource from the current Java archive into an output directory.
     *
     * @param resourceName the extracted resource's name, relative to the {@code src/main/resources} directory
     * @param outputPath the output directory
     * @throws IOException if an I/O exception occurs
     */
    public static void extractResource(String resourceName, Path outputPath) throws IOException {
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceName);
        if (url == null) throw new IOException("no resource found at " + resourceName);
        try (InputStream in = url.openStream()) {
            Files.copy(in, outputPath);
        }
    }
}