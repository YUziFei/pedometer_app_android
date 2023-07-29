package com.internet.yu_zi_fei_android.sqlite.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * Created by Peripateticism on 2021/08/11
 */
public class HistoryData extends SugarRecord implements Serializable {

    @Unique
    public Long id;
    public String stepNumber;
    public String time;
    public String data;
    public String speed;
    public String distance;
    public String heat;

} 