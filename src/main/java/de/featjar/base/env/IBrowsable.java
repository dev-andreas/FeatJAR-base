package de.featjar.base.env;

import de.featjar.base.FeatJAR;
import de.featjar.base.data.Result;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public interface IBrowsable<T> {
    static void browse(String urlString) {
        browse(URI.create(urlString));
    }

    static void browse(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Result<URI> getBrowseURI(T argument);

    default void browse(T argument) {
        Result<URI> browseURI = getBrowseURI(argument);
        if (browseURI.isEmpty()) {
            FeatJAR.log().error("cannot display " + this + " in browser");
            return;
        }
        FeatJAR.log().info("displaying " + this + " in browser");
        browse(browseURI.get());
    }

    default void debugBrowse(T argument) {
        browse(argument);
        FeatJAR.log().info("press return to continue");
        new Scanner(System.in).nextLine();
    }
}
