package com.dss886.utils.io;

import com.dss886.utils.http.utils.ProgressListener;
import com.dss886.utils.log.LogUtils;
import sun.rmi.runtime.Log;

import java.io.*;

/**
 * Created by dss886 on 14/10/22.
 */
public class FileHelper {

    /**
     * write content to target file
     * noticed that we don't deal with the charset
     * @throws IOException
     */
    public static void writeFile(String content, File file) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        writeFile(inputStream, file);
    }

    public static void writeFile(InputStream contentStream, File file) throws IOException {
        writeFile(contentStream, file, 0, null);
    }

    /**
     * write content to target file
     * use a default 1024 buffer size
     * @throws IOException
     */
    public static void writeFile(InputStream contentStream, File file, int fileSize, ProgressListener progressListener)
            throws IOException {
        File folder = file.getParentFile();
        if(!folder.exists()){
            folder.mkdirs();
            LogUtils.v("FileHelper", "target Directory is not exit, create it.");
        }

        FileOutputStream outputStream = new FileOutputStream(file);

        int i = 0, j;
        byte buffer[] = new byte[1024];
        while ((j = contentStream.read(buffer)) != -1) {
            if (null != progressListener) {
                i = i + j;
                int progress = i * 100 / fileSize;
                progressListener.onProgressChanged(progress);
            }
            outputStream.write(buffer, 0, j);
        }
        outputStream.flush();
        outputStream.close();
    }

    /**
     * read some line of a file's head
     * @throws IOException
     */
    public static String readFile(File file, int firstLineNum, String charset) throws IOException {
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File is not exits");
        }
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file),charset);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String strLine, result = "";
        for (int i = 0; i < firstLineNum && (strLine = bufferedReader.readLine()) != null; i++) {
            result += strLine;
        }
        bufferedReader.close();
        return result;
    }

    /**
     * read some line of a file's head
     * @throws IOException
     */
    public static String readFile(File file, String charset) throws IOException {
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File is not exits");
        }
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file),charset);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String strLine, result = "";
        while ((strLine = bufferedReader.readLine())!= null) {
            result += strLine;
        }
        bufferedReader.close();
        return result;
    }

    /**
     * rename files in a folder with prefix or suffix
     * unused params (prefix or suffix ) shall be null
     * @throws IOException
     */
    public static void renameFile(File srcfolder, File tarFolder, String prefix, String suffix) throws IOException {
        if (!srcfolder.exists()) {
            throw new IOException("Folder is not exit!");
        }
        if (!srcfolder.isDirectory()) {
            throw new IOException("folder param is not a directory!");
        }
        if (!tarFolder.exists()) {
            tarFolder.mkdirs();
            LogUtils.v("FileHelper", "tarFolder is not exist, create it");
        }
        if (prefix == null) {
            prefix = "";
        }
        if (suffix == null) {
            suffix = "";
        }
        for(File file:srcfolder.listFiles()){
            file.renameTo(new File(tarFolder.getPath() + File.pathSeparator
                    + prefix + file.getName() + suffix));
        }

    }
}
