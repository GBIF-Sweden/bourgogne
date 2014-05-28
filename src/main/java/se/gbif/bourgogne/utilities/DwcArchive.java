/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne.utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.gbif.dwc.text.Archive;
import org.gbif.dwc.text.ArchiveFactory;
import org.gbif.utils.HttpUtil;
import org.gbif.utils.file.CompressionUtil;

/**
 *
 * @author korbinus
 */
public class DwcArchive {

    private Archive archive;
    private File location;

    /**
     * Constructor from a URI
     *
     * @param source
     * @throws IOException
     */
    public DwcArchive(URL source) throws IOException {

        File dwca = File.createTempFile("dwcaFile-", null);
        dwca.deleteOnExit();

        System.out.println("Downloading DarwinCore archive " + source.toString());
        try {
            // download
            DefaultHttpClient client = new DefaultHttpClient();
            HttpUtil http = new HttpUtil(client);
            http.download(source, dwca);
        } catch (Exception e) {
            System.out.println("Cannot download DarwinCore archive. Abort.");
            System.exit(0);
        }

        location = CompressionUtil.decompressFile(dwca);
        System.out.println("Uncompressed into directory: " + location.getAbsolutePath());
        archive = ArchiveFactory.openArchive(location);
    }

    /**
     * Constructor from local DarwinCore Archive
     *
     * @param source
     * @throws IOException
     */
    public DwcArchive(String source) throws IOException {
        File dwca = new File(source);

        System.out.println("Opening local DarwinCore archive " + source);

        location = CompressionUtil.decompressFile(dwca);
        System.out.println("Uncompressed into directory: " + location.getAbsolutePath());
        archive = ArchiveFactory.openArchive(location);

    }

    public Archive getArchive() {
        return archive;
    }

    public void clean() {
        System.out.println("Removing directory " + location.getAbsolutePath());
        try {
            FileUtils.deleteDirectory(location);
        } catch (IOException ex) {
            System.err.println("Error removing " + location.getAbsolutePath());
        }
    }
}
