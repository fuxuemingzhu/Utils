package com.dss886.utils.io;

import com.dss886.utils.log.LogUtils;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dss886 on 14/10/21.
 */
public class unRarHelper {

    private static final int MODE_FILE_ALL = 0;
    private static final int MODE_FILE_TYPE = 1;
    private static final int MODE_FILE_NAME = 2;
    private static String srcRarPath;
    private static String targetDirPath;
    private static int unRarMode;
    private static String targetFileType;
    private static String targetFileName;

    /**
     * unRar files to target directory,
     * default to unRar all files and directory
     *
     * @throws IOException
     * @throws RarException
     */
    public static void unRarAll(String srcRarPath, String targetDirPath)
            throws IOException, RarException {
        unRarHelper.srcRarPath = srcRarPath;
        unRarHelper.targetDirPath = targetDirPath;
        unRarHelper.unRarMode = MODE_FILE_ALL;
        unRar();
    }

    public static void unRarOfFileName(String srcRarPath, String targetDirPath, String fileName)
            throws IOException, RarException {
        unRarHelper.srcRarPath = srcRarPath;
        unRarHelper.targetDirPath = targetDirPath;
        unRarHelper.unRarMode = MODE_FILE_NAME;
        unRarHelper.targetFileName = fileName;
        unRar();
    }

    public static void unRarOfFileType(String srcRarPath, String targetDirPath, String typeName)
            throws IOException, RarException {
        unRarHelper.srcRarPath = srcRarPath;
        unRarHelper.targetDirPath = targetDirPath;
        unRarHelper.unRarMode = MODE_FILE_TYPE;
        unRarHelper.targetFileType = typeName;
        unRar();
    }

    private static void unRar()
            throws IOException, RarException {

        // check if the given file is a .rar archive file
        if (!srcRarPath.toLowerCase().endsWith(".rar")) {
            throw new RarException(new Exception("not a .rar file !"));
        }

        // check if target directory exist, when not, create it.
        File dstDirectory = new File(targetDirPath);
        if (!dstDirectory.exists()) {
            dstDirectory.mkdirs();
        }

        Archive archive = new Archive(new File(srcRarPath));
        FileHeader fileHeader = archive.nextFileHeader();
        while (fileHeader != null) {
            if (fileHeader.isDirectory()) { // when folders
                File folder = new File(targetDirPath + File.separator + fileHeader.getFileNameString());
                folder.mkdirs();
            }
            else { // when files
                // check whether in modes or not
                String fileName;
                if(fileHeader.isUnicode()){     //dealing with the non-unicode file name
                    fileName = fileHeader.getFileNameW().trim();
                }else{
                    fileName = fileHeader.getFileNameString().trim();
                }
                fileName = fileName.replaceAll("\\\\", "/");

                int fileNameLength = fileName.length();
                int lastIndex = fileName.lastIndexOf(".");
                String fileType = fileName.substring(lastIndex + 1, fileNameLength);

                if (unRarMode == MODE_FILE_NAME) {
                    if (!fileName.equals(targetFileName)) {
                        fileHeader = archive.nextFileHeader();
                        continue;
                    }
                }

                if (unRarMode == MODE_FILE_TYPE) {
                    if (!fileType.equals(targetFileType)) {
                        fileHeader = archive.nextFileHeader();
                        continue;
                    }
                }
                outputFile(archive, fileHeader, targetDirPath + File.separator +fileName);
            }
            fileHeader = archive.nextFileHeader();
        }
        archive.close();
    }

    /**
     * output files to target path
     */
    private static void outputFile(Archive archive, FileHeader fileHeader, String path) {
        File outFile = new File(path);
        LogUtils.v("AbsolutePath", outFile.getAbsolutePath());
        try {  // write try-catch here to avoid some exceptions interrupting the whole working
            if (!outFile.exists()) {
                if (!outFile.getParentFile().exists()) {    //may have to create father folder
                    outFile.getParentFile().mkdirs();
                }
                outFile.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(outFile);
            archive.extractFile(fileHeader, os);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
