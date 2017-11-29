package aaa.pfe.auth.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Anasse on 29/11/2017.
 */

public class LogWriter {

    private static String directoryName = "/AuthApp_Logs/";
    private String fileName;

    public LogWriter(String fileName){
        this.fileName = fileName;
    }

    public void logParams(ArrayList<String> values){

        try {
            writeInFile("Params;");
            for (int i = 0; i < values.size(); i++) {
                writeInFile(values.get(i)+";");
            }
            writeInFile("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logColumnsNames(ArrayList<String> names){

        logNextTentative(names);
    }

    public void logFirstTentative(String name, ArrayList<String> values){
        try {
            writeInFile(name+";");
            for(int i=0;i<values.size();i++){
                writeInFile(values.get(i)+";");
            }
            writeInFile("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logNextTentative(ArrayList values){
        try {
            writeInFile(";");
            for (int i = 0; i < values.size(); i++) {
                writeInFile(values.get(i)+";");
            }
            writeInFile("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeInFile(String txt) throws IOException {

        if(canWriteOnExternalStorage()) {

            File sdcard = Environment.getExternalStorageDirectory();
            // to this path add a new directory path
            File dir = new File(sdcard.getAbsolutePath() + directoryName);
            // create this directory if not already created
            dir.mkdirs();
            // create the file in which we will write the contents
            File file = new File(dir, fileName);
            String data = txt;
            if (!file.exists()) {
                file.createNewFile();
                FileWriter heapFileWritter = new FileWriter(
                        file.getAbsolutePath(), true);
                BufferedWriter heapBufferWritter = new BufferedWriter(
                        heapFileWritter);
                heapBufferWritter.write(data);
                heapBufferWritter.close();

                Log.d("Create and Write :","Done");
            }else{
                // true = append file
                FileWriter heapFileWritter = new FileWriter(
                        file.getAbsolutePath(), true);
                BufferedWriter heapBufferWritter = new BufferedWriter(
                        heapFileWritter);
                heapBufferWritter.write(data);
                heapBufferWritter.close();

                Log.d("Write :","Done");
            }




            /*try {
                FileOutputStream os = new FileOutputStream(file);
                String data = "This is the content of my file";
                os.write(data.getBytes());
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
    }

    public static boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // if storage is mounted return true
            Log.v("sTag", "Yes, can write to external storage.");
            return true;
        }
        return false;
    }


}
