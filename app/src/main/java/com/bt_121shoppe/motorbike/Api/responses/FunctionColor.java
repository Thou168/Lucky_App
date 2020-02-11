package com.bt_121shoppe.motorbike.Api.responses;

import android.util.Log;

import com.bt_121shoppe.motorbike.R;

import java.util.ArrayList;
import java.util.List;

public class FunctionColor {
    public static final int[] itemcolor = new int[]{
            R.drawable.white,
            R.drawable.blue,
            R.drawable.ligth_black,
            R.drawable.red,
            R.drawable.yellow,
            R.drawable.pink,
            R.drawable.purple,
            R.drawable.oriange,
            R.drawable.dark_blue,
            R.drawable.dark_gray,
            R.drawable.dark_green,
            R.drawable.dark_red,
            R.drawable.light_blue_sky,
            R.drawable.light_red,
            R.drawable.ligth_green,
            R.drawable.ligth_gray,
            R.drawable.green,
            R.drawable.blue_sky
    };
    public static int[] selectItemColor(String strColor){
        int[] index = new int[0];
        if (strColor != null){
            switch (strColor) {
                case "black":
                    index = new int[]{2};
                    break;
                case "blue":
                    index = new int[]{1};
                    break;
                case "red":
                    index =  new int[]{3};
                    break;
                case "orange":
                    index = new int[]{7};
                    break;
                case "white":
                    index = new int[]{0};
                    break;
                case "yellow":
                    index = new int[]{4};
                    break;
                case "pink":
                    index = new int[]{5};
                    break;
                case "purple":
                    index = new int[]{6};
                    break;
                case "dark blue":
                    index = new int[]{8};
                    break;
                case "brown":
                    index = new int[]{9};
                    break;
                case "dark green":
                    index = new int[]{10};
                    break;
                case "dark red":
                    index = new int[]{11};
                    break;
                case "light blue sky":
                    index = new int[]{12};
                    break;
                case "light red":
                    index = new int[]{13};
                    break;
                case "green":
                    index = new int[]{14};
                    break;
                case "silver":
                    index = new int[]{15};
                    break;
                case "light green":
                    index = new int[]{16};
                    break;
                case "blue sky":
                    index = new int[]{17};
                    break;
                case "white,black":
                    index = new int[]{0,2};
                    break;
                case "white,silver":
                    index = new int[]{0,15};
                    break;
                case "white,blue":
                    index = new int[]{0,1};
                    break;
                case "white,red":
                    index = new int[]{0,3};
                    break;
                case "white,orange":
                    index = new int[]{0,7};
                    break;
                case "white,yellow":
                    index = new int[]{0,4};
                    break;
                case "white,pink":
                    index = new int[]{0,5};
                    break;
                case "white,purple":
                    index = new int[]{0,6};
                    break;
                case "white,dark blue":
                    index = new int[]{0,8};
                    break;
                case "white,brown":
                    index = new int[]{0,9};
                    break;
                case "white,dark green":
                    index = new int[]{0,10};
                    break;
                case "white,dark red":
                    index = new int[]{0,11};
                    break;
                case "white,light blue sky":
                    index = new int[]{0,12};
                    break;
                case "white,light red":
                    index = new int[]{0,13};
                    break;
                case "white,green":
                    index = new int[]{0,14};
                    break;
                case "black,silver":
                    index = new int[]{2,15};
                    break;
                case "black,blue":
                    index = new int[]{2,1};
                    break;
                case "black,red":
                    index = new int[]{2,3};
                    break;
                case "black,orange":
                    index = new int[]{2,7};
                    break;
                case "black,yellow":
                    index = new int[]{2,4};
                    break;
                case "black,pink":
                    index = new int[]{2,5};
                    break;
                case "black,purple":
                    index = new int[]{2,6};
                    break;
                case "black,dark blue":
                    index = new int[]{2,8};
                    break;
                case "black,brown":
                    index = new int[]{2,9};
                    break;
                case "black,dark green":
                    index = new int[]{2,10};
                    break;
                case "black,dark red":
                    index = new int[]{2,11};
                    break;
                case "black,light blue sky":
                    index = new int[]{2,12};
                    break;
                case "black,light red":
                    index = new int[]{2,13};
                    break;
                case "black,green":
                    index = new int[]{2,14};
                    break;
            }
        }
        return index;
    }
    public static String setColor(List<String> arrayColor){
        String strColor = "";
        if (arrayColor.size() > 0){
            switch (arrayColor.size()) {
                case 1:
                    if (arrayColor.contains("2131231207")) {
                        strColor = "white";
                    }else if (arrayColor.contains("2131230843")){
                        strColor = "blue";
                    }else if (arrayColor.contains("2131231068")){
                        strColor = "black";
                    }else if (arrayColor.contains("2131231164")){
                        strColor = "red";
                    }else if (arrayColor.contains("2131231209")){
                        strColor = "yellow";
                    }else if (arrayColor.contains("2131231159")){
                        strColor = "pink";
                    }else if (arrayColor.contains("2131231162")){
                        strColor = "purple";
                    }else if (arrayColor.contains("2131231124")){
                        strColor = "orange";
                    }else if (arrayColor.contains("2131231070")){
                        strColor = "green";
                    }else if (arrayColor.contains("2131231069")){
                        strColor = "silver";
                    }else if (arrayColor.contains("2131230927")){
                        strColor = "brown";
                    }else if (arrayColor.contains("2131230844")){
                        strColor = "blue sky";
                    }else if (arrayColor.contains("2131230956")){
                        strColor = "light green";
                    }else if (arrayColor.contains("2131231067")){
                        strColor = "light red";
                    }else if (arrayColor.contains("2131231066")){
                        strColor = "light blue sky";
                    }else if (arrayColor.contains("2131230929")){
                        strColor = "dark red";
                    }else if (arrayColor.contains("2131230928")){
                        strColor = "dark green";
                    }else if (arrayColor.contains("2131230926")){
                        strColor = "dark blue";
                    }
                    break;
                case 2:
                    if (arrayColor.contains("2131231207") && arrayColor.contains("2131230843")) {
                        strColor = "white" + "," + "blue";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231068")){
                        strColor = "white" + "," + "black";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231164")){
                        strColor = "white" + "," + "red";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231209")){
                        strColor = "white" + "," + "yellow";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231159")){
                        strColor = "white" + "," + "pink";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231162")){
                        strColor = "white" + "," + "purple";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231124")){
                        strColor = "white" + "," + "orange";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231070")){
                        strColor = "white" + "," + "green";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231069")){
                        strColor = "white" + "," + "silver";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131230927")){
                        strColor = "white" + "," + "brown";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131230844")){
                        strColor = "white" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131230956")){
                        strColor = "white" + "," + "light green";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231067")){
                        strColor = "white" + "," + "light red";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131231066")){
                        strColor = "white" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131230929")){
                        strColor = "white" + "," + "dark red";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131230928")){
                        strColor = "white" + "," + "dark green";
                    }else if (arrayColor.contains("2131231207") && arrayColor.contains("2131230926")){
                        strColor = "white" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231068")){
                        strColor = "blue" + "," + "black";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231164")){
                        strColor = "blue" + "," + "red";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231209")){
                        strColor = "blue" + "," + "yellow";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231159")){
                        strColor = "blue" + "," + "pink";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231162")){
                        strColor = "blue" + "," + "purple";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231124")){
                        strColor = "blue" + "," + "orange";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231070")){
                        strColor = "blue" + "," + "green";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231069")){
                        strColor = "blue" + "," + "silver";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131230927")){
                        strColor = "blue" + "," + "brown";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131230844")){
                        strColor = "blue" + "," + "blue sky";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131230956")){
                        strColor = "blue" + "," + "light green";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231067")){
                        strColor = "blue" + "," + "light red";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231066")){
                        strColor = "blue" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131230929")){
                        strColor = "blue" + "," + "dark red";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131230928")){
                        strColor = "blue" + "," + "dark green";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131230926")){
                        strColor = "blue" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231068") && arrayColor.contains("2131230843")) {
                        strColor = "black" + "," + "blue";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231164")){
                        strColor = "black" + "," + "red";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231209")){
                        strColor = "black" + "," + "yellow";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231159")){
                        strColor = "black" + "," + "pink";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231162")){
                        strColor = "black" + "," + "purple";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231124")){
                        strColor = "black" + "," + "orange";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231070")){
                        strColor = "black" + "," + "green";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231069")){
                        strColor = "black" + "," + "silver";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131230927")){
                        strColor = "black" + "," + "brown";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131230844")){
                        strColor = "black" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131230956")){
                        strColor = "black" + "," + "light green";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231067")){
                        strColor = "black" + "," + "light red";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231066")){
                        strColor = "black" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131230929")){
                        strColor = "black" + "," + "dark red";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131230928")){
                        strColor = "black" + "," + "dark green";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131230926")){
                        strColor = "black" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231164") && arrayColor.contains("2131230843")) {
                        strColor = "red" + "," + "blue";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231068")){
                        strColor = "red" + "," + "black";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231209")){
                        strColor = "red" + "," + "yellow";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231159")){
                        strColor = "red" + "," + "pink";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231162")){
                        strColor = "red" + "," + "purple";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231124")){
                        strColor = "red" + "," + "orange";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231070")){
                        strColor = "red" + "," + "green";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231069")){
                        strColor = "red" + "," + "silver";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131230927")){
                        strColor = "red" + "," + "brown";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131230844")){
                        strColor = "red" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131230956")){
                        strColor = "red" + "," + "light green";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231067")){
                        strColor = "red" + "," + "light red";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131231066")){
                        strColor = "red" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131230929")){
                        strColor = "red" + "," + "dark red";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131230928")){
                        strColor = "red" + "," + "dark green";
                    }else if (arrayColor.contains("2131231164") && arrayColor.contains("2131230926")){
                        strColor = "red" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231209") && arrayColor.contains("2131230843")) {
                        strColor = "yellow" + "," + "blue";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231068")){
                        strColor = "yellow" + "," + "black";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231164")){
                        strColor = "yellow" + "," + "red";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231159")){
                        strColor = "yellow" + "," + "pink";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231162")){
                        strColor = "yellow" + "," + "purple";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231124")){
                        strColor = "yellow" + "," + "orange";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231070")){
                        strColor = "yellow" + "," + "green";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231069")){
                        strColor = "yellow" + "," + "silver";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131230927")){
                        strColor = "yellow" + "," + "brown";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131230844")){
                        strColor = "yellow" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131230956")){
                        strColor = "yellow" + "," + "light green";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231067")){
                        strColor = "yellow" + "," + "light red";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131231066")){
                        strColor = "yellow" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131230929")){
                        strColor = "yellow" + "," + "dark red";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131230928")){
                        strColor = "yellow" + "," + "dark green";
                    }else if (arrayColor.contains("2131231209") && arrayColor.contains("2131230926")){
                        strColor = "yellow" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231159") && arrayColor.contains("2131230843")) {
                        strColor = "pink" + "," + "blue";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231068")){
                        strColor = "pink" + "," + "black";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231164")){
                        strColor = "pink" + "," + "red";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231209")){
                        strColor = "pink" + "," + "yellow";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231162")){
                        strColor = "pink" + "," + "purple";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231124")) {
                        strColor = "pink" + "," + "orange";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231070")){
                        strColor = "pink" + "," + "green";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231069")){
                        strColor = "pink" + "," + "silver";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131230927")){
                        strColor = "pink" + "," + "brown";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131230844")){
                        strColor = "pink" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131230956")){
                        strColor = "pink" + "," + "light green";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231067")){
                        strColor = "pink" + "," + "light red";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231066")){
                        strColor = "pink" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131230929")){
                        strColor = "pink" + "," + "dark red";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131230928")){
                        strColor = "pink" + "," + "dark green";
                    }else if (arrayColor.contains("2131231159") && arrayColor.contains("2131230926")){
                        strColor = "pink" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231162") && arrayColor.contains("2131230843")) {
                        strColor = "purple" + "," + "blue";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231068")){
                        strColor = "purple" + "," + "black";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231164")){
                        strColor = "purple" + "," + "red";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231209")){
                        strColor = "purple" + "," + "yellow";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231159")){
                        strColor = "purple" + "," + "pink";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231124")){
                        strColor = "purple" + "," + "orange";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231070")){
                        strColor = "purple" + "," + "green";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231069")){
                        strColor = "purple" + "," + "silver";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131230927")){
                        strColor = "purple" + "," + "brown";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131230844")){
                        strColor = "purple" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131230956")){
                        strColor = "purple" + "," + "light green";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231067")){
                        strColor = "purple" + "," + "light red";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131231066")){
                        strColor = "purple" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131230929")){
                        strColor = "purple" + "," + "dark red";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131230928")){
                        strColor = "purple" + "," + "dark green";
                    }else if (arrayColor.contains("2131231162") && arrayColor.contains("2131230926")){
                        strColor = "purple" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231124") && arrayColor.contains("2131230843")) {
                        strColor = "orange" + "," + "blue";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231068")){
                        strColor = "orange" + "," + "black";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231164")){
                        strColor = "orange" + "," + "red";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231209")){
                        strColor = "orange" + "," + "yellow";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231159")){
                        strColor = "orange" + "," + "pink";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231162")){
                        strColor = "orange" + "," + "purple";
                    }
                    else if (arrayColor.contains("2131231070") && arrayColor.contains("2131230843")) {
                        strColor = "green" + "," + "blue";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231068")){
                        strColor = "green" + "," + "black";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231164")){
                        strColor = "green" + "," + "red";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231209")){
                        strColor = "green" + "," + "yellow";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231159")){
                        strColor = "green" + "," + "pink";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231162")){
                        strColor = "green" + "," + "purple";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231124")){
                        strColor = "green" + "," + "orange";
                    }
                    else if (arrayColor.contains("2131231069") && arrayColor.contains("2131230843")) {
                        strColor = "silver" + "," + "blue";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231068")){
                        strColor = "silver" + "," + "black";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231164")){
                        strColor = "silver" + "," + "red";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231209")){
                        strColor = "silver" + "," + "yellow";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231159")){
                        strColor = "silver" + "," + "pink";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231162")){
                        strColor = "silver" + "," + "purple";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231124")){
                        strColor = "silver" + "," + "orange";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231070")){
                        strColor = "silver" + "," + "green";
                    }
                    break;
                case 3:
                    strColor = "white" + "," + "black";
                    break;
            }
            Log.e("color select:", "" + strColor);
        }
        return strColor;
    }
}
