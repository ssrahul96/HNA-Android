package net.ddns.ssrahul96.hna_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

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

        ArrayList<String> list1=new ArrayList<String>();
        ArrayList<String> list2=new ArrayList<String>();
        ArrayList<String> list3=new ArrayList<String>();
        ArrayList<String> list4=new ArrayList<String>();

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
                list1.add(jtime);
                list2.add(jdate);
                list3.add(jmessage);
                list4.add(jpath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response1= "";
/*
        try {
            response1 = new DelData().execute(url).get();
            Log.i("response 1", response1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData(list1,list2, list3,list4);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(),expandableListTitle.get(groupPosition) + " List Expanded.",Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(),expandableListTitle.get(groupPosition) + " List Collapsed.",Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(getApplicationContext(),expandableListTitle.get(groupPosition)+ " -> "+ expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }
}
