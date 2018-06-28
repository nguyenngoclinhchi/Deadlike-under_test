package androidexample.com.deadlike.internet;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Permission;
import java.util.jar.Manifest;

import androidexample.com.deadlike.Deadline;
import androidexample.com.deadlike.InputActivity;
import androidexample.com.deadlike.MyApplication;
import androidexample.com.deadlike.R;

public class InternetActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = InternetActivity.class.getSimpleName();

    private String selectedFilePath;
    private final static String SERVER_NAME     = "https://stark-mesa-52163.herokuapp.com/extras";
    private final static String SERVER_URL      = SERVER_NAME.concat("/UploadToServer.php");
    private final static String DOWNLOAD_URL    = SERVER_NAME.concat("/uploads/");



    Button bUpload, bDownload;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        bUpload = (Button) findViewById(R.id.b_upload);
        bUpload.setOnClickListener(this);
        bDownload = (Button) findViewById(R.id.b_download);
        bDownload.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {

        if(v== bUpload){
            EditText abc = (EditText) findViewById(R.id.filepath_editText);
            selectedFilePath = getFilesDir() + "/" + abc.getText().toString();
            //on upload button Click
            if(selectedFilePath != null && !selectedFilePath.equals(getFilesDir() + "/")){
                //dialog = ProgressDialog.show(InternetActivity.this.getApplicationContext(),"","Uploading File...",true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        uploadFile(selectedFilePath);
                    }
                }).start();

            }else{
                Toast.makeText(InternetActivity.this,"Please choose a File First",Toast.LENGTH_SHORT).show();
            }

        }

        if(v == bDownload){
            EditText abc = (EditText) findViewById(R.id.filepath_editText);
            final String fileName = abc.getText().toString();

           if(fileName != null){
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       boolean download = downloadFile(fileName);


                   }
               }).start();

           }
        }
    }

    //android upload file to server
    public int uploadFile(final String uploadName) {
        String selectedFilePath = getFilesDir() + "/" + "deadlist.dat";
        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            //dialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.printWarning("File doesn't exist");
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);
                connection.setRequestProperty("filename", uploadName);


                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
               
                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + uploadName + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyApplication.printWarning("Upload success");
                        }
                    });
                }
                else if (serverResponseCode == 521) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyApplication.printWarning("File existed");
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternetActivity.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternetActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternetActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            //dialog.dismiss();
            return serverResponseCode;
        }
    }
    public boolean downloadFile(final String filename){
        boolean download = true;
        try
        {
            InputStream input = null;
            try{
                URL url = new URL(DOWNLOAD_URL.concat(filename));
                input = url.openStream();
                ActivityCompat.requestPermissions(InternetActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2214);
                File SDCardRoot = Environment.getExternalStorageDirectory(); // location where you want to store
                File directory = new File(SDCardRoot, "/my_folder/"); //create directory to keep your downloaded file
                if (!directory.exists())
                {
                    directory.mkdir();
                }
                File f = new File(directory, filename);
                OutputStream output = new FileOutputStream(f);
                download = true;
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0)
                    {
                        output.write(buffer, 0, bytesRead);
                        download = true;
                    }
                    output.close();
                }

                catch (Exception exception)
                {

                    download = false;

                }
                FileInputStream in = new FileInputStream(f);
                MyApplication.getInstance().readFromFile(in);
                in.close();
                Deadline.updateDeadlineList(InternetActivity.this);
            }
            catch (Exception exception)
            {
                download = false;
            }
            finally
            {
                if(input != null)
                    input.close();
            }
        }
        catch (Exception exception)
        {
            download = false;
        }
        //Download completed
        //Load file
        if(download){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView successText = (TextView) findViewById(R.id.successTextView);
                    successText.setText("Download success");
                }
            });

        }
        return download;
    }

}
