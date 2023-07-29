package com.internet.yu_zi_fei_android.sqlite.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * Created by Peripateticism on 2021/08/11
 */
public class SettingBean extends SugarRecord implements Serializable {
    @Unique
    public Long id;
    public String name;
    public String sex;
    public String height;
    public String weight;

    public SettingBean() {}

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}