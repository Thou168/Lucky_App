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

                case "blue,silver":
                    index = new int[]{1,15};
                    break;
                case "blue,red":
                    index = new int[]{1,3};
                    break;
                case "blue,orange":
                    index = new int[]{1,7};
                    break;
                case "blue,yellow":
                    index = new int[]{1,4};
                    break;
                case "blue,pink":
                    index = new int[]{1,5};
                    break;
                case "blue,purple":
                    index = new int[]{1,6};
                    break;
                case "blue,dark blue":
                    index = new int[]{1,8};
                    break;
                case "blue,brown":
                    index = new int[]{1,9};
                    break;
                case "blue,dark green":
                    index = new int[]{1,10};
                    break;
                case "blue,dark red":
                    index = new int[]{1,11};
                    break;
                case "blue,light blue sky":
                    index = new int[]{1,12};
                    break;
                case "blue,light red":
                    index = new int[]{1,13};
                    break;
                case "blue,green":
                    index = new int[]{1,14};
                    break;

                case "red,silver":
                    index = new int[]{3,15};
                    break;
                case "red,orange":
                    index = new int[]{3,7};
                    break;
                case "red,yellow":
                    index = new int[]{3,4};
                    break;
                case "red,pink":
                    index = new int[]{3,5};
                    break;
                case "red,purple":
                    index = new int[]{3,6};
                    break;
                case "red,dark blue":
                    index = new int[]{3,8};
                    break;
                case "red,brown":
                    index = new int[]{3,9};
                    break;
                case "red,dark green":
                    index = new int[]{3,10};
                    break;
                case "red,dark red":
                    index = new int[]{3,11};
                    break;
                case "red,light blue sky":
                    index = new int[]{3,12};
                    break;
                case "red,light red":
                    index = new int[]{3,13};
                    break;
                case "red,green":
                    index = new int[]{3,14};
                    break;

                case "yellow,silver":
                    index = new int[]{4,15};
                    break;
                case "yellow,orange":
                    index = new int[]{4,7};
                    break;
                case "yellow,pink":
                    index = new int[]{4,5};
                    break;
                case "yellow,purple":
                    index = new int[]{4,6};
                    break;
                case "yellow,dark blue":
                    index = new int[]{4,8};
                    break;
                case "yellow,brown":
                    index = new int[]{4,9};
                    break;
                case "yellow,dark green":
                    index = new int[]{4,10};
                    break;
                case "yellow,dark red":
                    index = new int[]{4,11};
                    break;
                case "yellow,light blue sky":
                    index = new int[]{4,12};
                    break;
                case "yellow,light red":
                    index = new int[]{4,13};
                    break;
                case "yellow,green":
                    index = new int[]{4,14};
                    break;

                case "pink,silver":
                    index = new int[]{5,15};
                    break;
                case "pink,orange":
                    index = new int[]{5,7};
                    break;
                case "pink,purple":
                    index = new int[]{5,6};
                    break;
                case "pink,dark blue":
                    index = new int[]{5,8};
                    break;
                case "pink,brown":
                    index = new int[]{5,9};
                    break;
                case "pink,dark green":
                    index = new int[]{5,10};
                    break;
                case "pink,dark red":
                    index = new int[]{5,11};
                    break;
                case "pink,light blue sky":
                    index = new int[]{5,12};
                    break;
                case "pink,light red":
                    index = new int[]{5,13};
                    break;
                case "pink,green":
                    index = new int[]{5,14};
                    break;

                case "purple,silver":
                    index = new int[]{6,15};
                    break;
                case "purple,orange":
                    index = new int[]{6,7};
                    break;
                case "purple,dark blue":
                    index = new int[]{6,8};
                    break;
                case "purple,brown":
                    index = new int[]{6,9};
                    break;
                case "purple,dark green":
                    index = new int[]{6,10};
                    break;
                case "purple,dark red":
                    index = new int[]{6,11};
                    break;
                case "purple,light blue sky":
                    index = new int[]{6,12};
                    break;
                case "purple,light red":
                    index = new int[]{6,13};
                    break;
                case "purple,green":
                    index = new int[]{6,14};
                    break;

                case "orange,silver":
                    index = new int[]{7,15};
                    break;
                case "orange,dark blue":
                    index = new int[]{7,8};
                    break;
                case "orange,brown":
                    index = new int[]{7,9};
                    break;
                case "orange,dark green":
                    index = new int[]{7,10};
                    break;
                case "orange,dark red":
                    index = new int[]{7,11};
                    break;
                case "orange,light blue sky":
                    index = new int[]{7,12};
                    break;
                case "orange,light red":
                    index = new int[]{7,13};
                    break;
                case "orange,green":
                    index = new int[]{7,14};
                    break;

                case "green,silver":
                    index = new int[]{14,15};
                    break;
                case "green,dark blue":
                    index = new int[]{14,8};
                    break;
                case "green,brown":
                    index = new int[]{14,9};
                    break;
                case "green,dark green":
                    index = new int[]{14,10};
                    break;
                case "green,dark red":
                    index = new int[]{14,11};
                    break;
                case "green,light blue sky":
                    index = new int[]{14,12};
                    break;
                case "green,light red":
                    index = new int[]{14,13};
                    break;

                case "silver,dark blue":
                    index = new int[]{15,8};
                    break;
                case "silver,brown":
                    index = new int[]{15,9};
                    break;
                case "silver,dark green":
                    index = new int[]{15,10};
                    break;
                case "silver,dark red":
                    index = new int[]{15,11};
                    break;
                case "silver,light blue sky":
                    index = new int[]{15,12};
                    break;
                case "silver,light red":
                    index = new int[]{15,13};
                    break;

                case "brown,dark blue":
                    index = new int[]{9,8};
                    break;
                case "brown,dark green":
                    index = new int[]{9,10};
                    break;
                case "brown,dark red":
                    index = new int[]{9,11};
                    break;
                case "brown,light blue sky":
                    index = new int[]{9,12};
                    break;
                case "brown,light red":
                    index = new int[]{9,13};
                    break;

                case "blue sky,dark blue":
                    index = new int[]{17,8};
                    break;
                case "blue sky,dark green":
                    index = new int[]{17,10};
                    break;
                case "blue sky,dark red":
                    index = new int[]{17,11};
                    break;
                case "blue sky,light blue sky":
                    index = new int[]{17,12};
                    break;
                case "blue sky,light red":
                    index = new int[]{17,13};
                    break;

                case "light green,dark blue":
                    index = new int[]{16,8};
                    break;
                case "light green,dark green":
                    index = new int[]{16,10};
                    break;
                case "light green,dark red":
                    index = new int[]{16,11};
                    break;
                case "light green,light blue sky":
                    index = new int[]{16,12};
                    break;
                case "light green,light red":
                    index = new int[]{16,13};
                    break;

                case "light red,dark blue":
                    index = new int[]{13,8};
                    break;
                case "light red,dark green":
                    index = new int[]{13,10};
                    break;
                case "light red,dark red":
                    index = new int[]{13,11};
                    break;
                case "light red,light blue sky":
                    index = new int[]{13,12};
                    break;

                case "light blue sky,dark blue":
                    index = new int[]{12,8};
                    break;
                case "light blue sky,dark green":
                    index = new int[]{12,10};
                    break;
                case "light blue sky,dark red":
                    index = new int[]{12,11};
                    break;

                case "dark red,dark green":
                    index = new int[]{11,10};
                    break;
                case "dark red,dark blue":
                    index = new int[]{11,8};
                    break;

                case "dark green,dark blue":
                    index = new int[]{10,8};
                    break;
            }
        }
        return index;
    }
    public static String setColor(List<String> arrayColor){
        String strColor = "white";
        if (arrayColor.size() > 0){
            switch (arrayColor.size()) {
                case 1:
                    if (arrayColor.contains("2131231217")) {
                        strColor = "white";
                    }else if (arrayColor.contains("2131230843")){
                        strColor = "blue";
                    }else if (arrayColor.contains("2131231068")){
                        strColor = "black";
                    }else if (arrayColor.contains("2131231165")){
                        strColor = "red";
                    }else if (arrayColor.contains("2131231219")){
                        strColor = "yellow";
                    }else if (arrayColor.contains("2131231159")){
                        strColor = "pink";
                    }else if (arrayColor.contains("2131231163")){
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
                case 3:
                    if (arrayColor.contains("2131231217") && arrayColor.contains("2131230843")) {
                        strColor = "white" + "," + "blue";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231068")){
                        strColor = "white" + "," + "black";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231165")){
                        strColor = "white" + "," + "red";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231219")){
                        strColor = "white" + "," + "yellow";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231159")){
                        strColor = "white" + "," + "pink";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231163")){
                        strColor = "white" + "," + "purple";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231124")){
                        strColor = "white" + "," + "orange";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231070")){
                        strColor = "white" + "," + "green";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231069")){
                        strColor = "white" + "," + "silver";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131230927")){
                        strColor = "white" + "," + "brown";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131230844")){
                        strColor = "white" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131230956")){
                        strColor = "white" + "," + "light green";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231067")){
                        strColor = "white" + "," + "light red";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131231066")){
                        strColor = "white" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131230929")){
                        strColor = "white" + "," + "dark red";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131230928")){
                        strColor = "white" + "," + "dark green";
                    }else if (arrayColor.contains("2131231217") && arrayColor.contains("2131230926")){
                        strColor = "white" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231068")){
                        strColor = "blue" + "," + "black";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231165")){
                        strColor = "blue" + "," + "red";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231219")){
                        strColor = "blue" + "," + "yellow";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231159")){
                        strColor = "blue" + "," + "pink";
                    }else if (arrayColor.contains("2131230843") && arrayColor.contains("2131231163")){
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
                    else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231165")){
                        strColor = "black" + "," + "red";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231219")){
                        strColor = "black" + "," + "yellow";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231159")){
                        strColor = "black" + "," + "pink";
                    }else if (arrayColor.contains("2131231068") && arrayColor.contains("2131231163")){
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
                    else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231219")){
                        strColor = "red" + "," + "yellow";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231159")){
                        strColor = "red" + "," + "pink";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231163")){
                        strColor = "red" + "," + "purple";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231124")){
                        strColor = "red" + "," + "orange";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231070")){
                        strColor = "red" + "," + "green";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231069")){
                        strColor = "red" + "," + "silver";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131230927")){
                        strColor = "red" + "," + "brown";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131230844")){
                        strColor = "red" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131230956")){
                        strColor = "red" + "," + "light green";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231067")){
                        strColor = "red" + "," + "light red";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131231066")){
                        strColor = "red" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131230929")){
                        strColor = "red" + "," + "dark red";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131230928")){
                        strColor = "red" + "," + "dark green";
                    }else if (arrayColor.contains("2131231165") && arrayColor.contains("2131230926")){
                        strColor = "red" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231219") && arrayColor.contains("2131231159")){
                        strColor = "yellow" + "," + "pink";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131231163")){
                        strColor = "yellow" + "," + "purple";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131231124")){
                        strColor = "yellow" + "," + "orange";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131231070")){
                        strColor = "yellow" + "," + "green";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131231069")){
                        strColor = "yellow" + "," + "silver";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131230927")){
                        strColor = "yellow" + "," + "brown";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131230844")){
                        strColor = "yellow" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131230956")){
                        strColor = "yellow" + "," + "light green";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131231067")){
                        strColor = "yellow" + "," + "light red";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131231066")){
                        strColor = "yellow" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131230929")){
                        strColor = "yellow" + "," + "dark red";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131230928")){
                        strColor = "yellow" + "," + "dark green";
                    }else if (arrayColor.contains("2131231219") && arrayColor.contains("2131230926")){
                        strColor = "yellow" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231159") && arrayColor.contains("2131231163")){
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
                    else if (arrayColor.contains("2131231163") && arrayColor.contains("2131231124")){
                        strColor = "purple" + "," + "orange";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131231070")){
                        strColor = "purple" + "," + "green";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131231069")){
                        strColor = "purple" + "," + "silver";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131230927")){
                        strColor = "purple" + "," + "brown";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131230844")){
                        strColor = "purple" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131230956")){
                        strColor = "purple" + "," + "light green";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131231067")){
                        strColor = "purple" + "," + "light red";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131231066")){
                        strColor = "purple" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131230929")){
                        strColor = "purple" + "," + "dark red";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131230928")){
                        strColor = "purple" + "," + "dark green";
                    }else if (arrayColor.contains("2131231163") && arrayColor.contains("2131230926")){
                        strColor = "purple" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231070")){
                        strColor = "orange" + "," + "green";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231069")){
                        strColor = "orange" + "," + "silver";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131230927")){
                        strColor = "orange" + "," + "brown";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131230844")){
                        strColor = "orange" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131230956")){
                        strColor = "orange" + "," + "light green";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231067")){
                        strColor = "orange" + "," + "light red";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131231066")){
                        strColor = "orange" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131230929")){
                        strColor = "orange" + "," + "dark red";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131230928")){
                        strColor = "orange" + "," + "dark green";
                    }else if (arrayColor.contains("2131231124") && arrayColor.contains("2131230926")){
                        strColor = "orange" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231069")){
                        strColor = "green" + "," + "silver";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131230927")){
                        strColor = "green" + "," + "brown";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131230844")){
                        strColor = "green" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131230956")){
                        strColor = "green" + "," + "light green";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231067")){
                        strColor = "green" + "," + "light red";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131231066")){
                        strColor = "green" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131230929")){
                        strColor = "green" + "," + "dark red";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131230928")){
                        strColor = "green" + "," + "dark green";
                    }else if (arrayColor.contains("2131231070") && arrayColor.contains("2131230926")){
                        strColor = "green" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231069") && arrayColor.contains("2131230927")){
                        strColor = "silver" + "," + "brown";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131230844")){
                        strColor = "silver" + "," + "blue sky";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131230956")){
                        strColor = "silver" + "," + "light green";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231067")){
                        strColor = "silver" + "," + "light red";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131231066")){
                        strColor = "silver" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131230929")){
                        strColor = "silver" + "," + "dark red";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131230928")){
                        strColor = "silver" + "," + "dark green";
                    }else if (arrayColor.contains("2131231069") && arrayColor.contains("2131230926")){
                        strColor = "silver" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131230927") && arrayColor.contains("2131230844")){
                        strColor = "brown" + "," + "blue sky";
                    }else if (arrayColor.contains("2131230927") && arrayColor.contains("2131230956")){
                        strColor = "brown" + "," + "light green";
                    }else if (arrayColor.contains("2131230927") && arrayColor.contains("2131231067")){
                        strColor = "brown" + "," + "light red";
                    }else if (arrayColor.contains("2131230927") && arrayColor.contains("2131231066")){
                        strColor = "brown" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131230927") && arrayColor.contains("2131230929")){
                        strColor = "brown" + "," + "dark red";
                    }else if (arrayColor.contains("2131230927") && arrayColor.contains("2131230928")){
                        strColor = "brown" + "," + "dark green";
                    }else if (arrayColor.contains("2131230927") && arrayColor.contains("2131230926")){
                        strColor = "brown" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131230844") && arrayColor.contains("2131230956")){
                        strColor = "blue sky" + "," + "light green";
                    }else if (arrayColor.contains("2131230844") && arrayColor.contains("2131231067")){
                        strColor = "blue sky" + "," + "light red";
                    }else if (arrayColor.contains("2131230844") && arrayColor.contains("2131231066")){
                        strColor = "blue sky" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131230844") && arrayColor.contains("2131230929")){
                        strColor = "blue sky" + "," + "dark red";
                    }else if (arrayColor.contains("2131230844") && arrayColor.contains("2131230928")){
                        strColor = "blue sky" + "," + "dark green";
                    }else if (arrayColor.contains("2131230844") && arrayColor.contains("2131230926")){
                        strColor = "blue sky" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131230956") && arrayColor.contains("2131231067")){
                        strColor = "light green" + "," + "light red";
                    }else if (arrayColor.contains("2131230956") && arrayColor.contains("2131231066")){
                        strColor = "light green" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131230956") && arrayColor.contains("2131230929")){
                        strColor = "light green" + "," + "dark red";
                    }else if (arrayColor.contains("2131230956") && arrayColor.contains("2131230928")){
                        strColor = "light green" + "," + "dark green";
                    }else if (arrayColor.contains("2131230956") && arrayColor.contains("2131230926")){
                        strColor = "light green" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231067") && arrayColor.contains("2131231066")){
                        strColor = "light red" + "," + "light blue sky";
                    }else if (arrayColor.contains("2131231067") && arrayColor.contains("2131230929")){
                        strColor = "light red" + "," + "dark red";
                    }else if (arrayColor.contains("2131231067") && arrayColor.contains("2131230928")){
                        strColor = "light red" + "," + "dark green";
                    }else if (arrayColor.contains("2131231067") && arrayColor.contains("2131230926")){
                        strColor = "light red" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131231066") && arrayColor.contains("2131230929")){
                        strColor = "light blue sky" + "," + "dark red";
                    }else if (arrayColor.contains("2131231066") && arrayColor.contains("2131230928")){
                        strColor = "light blue sky" + "," + "dark green";
                    }else if (arrayColor.contains("2131231066") && arrayColor.contains("2131230926")){
                        strColor = "light blue sky" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131230929") && arrayColor.contains("2131230928")){
                        strColor = "dark red" + "," + "dark green";
                    }else if (arrayColor.contains("2131230929") && arrayColor.contains("2131230926")){
                        strColor = "dark red" + "," + "dark blue";
                    }
                    else if (arrayColor.contains("2131230928") && arrayColor.contains("2131230926")){
                        strColor = "dark green" + "," + "dark blue";
                    }
                    break;
            }
            Log.e("color select:", "" + strColor);
        }
        return strColor;
    }
}
