package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * List of data for to do fuzzzy.
 *
 * @author andre, vinicius
 */
public class CsvFile {

    private File file;
    private String data;

    public CsvFile(String pathName) {
        this.file = new File(pathName);
        this.data = initData();
    }
    
    private boolean isLineEmpty(String line){
        return line.matches(",+");
    }

    private String initData() {
        String temp = "",
                st;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            while ((st = br.readLine()) != null && !isLineEmpty(st)) {
                temp += st + '\n';
            }
        } catch (FileNotFoundException ex) {
            System.out.println("O arquivo CSV não foi possível localizar:\n" + file.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Problema ao ler o arquivo CSV:\n" + file.getAbsolutePath());
        }
        return replaceOnlyTab(temp);
    }

    /**
     * Problem to split only where is collumn, so where is a string must skip, and where is comma tab.
     * @param line data of csv.
     * @return data adjusted.
     */
    private String replaceOnlyTab(String line) {
        boolean open = false;
        String newLine = "";
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                open = !open;
            }
            c = (open && c == ',') ? c
                    : (!open && c == ',') ? '\t' : c;
            newLine += c;
        }
        return newLine;
    }

    /**
     * Convert the text in matrix.
     * @return string matrix.
     */
    public String[][] getData() {
        String[] rows = data.split("\n");
        String[] collumns = rows[0].split("\t");
        String[][] temp = new String[rows.length][collumns.length];

        for (int i = 0; i < rows.length; i++) {
            collumns = rows[i].split("\t");
            for (int j = 0; j < collumns.length; j++) {
                temp[i][j] = collumns[j];
            }
        }
        return temp;
    }

}
