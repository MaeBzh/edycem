package com.imie.edycem.entity;

import com.tactfactory.harmony.annotation.Column;
import com.tactfactory.harmony.annotation.Entity;
import com.tactfactory.harmony.annotation.GeneratedValue;
import com.tactfactory.harmony.annotation.Id;
import com.tactfactory.harmony.annotation.Table;

import org.json.JSONObject;


@Entity
@Table
public class Url {

    @Id
    @Column(type = Column.Type.INTEGER, hidden = true)
    @GeneratedValue(strategy = GeneratedValue.Strategy.MODE_IDENTITY)
    private int id;
    @Column(type = Column.Type.INTEGER, nullable = true)
    private int url;
    @Column(type = Column.Type.TEXT, nullable = true)
    private JSONObject json;

}
