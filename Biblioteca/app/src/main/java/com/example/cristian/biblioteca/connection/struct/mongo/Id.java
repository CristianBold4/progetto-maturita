package com.example.cristian.biblioteca.connection.struct.mongo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Cristian on 13/04/2018.
 */

public class Id {

    @SerializedName("$oid")
    @Expose
    private String $oid;

    public String get$oid() {
        return $oid;
    }

    public void set$oid(String $oid) {
        this.$oid = $oid;
    }

    public boolean equals(String other) {
        return $oid.equals(other);
    }

    @Override
    public int hashCode() {
        return $oid != null ? $oid.hashCode() : 0;
    }
}
