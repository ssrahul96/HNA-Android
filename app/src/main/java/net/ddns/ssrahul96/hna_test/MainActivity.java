package net.ddns.ssrahul96.hna_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView tw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);


        tw = (TextView) findViewById(R.id.textview);
        tw.setText("");
        Toast.makeText(getBaseContext(), "hello", Toast.LENGTH_SHORT).show();


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String mdate = df.format(c.getTime());
        Log.i("date", mdate);
        String url = "https://hna-test.firebaseio.com/" + mdate + ".json";
        Log.i("URL", url);
        String response = "";
        try {
            response = new GetData().execute(url).get();
            Log.i("url main", response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jo = new JSONObject(response);
            Log.i("length",""+jo.length());

            Iterator<?> keys = jo.keys();

            while(keys.hasNext()){
                String key = (String)keys.next();
                JSONObject jo1 = (JSONObject)jo.get(key);
                Log.i("key",key);
                Log.i("value",jo1.toString());
                String jdate= jo1.getString("mdate");
                String jtime = jo1.getString("mtime");
                String jmessage = jo1.getString("mmessage");
                String jpath = jo1.getString("mpath");
                tw.append("\n"+jdate);
                tw.append("\n"+jtime);
                tw.append("\n"+jmessage);
                tw.append("\n"+jpath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response1= "";
        try {
            response1 = new DelData().execute(url).get();
            Log.i("response 1", response1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



    }
}
