package com.github.wvengen.maven.proguard;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

/**
 * 处理WAR文件工具类。可压缩或解压缩WAR文件。
 * 
 * @author Xiong Shuhong(shelltea@gmail.com)
 */
public class WarUtils {
    public static void unzip(String warPath, String unzipPath) {
        File warFile = new File(warPath);
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(
                warFile));
            ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(
                ArchiveStreamFactory.JAR, bufferedInputStream);

            JarArchiveEntry entry = null;
            while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
                File file = new File(unzipPath, entry.getName());
                if (file.exists()) {
                    file.delete();
                }
                if (entry.isDirectory()) {
                    file.mkdir();
                } else {
                    OutputStream out = FileUtils.openOutputStream(file);
                    IOUtils.copy(in, out);
                    out.close();
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("未找到war文件");
        } catch (ArchiveException e) {
            System.err.println("不支持的压缩格式");
        } catch (IOException e) {
            System.err.println("文件写入发生错误");
        }
    }

    public static void zip(String destFile, String zipDir) {
        File outFile = new File(destFile);
        try {
            outFile.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new FileOutputStream(outFile));
            ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(
                ArchiveStreamFactory.JAR, bufferedOutputStream);

            if (zipDir.charAt(zipDir.length() - 1) != '/') {
                zipDir += '/';
            }

            Iterator<File> files = FileUtils.iterateFiles(new File(zipDir), null, true);
            while (files.hasNext()) {
                File file = files.next();
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getPath().replace(
                    zipDir.replace("/", "\\"), ""));
                out.putArchiveEntry(zipArchiveEntry);
                IOUtils.copy(new FileInputStream(file), out);
                out.closeArchiveEntry();
            }
            out.finish();
            out.close();
        } catch (IOException e) {
            System.err.println("创建文件失败");
        } catch (ArchiveException e) {
            System.err.println("不支持的压缩格式");
        }
    }
}