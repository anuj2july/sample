package uk.gov.dwp.cmg.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;
public class ParseDynamicArray
{
    public static Object js;

    public static void parseObject(JSONObject json, String key)
    {
        //   System.out.println(json.has(key));
        //   System.out.println("Key of JSON Array is " +json.get(key));
        js=json.get(key);
        System.out.println("Key of JSON Array is " +js);
    }

    public static void getKey(JSONObject json, String key)
    {
        boolean exists = json.has(key);
        Iterator<?> keys;
        String nextKey;
        if (!exists) {
            keys = json.keys();
            while (keys.hasNext()) {
                nextKey = (String) keys.next();
                try {
                    if (json.get(nextKey) instanceof JSONObject)
                    {
                        if (exists == false)
                            getKey(json.getJSONObject(nextKey), key);
                    } else if (json.get(nextKey) instanceof JSONArray)
                    {
                        JSONArray jsonArray = json.getJSONArray(nextKey);
                        for(int i=0; i<jsonArray.length(); i++) {
                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson=new JSONObject(jsonArrayString);
                            if(exists==false)
                            {
                                getKey(innerJson,key);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        } else {
            parseObject(json, key);
        }
    }
}