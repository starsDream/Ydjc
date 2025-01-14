package com.zangcun.store.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/4/8.
 */
@Table(name = "CityModel")
public class CityModel {

    /**
     * id : 1
     * pid : 0
     * name : 中国
     */
    @Column(name = "_id",isId = true)
    private int id;
    @Column(name = "_pid")
    private int pid;
    @Column(name = "_name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CityModel{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                '}';
    }
}
