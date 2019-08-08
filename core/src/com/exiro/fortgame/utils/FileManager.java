package com.exiro.fortgame.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class FileManager {


    public static FileManager fileManager;

    static public String readFile(FileHandle f) throws IOException {

        return Base64Coder.decodeString(f.readString());
    }

    static public Document getXMLDocuement(String path) {
        try {
            return convertStringToXMLDocument(readFile(Gdx.files.internal(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Document convertStringToXMLDocument(String xmlString) {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

