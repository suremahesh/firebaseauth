package com.example.maheshs.firebaseauth;

/**
 * Created by maheshs on 5/9/2017.
 */

public class Uploadclass
{
   // public String name;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Uploadclass() {
    }

    public Uploadclass( String url) {
       // this.name = name;
        this.url= url;
    }

   /* public String getName() {
        return name;
    }*/

    public String getUrl() {
        return url;
    }
}
