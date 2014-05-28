/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 *
 * @author korbinus
 */
public class Gz {

    /**
     * Compress a string in gzip format
     *
     * @param String
     * @return ByteArrayOutputStream
     * @throws IOException
     */
    public static byte[] compress(String str) throws IOException {
        byte[] out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(baos, 65536);
        gzip.write(str.getBytes());
        gzip.close();
        out = baos.toByteArray();
        return out;
    }

    /**
     * Decompress a string in gzip format
     *
     * @param byte[]
     * @return String
     * @throws IOException
     */
    public static String decompress(byte[] in) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(in);
        GZIPInputStream gis = new GZIPInputStream(bais,65536);
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis));
        String outStr = "";
        String line;
        while ((line = bf.readLine()) != null) {
            outStr += line;
        }
        return outStr;
    }
}
