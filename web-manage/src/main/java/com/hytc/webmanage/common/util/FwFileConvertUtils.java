package com.hytc.webmanage.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.hytc.webmanage.common.exception.BusinessException;
import com.hytc.webmanage.common.exception.MessageCodeMaster;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum FwFileConvertUtils {
    me;

    private static final int BUFFER_SIZE = 2 * 1024;
    private static final String OUT_PUT_PATH = "out path is：";
    private static final String SLASH = "/";
    private static final String TAKE_TIME_MESSAGE = "it take care：";
    private static final String MS = " ms";
    private static final String GBK = "gbk";
    //カンマ
    private static final String COMMA = ",";
    //改行
    private static final String NEW_LINE = "\r\n";

    private static final String CSV_FILE_NAME = "CSVパス";

    /**
     * encode file to String
     *
     * @param is
     * @return file content Str
     */
    public final String GetFileStrFromIs(InputStream is) throws IOException {
        byte[] data = null;
        //read file form  path
        try (var in = is) {
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
        var encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * encode file to String
     *
     * @param filePath
     * @return file content Str
     */
    public final String GetFileStr(String filePath) throws IOException {
        byte[] data = null;
        //read file form  path
        try (var in = new FileInputStream(filePath)) {
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
        var encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * Decode the String 2 file
     *
     * @param fileContent
     * @param fileOutPath
     * @return process result
     */
    public final boolean GenerateFile(String fileContent, String fileOutPath) {
        // 不存在のフォールダ
        if (fileContent == null) {
            return false;
        }
        var decoder = Base64.getDecoder();
        try (var out = new FileOutputStream(fileOutPath);) {
            //Base64 decode
            byte[] b = decoder.decode(fileContent);

            //異常データ調整
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param files
     * @param outFile
     */
    public final void takeFile2Zip(List<String> files, String outFile) {
        long start = System.currentTimeMillis();
        File outFileMyFile = new File(outFile);

        if (!outFileMyFile.exists()) {
            try {
                outFileMyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (var out = new FileOutputStream(new File(outFile));) {
            try (var zos = new ZipOutputStream(out)) {
                for (var file : files) {
                    var srcFile = new File(file);
                    byte[] buf = new byte[BUFFER_SIZE];
                    zos.putNextEntry(new ZipEntry(srcFile.getName()));
                    int len;
                    try (var in = new FileInputStream(srcFile);) {
                        while ((len = in.read(buf)) != -1) {
                            zos.write(buf, 0, len);
                        }
                        zos.closeEntry();
                    }
                }
                long end = System.currentTimeMillis();
                log.info(TAKE_TIME_MESSAGE + (end - start) + MS);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param zipPath
     * @param descDir
     */
    public final void freeZip2File(String zipPath, String descDir) {
        File zipFile = new File(zipPath);

        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try (var zip = new ZipFile(zipFile, Charset.forName(GBK));) {
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();

                try (var in = zip.getInputStream(entry)) {
                    var outPath = (descDir + zipEntryName).replace(SLASH, File.separator);

                    File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    if (new File(outPath).isDirectory()) {
                        continue;
                    }

                    log.info(OUT_PUT_PATH + outPath);
                    try (var out = new FileOutputStream(outPath);) {
                        byte[] buf1 = new byte[2048];
                        int len;
                        while ((len = in.read(buf1)) > 0) {
                            out.write(buf1, 0, len);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * フォルダー圧縮
     *
     * @param srcDir           フォルダーパス
     * @param outPath          出力パス
     * @param KeepDirStructure フォルダー内部構造保留フラグ true保留 false廃棄
     */
    public final void transferFolderToZip(String srcDir, String outPath, boolean KeepDirStructure) {

        long start = System.currentTimeMillis();

        try (FileOutputStream out = new FileOutputStream(new File(outPath));
             ZipOutputStream zos = new ZipOutputStream(out)) {
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println(TAKE_TIME_MESSAGE + (end - start) + MS);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        }
    }

    public static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + File.separator));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + File.separator + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    /**
     * @param t
     * @param destFileAbsolutePath
     */
    public final <T> void saveDto2CsvFile(T t, String destFileAbsolutePath, Integer headerCount) {
        var destClass = t.getClass();
        if (!destFileAbsolutePath.endsWith(".csv")) {
            throw new BusinessException(MessageCodeMaster.W_0000, "フォーマットエラーです。");
        }

        var file = new File(destFileAbsolutePath);

        File file2 = new File(destFileAbsolutePath.substring(0, destFileAbsolutePath.lastIndexOf(File.separator)));
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (var fileWriter = new FileWriter(destFileAbsolutePath, true)) {

            var nameStr = "";
            var contentStr = "";
            for (Field field : destClass.getDeclaredFields()) {
                field.setAccessible(true);
                var fileName = field.getName();
                var fileContent = "";
                if (field.get(t) instanceof LocalDateTime) {
                    fileContent = field.get(t).toString().replaceAll("T", " ");
                } else {
                    fileContent = String.valueOf(field.get(t));
                }

                nameStr = nameStr + fileName + COMMA;
                contentStr = contentStr + fileContent + COMMA;
            }
            if (headerCount == 0) {
                fileWriter.append(nameStr.substring(0, nameStr.length() - 1));
                fileWriter.append(NEW_LINE);
                headerCount++;
            }
            fileWriter.append(contentStr.substring(0, contentStr.length() - 1));
            fileWriter.append(NEW_LINE);
            fileWriter.flush();
        } catch (IllegalAccessException | IOException e) {
            log.warn(e);
            throw new BusinessException(MessageCodeMaster.W_0000, "CSV出力エーラです。");
        }
    }

    /**
     * @param destDtoLst
     * @param destFileAbsolutePath
     */
    public final <T> void saveDto2CsvFile(List<T> destDtoLst, String destFileAbsolutePath) {
        var headerCount = Integer.valueOf(0);
        for (Object o : destDtoLst) {
            saveDto2CsvFile(o, destFileAbsolutePath, headerCount);
            headerCount++;
        }
    }


    public final <T> List<T> convertCsvFile2Dto(String csvFilePath, Class m) {

        try {
            FileReader csvReader = new FileReader(csvFilePath);
            List<T> csvList = new CsvToBeanBuilder<T>(csvReader)
                    .withType(m).build().parse();
            csvReader.close();
            return csvList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
