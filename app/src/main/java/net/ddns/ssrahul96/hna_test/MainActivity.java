package net.ddns.ssrahul96.hna_test;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

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

    SQLiteDatabase db;
    private static final String TAG = "MainActivity";
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateDb();
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
                InsertDb(jtime,jdate,jmessage,jpath);
                list1.add(jtime);
                list2.add(jdate);
                list3.add(jmessage);
                list4.add(jpath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),"No Data to show",Toast.LENGTH_SHORT).show();
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

    private void InsertDb(String jtime, String jdate, String jmessage, String jpath) {
        String isql="insert into record (rtime,rdate,rmessage,rpath) values('"+jtime+"','"+jdate+"','"+jmessage+"','"+jpath+"');";
        Log.i("Insert Sql",isql);
        db=openOrCreateDatabase("HNA.db",MODE_PRIVATE,null);
        db.execSQL(isql);
    }

    private void CreateDb() {
        try {
            //db.execSQL("drop table record");
            String csql = " create table if not exists record (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,rtime text," +
                    "rdate text,rmessage text,rpath text)";
            Log.i("Create Sql", csql);
            db = openOrCreateDatabase("HNA.db", MODE_PRIVATE, null);
            db.execSQL(csql);
        }catch (Exception e){
            Log.i("Exception",e.toString());
        }
    }

    public void viewReport(View v){
        db.close();
        Intent i = new Intent(getBaseContext(),Report.class);
        startActivity(i);
    }
}
