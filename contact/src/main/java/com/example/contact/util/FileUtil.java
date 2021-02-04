package com.example.contact.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void printStringToFile(String path, String name, String str) {
        File file = new File(path, name);
        Log.d("FileUtil", "path = " + file.getAbsolutePath());
        FileWriter writer = null;
        try {
            Log.d("FileUtil", "createNewFile");
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file, true);
            writer.write(str);
            writer.flush();
            writer.close();
            writer = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
