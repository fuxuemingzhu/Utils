package com.dss886.utils.io;

import com.dss886.utils.http.utils.ProgressListener;
import com.dss886.utils.log.LogUtils;

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

}
