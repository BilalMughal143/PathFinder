package com.example.bc110403238.routeapp.Controller;

import android.app.Application;

import com.example.bc110403238.routeapp.Modal.MapDictionery;

/**
 * Created by zaib on 7/10/2015.
 */
public class secondSinglton {
        private static secondSinglton myObject;
        public String html = null;
        public String duration = null;
        public String distance = null ;
        MapDictionery mp = new MapDictionery();


        private secondSinglton() {


        }

        public static  secondSinglton getMyObject(){
            if(myObject == null){
                myObject = new secondSinglton();

            }
            return myObject;
        }
    }
