package com.example.aktivatorpaketa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Options {

    private static List<Option> optionsList = init();

    private static List<Option> init() {
        return new ArrayList<>();
    }

    public static void save(String path) {
        File file = new File(path, "data.dat");

        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(optionsList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(String path) {
        File file = new File(path, "data.dat");
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream o = new ObjectInputStream(fi);
            optionsList = (List<Option>) o.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<Option> getOptionList() {
        return optionsList;
    }
}
