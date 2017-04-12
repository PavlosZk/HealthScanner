package com.androidprojects.pablo.healthscanner;

import android.util.Log;

import com.chilkatsoft.CkCrypt2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by pablo on 24/3/2017.
 */

public class NodeServer {


    private String getData(String urlPath) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader =null;

        try {
            //Initialize and config request, then connect to server
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

            //Read data response from server
            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }

        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return result.toString();
    }


    public String postData(String urlPath, JSONObject dataToSend) throws IOException, JSONException {

        StringBuilder result = new StringBuilder();
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;

        try {


            //Initialize and config request, then connect to server.
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);  //enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

            //Write data into server
            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            InputStream inputStream = urlConnection.getInputStream();

            CkCrypt2 crypt = new CkCrypt2();

            boolean success = crypt.UnlockComponent("Anything for 30-day trial");
            if (success != true) {
                Log.i(TAG, crypt.lastErrorText());
            }

            //  Set the encryption algorithm to "AES"
            crypt.put_CryptAlgorithm("aes");

            //  Indicate that the Galois/Counter Mode (GCM) should be used:
            crypt.put_CipherMode("gcm");

            //  KeyLength may be 128, 192, 256
            crypt.put_KeyLength(128);
            //  This is the 128-bit AES secret key (in hex format)
            String K = "feffe9928665731c6d6a8f9467308308";

            //  This is the 16-byte initialization vector:
            String IV = "cafebabefacedbaddecaf888";

            //  This is the additional data to be used as input to the GCM AEAD algorithm,
            //  but is not included in the output.  It plays a role in the computation of the
            //  resulting authenticated tag.
            String AAD = "feedfacedeadbeeffeedfacedeadbeefabaddad2";
            //  EncodingMode specifies the encoding of the output for
            //  encryption, and the input for decryption.
            //  It may be "hex", "url", "base64", or "quoted-printable".
            crypt.put_EncodingMode("hex");

            //  Set the secret key and IV
            crypt.SetEncodedIV(IV,"hex");
            crypt.SetEncodedKey(K,"hex");

            //  Set the additional authenticated data (AAD)
            success = crypt.SetEncodedAad(AAD,"hex");

            String encrypted = crypt.encryptEncoded(dataToSend.toString());
            if (crypt.get_LastMethodSuccess() != true) {
                  Log.i(TAG, crypt.lastErrorText());
            }

           // String encypted = encrypt(dataToSend);

            Log.d("CRYPT", dataToSend.toString());

            Log.d("CRYPT", encrypted);

            String toSend = dataToSend.toString();
//            JSONObject encr = new JSONObject(encrypted);

         //   outputStream.write(encrypted.getBytes());
            bufferedWriter.write(encrypted);
//          bufferedWriter.write(toSend);
            bufferedWriter.flush();

            //Read data response from server
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }

        return result.toString();
    }

    static {
        // Important: Make sure the name passed to loadLibrary matches the shared library
        // found in your project's libs/armeabi directory.
        //  for "libchilkat.so", pass "chilkat" to loadLibrary
        //  for "libchilkatemail.so", pass "chilkatemail" to loadLibrary
        //  etc.
        //
        System.loadLibrary("chilkat");

        // Note: If the incorrect library name is passed to System.loadLibrary,
        // then you will see the following error message at application startup:
        //"The application <your-application-name> has stopped unexpectedly. Please try again."
    }

}




