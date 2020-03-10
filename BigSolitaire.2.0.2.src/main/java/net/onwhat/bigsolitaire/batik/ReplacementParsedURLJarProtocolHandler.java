/**
 * Copyright (c) 2005-2008 William McRae
 * All rights reserved.
 */
package net.onwhat.bigsolitaire.batik;

import org.apache.batik.util.ParsedURLDefaultProtocolHandler;
import org.apache.batik.util.ParsedURLData;
import org.apache.batik.util.ParsedURL;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * batik ReplacementParsedURLJarProtocolHandler replacement.
 */
public class ReplacementParsedURLJarProtocolHandler extends ParsedURLDefaultProtocolHandler {

    public static final String JAR = "jar";

    public ReplacementParsedURLJarProtocolHandler() {
        super(JAR);
    }


    // We mostly use the base class parse methods (that leverage
    // java.net.URL.  But we take care to ignore the baseURL if urlStr
    // is an absolute URL.
    public ParsedURLData parseURL(ParsedURL baseURL, String urlStr) {
        // urlStr is absolute...
        if (urlStr.toLowerCase().startsWith(JAR + ":"))
            return parseURL(urlStr);

        // It's relative so base it off baseURL.
        try {
            URL context = new URL(baseURL.toString());
            URL url = new URL(context, urlStr);
            return constructParsedURLData(url);
        } catch (MalformedURLException mue) {
            return super.parseURL(baseURL, urlStr);
        }
    }
}