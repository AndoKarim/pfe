package aaa.pfe.auth.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Anasse on 29/11/2017.
 */

public class LogWriter {

    private static String directoryName = "/AuthApp_Logs/";
    private String fileName;

    public LogWriter(String method){
        DateFormat df = new SimpleDateFormat("dd MM yyyy_ HH_mm");
        String date = df.format(Calendar.getInstance().getTime());
        date = date.replace(" ","_");
        this.fileName = method + "_" + date + ".csv";
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
            File dir = new File(sdcard.getAbsolutePath() + directoryName);
            dir.mkdirs();
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
                FileWriter heapFileWritter = new FileWriter(
                        file.getAbsolutePath(), true);
                BufferedWriter heapBufferWritter = new BufferedWriter(
                        heapFileWritter);
                heapBufferWritter.write(data);
                heapBufferWritter.close();

                Log.d("Write :","Done");
            }
        }
    }


    public void writePassFaceParams(ArrayList<String> param) {
        try {
            writeInFile("Params;");
            for (int i = 0; i < param.size(); i++) {
                writeInFile(param.get(i) + "\n");

            }
            writeInFile("\n");
            writeInFile("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePassFaceCols(ArrayList<String> start) {
        try {
            writeInFile("Watching;");
            for (int i = 0; i < start.size(); i++) {
                writeInFile(start.get(i) + ";");
            }
            writeInFile("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePassfaceEvent(ArrayList<String> values) {
        try {
            for (int i = 0; i < values.size(); i++) {
                writeInFile(values.get(i) + "\n");

            }
            writeInFile("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
