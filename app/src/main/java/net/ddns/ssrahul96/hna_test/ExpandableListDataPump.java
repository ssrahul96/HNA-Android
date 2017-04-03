
        package net.ddns.ssrahul96.hna_test;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData(ArrayList<String> list1, ArrayList<String> list2, ArrayList<String> list3, ArrayList<String> list4) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> temp;
        for (int i=0;i<list1.size();i++) {
            temp= new ArrayList<String>();
            temp.add("Date : "+list2.get(i));
            temp.add("Message : "+list3.get(i));
            temp.add("Path : "+list4.get(i));
            expandableListDetail.put("Time : "+list1.get(i), temp);
        }
        return expandableListDetail;
    }
}