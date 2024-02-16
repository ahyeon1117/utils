package com.hyeon.backend.utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

  /**
   * Converts part to file.
   * @param jakarta.servlet.http.Part	part
   * @return	java.io.File
   */
  public File convertPartToFile(Part part) throws IOException {
    OutputStream out = null;
    InputStream filecontent = null;

    Path tempFilePath = Files.createTempFile(part.getSubmittedFileName(), "");
    File file = tempFilePath.toFile();

    try {
      out = new FileOutputStream(file);
      filecontent = part.getInputStream();

      int read = 0;
      final byte[] bytes = new byte[1024];

      while ((read = filecontent.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
    } catch (FileNotFoundException fne) {
      log.error(fne.getMessage());
    } finally {
      if (out != null) {
        out.close();
      }
      if (filecontent != null) {
        filecontent.close();
      }
    }

    return file;
  }
}
