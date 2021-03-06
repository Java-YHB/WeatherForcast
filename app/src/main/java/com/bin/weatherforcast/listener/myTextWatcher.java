package com.bin.weatherforcast.listener;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bin.weatherforcast.R;
import com.bin.weatherforcast.utils.Weather_contants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stranger on 2016/4/15.
 */
public class myTextWatcher implements TextWatcher {
    Context context;
    ListView search_view_tips_list;
    LinearLayout search_view_hot;
    LinearLayout net_error;
    public myTextWatcher(Context context,ListView search_view_tips_list,LinearLayout search_view_hot,LinearLayout net_error){
        this.context=context;
        this.search_view_tips_list=search_view_tips_list;
        this.search_view_hot=search_view_hot;
        this.net_error=net_error;
        this.net_error.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterTextChanged(s0);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    Editable s0;
    @Override
    public void afterTextChanged(Editable s) {
        s0=s;
        if(isNetworkAvailable(context)){
            String s_content = s.toString();
            net_error.setVisibility(View.GONE);
            if(s_content.length()==0)
            {
                search_view_hot.setVisibility(View.VISIBLE);
            }else{
                search_view_hot.setVisibility(View.GONE);
            }
            int s_length = s_content.getBytes().length;
            char[] chars=s_content.toCharArray();

            if (s_length >= 3) {
                char firt_zi=chars[0];
                //判断第一个字是否字母
                Pattern p = Pattern.compile("[a-zA-Z]");
                Matcher m = p.matcher(String.valueOf(firt_zi));
                ArrayList<HashMap<String,String>> data_list = new ArrayList<>();
                if(m.matches()){

                    for (int i=0;i< Weather_contants.area_name_en.length;i++)
                    {
                        if(Weather_contants.area_name_en[i].contains(s_content)){
                            HashMap<String,String> data = new HashMap<>();
                            data.put("area_name",Weather_contants.area_name_cn[i]);
                            data.put("area_id",Weather_contants.area_name_id[i]);
                            data_list.add(data);
                        }
                    }
                }
                //判断第一个字是否汉字
                p=Pattern.compile("[\u4e00-\u9fa5]");
                m = p.matcher(String.valueOf(firt_zi));
                if(m.matches()){
                    for (int i=0;i< Weather_contants.area_name_cn.length;i++)
                    {
                        if(Weather_contants.area_name_cn[i].contains(s_content)){
                            HashMap<String,String> data = new HashMap<>();
                            data.put("area_name",Weather_contants.area_name_cn[i]);
                            data.put("area_id",Weather_contants.area_name_id[i]);
                            data_list.add(data);
                        }
                    }
                }
                if (data_list.size()!=0 || data_list!=null)
                {
                    SimpleAdapter sa = new SimpleAdapter(context,data_list,R.layout.search_list_item,new String[]{"area_name","area_id"}
                            ,new int[]{R.id.search_list_item_area_name,R.id.search_list_item_area_id});
                    search_view_tips_list.setAdapter(sa);
                    search_view_tips_list.setVisibility(View.VISIBLE);
                }
            }else {
                search_view_tips_list.setVisibility(View.GONE);
            }
        }else {
            search_view_hot.setVisibility(View.GONE);
            search_view_tips_list.setVisibility(View.GONE);
            net_error.setVisibility(View.VISIBLE);
        }
    }
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
