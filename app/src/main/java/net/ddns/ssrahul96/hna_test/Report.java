package net.ddns.ssrahul96.hna_test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Report extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        SQLiteDatabase db = openOrCreateDatabase("HNA.db", MODE_PRIVATE, null);
        String rsql = "select * from record";
        Log.i("Retrive Sql", rsql);

        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        ArrayList<String> list3 = new ArrayList<String>();
        ArrayList<String> list4 = new ArrayList<String>();
        try {
            Cursor c = db.rawQuery(rsql, null);


            if (c.getCount() == 0) {
                Toast.makeText(getBaseContext(), "No Entry", Toast.LENGTH_SHORT).show();
            } else {

                while (c.moveToNext()) {
                    list1.add(c.getString(1));
                    list2.add(c.getString(2));
                    list3.add(c.getString(3));
                    list4.add(c.getString(4));
                }
            }
        }catch (Exception e){
            Log.i("Exception",e.toString());
            Toast.makeText(getBaseContext(),"No Record Found",Toast.LENGTH_SHORT).show();
        }
        db.close();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        expandableListDetail = ExpandableListDataPump.getData(list1, list2, list3, list4);
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

    public void DeleteDb(View v){
        SQLiteDatabase db = openOrCreateDatabase("HNA.db", MODE_PRIVATE, null);
        String dsql = "drop table record";
        Log.i("Delete Sql",dsql);
        db.execSQL(dsql);
        finish();
        startActivity(getIntent());
    }
}
