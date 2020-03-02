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
        int[] index = new int[]{};
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
    public static String setColor(List<Integer> arrayColor){
        String strColor = "white";
        if (arrayColor.size() > 0){
            switch (arrayColor.size()) {
                case 1:
                    if (arrayColor.contains(0)) {
                        strColor = "white";
                    }else if (arrayColor.contains(1)){
                        strColor = "blue";
                    }else if (arrayColor.contains(2)){
                        strColor = "black";
                    }else if (arrayColor.contains(3)){
                        strColor = "red";
                    }else if (arrayColor.contains(4)){
                        strColor = "yellow";
                    }else if (arrayColor.contains(5)){
                        strColor = "pink";
                    }else if (arrayColor.contains(6)){
                        strColor = "purple";
                    }else if (arrayColor.contains(7)){
                        strColor = "orange";
                    }else if (arrayColor.contains(8)){
                        strColor = "dark blue";
                    }else if (arrayColor.contains(9)){
                        strColor = "brown";
                    }else if (arrayColor.contains(10)){
                        strColor = "dark green";
                    }else if (arrayColor.contains(11)){
                        strColor = "dark red";
                    }else if (arrayColor.contains(12)){
                        strColor = "light blue sky";
                    }else if (arrayColor.contains(13)){
                        strColor = "light red";
                    }else if (arrayColor.contains(14)){
                        strColor = "light green";
                    }else if (arrayColor.contains(15)){
                        strColor = "silver";
                    }else if (arrayColor.contains(16)){
                        strColor = "green";
                    }else if (arrayColor.contains(17)){
                        strColor = "blue sky";
                    }
                    break;
                case 2:
                case 3:
                    if (arrayColor.contains(0) && arrayColor.contains(1)) {
                        strColor = "white" + "," + "blue";
                    }else if (arrayColor.contains(0) && arrayColor.contains(2)){
                        strColor = "white" + "," + "black";
                    }else if (arrayColor.contains(0) && arrayColor.contains(3)){
                        strColor = "white" + "," + "red";
                    }else if (arrayColor.contains(0) && arrayColor.contains(4)){
                        strColor = "white" + "," + "yellow";
                    }else if (arrayColor.contains(0) && arrayColor.contains(5)){
                        strColor = "white" + "," + "pink";
                    }else if (arrayColor.contains(0) && arrayColor.contains(6)){
                        strColor = "white" + "," + "purple";
                    }else if (arrayColor.contains(0) && arrayColor.contains(7)){
                        strColor = "white" + "," + "orange";
                    }else if (arrayColor.contains(0) && arrayColor.contains(8)){
                        strColor = "white" + "," + "dark blue";
                    }else if (arrayColor.contains(0) && arrayColor.contains(9)){
                        strColor = "white" + "," + "brown";
                    }else if (arrayColor.contains(0) && arrayColor.contains(10)){
                        strColor = "white" + "," + "dark green";
                    }else if (arrayColor.contains(0) && arrayColor.contains(11)){
                        strColor = "white" + "," + "dark red";
                    }else if (arrayColor.contains(0) && arrayColor.contains(12)){
                        strColor = "white" + "," + "light blue sky";
                    }else if (arrayColor.contains(0) && arrayColor.contains(13)){
                        strColor = "white" + "," + "light red";
                    }else if (arrayColor.contains(0) && arrayColor.contains(14)){
                        strColor = "white" + "," + "light green";
                    }else if (arrayColor.contains(0) && arrayColor.contains(15)){
                        strColor = "white" + "," + "silver";
                    }else if (arrayColor.contains(0) && arrayColor.contains(16)){
                        strColor = "white" + "," + "green";
                    }else if (arrayColor.contains(0) && arrayColor.contains(17)){
                        strColor = "white" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(1) && arrayColor.contains(2)){
                        strColor = "blue" + "," + "black";
                    }else if (arrayColor.contains(1) && arrayColor.contains(3)){
                        strColor = "blue" + "," + "red";
                    }else if (arrayColor.contains(1) && arrayColor.contains(4)){
                        strColor = "blue" + "," + "yellow";
                    }else if (arrayColor.contains(1) && arrayColor.contains(5)){
                        strColor = "blue" + "," + "pink";
                    }else if (arrayColor.contains(1) && arrayColor.contains(6)){
                        strColor = "blue" + "," + "purple";
                    }else if (arrayColor.contains(1) && arrayColor.contains(7)){
                        strColor = "blue" + "," + "orange";
                    }else if (arrayColor.contains(1) && arrayColor.contains(8)){
                        strColor = "blue" + "," + "dark blue";
                    }else if (arrayColor.contains(1) && arrayColor.contains(9)){
                        strColor = "blue" + "," + "brown";
                    }else if (arrayColor.contains(1) && arrayColor.contains(10)){
                        strColor = "blue" + "," + "dark green";
                    }else if (arrayColor.contains(1) && arrayColor.contains(11)){
                        strColor = "blue" + "," + "dark red";
                    }else if (arrayColor.contains(1) && arrayColor.contains(12)){
                        strColor = "blue" + "," + "light blue sky";
                    }else if (arrayColor.contains(1) && arrayColor.contains(13)){
                        strColor = "blue" + "," + "light red";
                    }else if (arrayColor.contains(1) && arrayColor.contains(14)){
                        strColor = "blue" + "," + "light green";
                    }else if (arrayColor.contains(1) && arrayColor.contains(15)){
                        strColor = "blue" + "," + "silver";
                    }else if (arrayColor.contains(1) && arrayColor.contains(16)){
                        strColor = "blue" + "," + "green";
                    }else if (arrayColor.contains(1) && arrayColor.contains(17)){
                        strColor = "blue" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(2) && arrayColor.contains(3)){
                        strColor = "black" + "," + "red";
                    }else if (arrayColor.contains(2) && arrayColor.contains(4)){
                        strColor = "black" + "," + "yellow";
                    }else if (arrayColor.contains(2) && arrayColor.contains(5)){
                        strColor = "black" + "," + "pink";
                    }else if (arrayColor.contains(2) && arrayColor.contains(6)){
                        strColor = "black" + "," + "purple";
                    }else if (arrayColor.contains(2) && arrayColor.contains(7)){
                        strColor = "black" + "," + "orange";
                    }else if (arrayColor.contains(2) && arrayColor.contains(8)){
                        strColor = "black" + "," + "dark blue";
                    }else if (arrayColor.contains(2) && arrayColor.contains(9)){
                        strColor = "black" + "," + "brown";
                    }else if (arrayColor.contains(2) && arrayColor.contains(10)){
                        strColor = "black" + "," + "dark green";
                    }else if (arrayColor.contains(2) && arrayColor.contains(11)){
                        strColor = "black" + "," + "dark red";
                    }else if (arrayColor.contains(2) && arrayColor.contains(12)){
                        strColor = "black" + "," + "light blue sky";
                    }else if (arrayColor.contains(2) && arrayColor.contains(13)){
                        strColor = "black" + "," + "light red";
                    }else if (arrayColor.contains(2) && arrayColor.contains(14)){
                        strColor = "black" + "," + "light green";
                    }else if (arrayColor.contains(2) && arrayColor.contains(15)){
                        strColor = "black" + "," + "silver";
                    }else if (arrayColor.contains(2) && arrayColor.contains(16)){
                        strColor = "black" + "," + "green";
                    }else if (arrayColor.contains(2) && arrayColor.contains(17)){
                        strColor = "black" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(3) && arrayColor.contains(4)){
                        strColor = "red" + "," + "yellow";
                    }else if (arrayColor.contains(3) && arrayColor.contains(5)){
                        strColor = "red" + "," + "pink";
                    }else if (arrayColor.contains(3) && arrayColor.contains(6)){
                        strColor = "red" + "," + "purple";
                    }else if (arrayColor.contains(3) && arrayColor.contains(7)){
                        strColor = "red" + "," + "orange";
                    }else if (arrayColor.contains(3) && arrayColor.contains(8)){
                        strColor = "red" + "," + "green";
                    }else if (arrayColor.contains(3) && arrayColor.contains(9)){
                        strColor = "red" + "," + "silver";
                    }else if (arrayColor.contains(3) && arrayColor.contains(10)){
                        strColor = "red" + "," + "brown";
                    }else if (arrayColor.contains(3) && arrayColor.contains(11)){
                        strColor = "red" + "," + "blue sky";
                    }else if (arrayColor.contains(3) && arrayColor.contains(12)){
                        strColor = "red" + "," + "light green";
                    }else if (arrayColor.contains(3) && arrayColor.contains(13)){
                        strColor = "red" + "," + "light red";
                    }else if (arrayColor.contains(3) && arrayColor.contains(14)){
                        strColor = "red" + "," + "light blue sky";
                    }else if (arrayColor.contains(3) && arrayColor.contains(15)){
                        strColor = "red" + "," + "dark red";
                    }else if (arrayColor.contains(3) && arrayColor.contains(16)){
                        strColor = "red" + "," + "dark green";
                    }else if (arrayColor.contains(3) && arrayColor.contains(17)){
                        strColor = "red" + "," + "dark blue";
                    }
                    else if (arrayColor.contains(4) && arrayColor.contains(5)){
                        strColor = "yellow" + "," + "pink";
                    }else if (arrayColor.contains(4) && arrayColor.contains(6)){
                        strColor = "yellow" + "," + "purple";
                    }else if (arrayColor.contains(4) && arrayColor.contains(7)){
                        strColor = "yellow" + "," + "orange";
                    }else if (arrayColor.contains(4) && arrayColor.contains(8)){
                        strColor = "yellow" + "," + "dark blue";
                    }else if (arrayColor.contains(4) && arrayColor.contains(9)){
                        strColor = "yellow" + "," + "brown";
                    }else if (arrayColor.contains(4) && arrayColor.contains(10)){
                        strColor = "yellow" + "," + "dark green";
                    }else if (arrayColor.contains(4) && arrayColor.contains(11)){
                        strColor = "yellow" + "," + "dark red";
                    }else if (arrayColor.contains(4) && arrayColor.contains(12)){
                        strColor = "yellow" + "," + "light blue sky";
                    }else if (arrayColor.contains(4) && arrayColor.contains(13)){
                        strColor = "yellow" + "," + "light red";
                    }else if (arrayColor.contains(4) && arrayColor.contains(14)){
                        strColor = "yellow" + "," + "light green";
                    }else if (arrayColor.contains(4) && arrayColor.contains(15)){
                        strColor = "yellow" + "," + "silver";
                    }else if (arrayColor.contains(4) && arrayColor.contains(16)){
                        strColor = "yellow" + "," + "green";
                    }else if (arrayColor.contains(4) && arrayColor.contains(17)){
                        strColor = "yellow" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(5) && arrayColor.contains(6)){
                        strColor = "pink" + "," + "purple";
                    }else if (arrayColor.contains(5) && arrayColor.contains(7)) {
                        strColor = "pink" + "," + "orange";
                    }else if (arrayColor.contains(5) && arrayColor.contains(8)){
                        strColor = "pink" + "," + "dark blue";
                    }else if (arrayColor.contains(5) && arrayColor.contains(9)){
                        strColor = "pink" + "," + "brown";
                    }else if (arrayColor.contains(5) && arrayColor.contains(10)){
                        strColor = "pink" + "," + "dark green";
                    }else if (arrayColor.contains(5) && arrayColor.contains(11)){
                        strColor = "pink" + "," + "dark red";
                    }else if (arrayColor.contains(5) && arrayColor.contains(12)){
                        strColor = "pink" + "," + "light blue sky";
                    }else if (arrayColor.contains(5) && arrayColor.contains(13)){
                        strColor = "pink" + "," + "light red";
                    }else if (arrayColor.contains(5) && arrayColor.contains(14)){
                        strColor = "pink" + "," + "light green";
                    }else if (arrayColor.contains(5) && arrayColor.contains(15)){
                        strColor = "pink" + "," + "silver";
                    }else if (arrayColor.contains(5) && arrayColor.contains(16)){
                        strColor = "pink" + "," + "green";
                    }else if (arrayColor.contains(5) && arrayColor.contains(17)){
                        strColor = "pink" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(6) && arrayColor.contains(7)){
                        strColor = "purple" + "," + "orange";
                    }else if (arrayColor.contains(6) && arrayColor.contains(8)){
                        strColor = "purple" + "," + "dark blue";
                    }else if (arrayColor.contains(6) && arrayColor.contains(9)){
                        strColor = "purple" + "," + "brown";
                    }else if (arrayColor.contains(6) && arrayColor.contains(10)){
                        strColor = "purple" + "," + "dark green";
                    }else if (arrayColor.contains(6) && arrayColor.contains(11)){
                        strColor = "purple" + "," + "dark red";
                    }else if (arrayColor.contains(6) && arrayColor.contains(12)){
                        strColor = "purple" + "," + "light blue sky";
                    }else if (arrayColor.contains(6) && arrayColor.contains(13)){
                        strColor = "purple" + "," + "light red";
                    }else if (arrayColor.contains(6) && arrayColor.contains(14)){
                        strColor = "purple" + "," + "light green";
                    }else if (arrayColor.contains(6) && arrayColor.contains(15)){
                        strColor = "purple" + "," + "silver";
                    }else if (arrayColor.contains(6) && arrayColor.contains(16)){
                        strColor = "purple" + "," + "green";
                    }else if (arrayColor.contains(6) && arrayColor.contains(17)){
                        strColor = "purple" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(7) && arrayColor.contains(8)){
                        strColor = "orange" + "," + "dark blue";
                    }else if (arrayColor.contains(7) && arrayColor.contains(9)){
                        strColor = "orange" + "," + "brown";
                    }else if (arrayColor.contains(7) && arrayColor.contains(10)){
                        strColor = "orange" + "," + "dark green";
                    }else if (arrayColor.contains(7) && arrayColor.contains(11)){
                        strColor = "orange" + "," + "dark red";
                    }else if (arrayColor.contains(7) && arrayColor.contains(12)){
                        strColor = "orange" + "," + "light blue sky";
                    }else if (arrayColor.contains(7) && arrayColor.contains(13)){
                        strColor = "orange" + "," + "light red";
                    }else if (arrayColor.contains(7) && arrayColor.contains(14)){
                        strColor = "orange" + "," + "light green";
                    }else if (arrayColor.contains(7) && arrayColor.contains(15)){
                        strColor = "orange" + "," + "silver";
                    }else if (arrayColor.contains(7) && arrayColor.contains(16)){
                        strColor = "orange" + "," + "green";
                    }else if (arrayColor.contains(7) && arrayColor.contains(17)){
                        strColor = "orange" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(8) && arrayColor.contains(9)){
                        strColor = "dark blue" + "," + "brown";
                    }else if (arrayColor.contains(8) && arrayColor.contains(10)){
                        strColor = "dark blue" + "," + "dark green";
                    }else if (arrayColor.contains(8) && arrayColor.contains(11)){
                        strColor = "dark blue" + "," + "dark red";
                    }else if (arrayColor.contains(8) && arrayColor.contains(12)){
                        strColor = "dark blue" + "," + "light blue sky";
                    }else if (arrayColor.contains(8) && arrayColor.contains(13)){
                        strColor = "dark blue" + "," + "light red";
                    }else if (arrayColor.contains(8) && arrayColor.contains(14)){
                        strColor = "dark blue" + "," + "light green";
                    }else if (arrayColor.contains(8) && arrayColor.contains(15)){
                        strColor = "dark blue" + "," + "silver";
                    }else if (arrayColor.contains(8) && arrayColor.contains(16)){
                        strColor = "dark blue" + "," + "green";
                    }else if (arrayColor.contains(8) && arrayColor.contains(17)){
                        strColor = "dark blue" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(9) && arrayColor.contains(10)){
                        strColor = "brown" + "," + "dark green";
                    }else if (arrayColor.contains(9) && arrayColor.contains(11)){
                        strColor = "brown" + "," + "dark red";
                    }else if (arrayColor.contains(9) && arrayColor.contains(12)){
                        strColor = "brown" + "," + "light blue sky";
                    }else if (arrayColor.contains(9) && arrayColor.contains(13)){
                        strColor = "brown" + "," + "light red";
                    }else if (arrayColor.contains(9) && arrayColor.contains(14)){
                        strColor = "brown" + "," + "light green";
                    }else if (arrayColor.contains(9) && arrayColor.contains(15)){
                        strColor = "brown" + "," + "silver";
                    }else if (arrayColor.contains(9) && arrayColor.contains(16)){
                        strColor = "brown" + "," + "green";
                    }else if (arrayColor.contains(9) && arrayColor.contains(17)){
                        strColor = "brown" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(10) && arrayColor.contains(11)){
                        strColor = "dark green" + "," + "dark red";
                    }else if (arrayColor.contains(10) && arrayColor.contains(12)){
                        strColor = "dark green" + "," + "light blue sky";
                    }else if (arrayColor.contains(10) && arrayColor.contains(13)){
                        strColor = "dark green" + "," + "light red";
                    }else if (arrayColor.contains(10) && arrayColor.contains(14)){
                        strColor = "dark green" + "," + "light green";
                    }else if (arrayColor.contains(10) && arrayColor.contains(15)){
                        strColor = "dark green" + "," + "silver";
                    }else if (arrayColor.contains(10) && arrayColor.contains(16)){
                        strColor = "dark green" + "," + "green";
                    }else if (arrayColor.contains(10) && arrayColor.contains(17)){
                        strColor = "dark green" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(11) && arrayColor.contains(12)){
                        strColor = "dark red" + "," + "light blue sky";
                    }else if (arrayColor.contains(11) && arrayColor.contains(13)){
                        strColor = "dark red" + "," + "light red";
                    }else if (arrayColor.contains(11) && arrayColor.contains(14)){
                        strColor = "dark red" + "," + "light green";
                    }else if (arrayColor.contains(11) && arrayColor.contains(15)){
                        strColor = "dark red" + "," + "silver";
                    }else if (arrayColor.contains(11) && arrayColor.contains(16)){
                        strColor = "dark red" + "," + "green";
                    }else if (arrayColor.contains(11) && arrayColor.contains(17)){
                        strColor = "dark red" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(12) && arrayColor.contains(13)){
                        strColor = "light blue sky" + "," + "light red";
                    }else if (arrayColor.contains(12) && arrayColor.contains(14)){
                        strColor = "light blue sky" + "," + "light green";
                    }else if (arrayColor.contains(12) && arrayColor.contains(15)){
                        strColor = "light blue sky" + "," + "silver";
                    }else if (arrayColor.contains(12) && arrayColor.contains(16)){
                        strColor = "light blue sky" + "," + "green";
                    }else if (arrayColor.contains(12) && arrayColor.contains(17)){
                        strColor = "light blue sky" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(13) && arrayColor.contains(14)){
                        strColor = "light red" + "," + "light green";
                    }else if (arrayColor.contains(13) && arrayColor.contains(15)){
                        strColor = "light red" + "," + "silver";
                    }else if (arrayColor.contains(13) && arrayColor.contains(16)){
                        strColor = "light red" + "," + "green";
                    }else if (arrayColor.contains(13) && arrayColor.contains(17)){
                        strColor = "light red" + "," + "blue sky";
                    }
                   else if (arrayColor.contains(14) && arrayColor.contains(15)){
                        strColor = "light green" + "," + "silver";
                    }else if (arrayColor.contains(14) && arrayColor.contains(16)){
                        strColor = "light green" + "," + "green";
                    }else if (arrayColor.contains(14) && arrayColor.contains(17)){
                        strColor = "light green" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(15) && arrayColor.contains(16)){
                        strColor = "silver" + "," + "green";
                    }else if (arrayColor.contains(15) && arrayColor.contains(17)){
                        strColor = "silver" + "," + "blue sky";
                    }
                    else if (arrayColor.contains(16) && arrayColor.contains(17)){
                        strColor = "green" + "," + "blue sky";
                    }
                    break;
            }
            Log.e("color select:", "" + strColor);
        }
        return strColor;
    }
    public static ArrayList<Integer> getItemColor(String strColor){
        ArrayList<Integer> index = new ArrayList<>();
        if (strColor != null){
            switch (strColor) {
                case "black":
                    index.add(2);
                    break;
                case "blue":
                    index.add(1);
                    break;
                case "red":
                    index.add(3);
                    break;
                case "orange":
                    index.add(7);
                    break;
                case "white":
                    index.add(0);
                    break;
                case "yellow":
                    index.add(4);
                    break;
                case "pink":
                    index.add(5);
                    break;
                case "purple":
                    index.add(6);
                    break;
                case "dark blue":
                    index.add(8);
                    break;
                case "brown":
                    index.add(9);
                    break;
                case "dark green":
                    index.add(10);
                    break;
                case "dark red":
                    index.add(11);
                    break;
                case "light blue sky":
                    index.add(12);
                    break;
                case "light red":
                    index.add(13);
                    break;
                case "green":
                    index.add(16);
                    break;
                case "silver":
                    index.add(15);
                    break;
                case "light green":
                    index.add(14);
                    break;
                case "blue sky":
                    index.add(17);
                    break;

                case "white,black":
                    index.add(0);
                    index.add(2);
                    break;
                case "white,silver":
                    index.add(0);
                    index.add(15);
                    break;
                case "white,blue":
                    index.add(0);
                    index.add(1);
                    break;
                case "white,red":
                    index.add(0);
                    index.add(3);
                    break;
                case "white,orange":
                    index.add(0);
                    index.add(7);
                    break;
                case "white,yellow":
                    index.add(0);
                    index.add(4);
                    break;
                case "white,pink":
                    index.add(0);
                    index.add(5);
                    break;
                case "white,purple":
                    index.add(0);
                    index.add(6);
                    break;
                case "white,dark blue":
                    index.add(0);
                    index.add(8);
                    break;
                case "white,brown":
                    index.add(0);
                    index.add(9);
                    break;
                case "white,dark green":
                    index.add(0);
                    index.add(10);
                    break;
                case "white,dark red":
                    index.add(0);
                    index.add(11);
                    break;
                case "white,light blue sky":
                    index.add(0);
                    index.add(12);
                    break;
                case "white,light red":
                    index.add(0);
                    index.add(13);
                    break;
                case "white,green":
                    index.add(0);
                    index.add(16);
                    break;
                case "white,light green":
                    index.add(0);
                    index.add(14);
                    break;

                case "black,silver":
                    index.add(2);
                    index.add(5);
                    break;
                case "black,blue":
                    index.add(2);
                    index.add(1);
                    break;
                case "black,red":
                    index.add(2);
                    index.add(3);
                    break;
                case "black,orange":
                    index.add(2);
                    index.add(7);
                    break;
                case "black,yellow":
                    index.add(2);
                    index.add(4);
                    break;
                case "black,pink":
                    index.add(2);
                    index.add(5);
                    break;
                case "black,purple":
                    index.add(2);
                    index.add(6);
                    break;
                case "black,dark blue":
                    index.add(2);
                    index.add(8);
                    break;
                case "black,brown":
                    index.add(2);
                    index.add(9);
                    break;
                case "black,dark green":
                    index.add(2);
                    index.add(10);
                    break;
                case "black,dark red":
                    index.add(2);
                    index.add(11);
                    break;
                case "black,light blue sky":
                    index.add(2);
                    index.add(12);
                    break;
                case "black,light red":
                    index.add(2);
                    index.add(13);
                    break;
                case "black,green":
                    index.add(2);
                    index.add(16);
                    break;
                case "black,light green":
                    index.add(2);
                    index.add(14);
                    break;

                case "blue,silver":
                    index.add(1);
                    index.add(15);
                    break;
                case "blue,red":
                    index.add(1);
                    index.add(3);
                    break;
                case "blue,orange":
                    index.add(1);
                    index.add(7);
                    break;
                case "blue,yellow":
                    index.add(1);
                    index.add(4);
                    break;
                case "blue,pink":
                    index.add(1);
                    index.add(5);
                    break;
                case "blue,purple":
                    index.add(1);
                    index.add(6);
                    break;
                case "blue,dark blue":
                    index.add(1);
                    index.add(8);
                    break;
                case "blue,brown":
                    index.add(1);
                    index.add(9);
                    break;
                case "blue,dark green":
                    index.add(1);
                    index.add(10);
                    break;
                case "blue,dark red":
                    index.add(1);
                    index.add(11);
                    break;
                case "blue,light blue sky":
                    index.add(1);
                    index.add(12);
                    break;
                case "blue,light red":
                    index.add(1);
                    index.add(13);
                    break;
                case "blue,green":
                    index.add(1);
                    index.add(16);
                    break;
                case "blue,light green":
                    index.add(1);
                    index.add(14);
                    break;

                case "red,silver":
                    index.add(3);
                    index.add(15);
                    break;
                case "red,orange":
                    index.add(3);
                    index.add(7);
                    break;
                case "red,yellow":
                    index.add(3);
                    index.add(4);
                    break;
                case "red,pink":
                    index.add(3);
                    index.add(5);
                    break;
                case "red,purple":
                    index.add(3);
                    index.add(6);
                    break;
                case "red,dark blue":
                    index.add(3);
                    index.add(7);
                    break;
                case "red,brown":
                    index.add(3);
                    index.add(8);
                    break;
                case "red,dark green":
                    index.add(3);
                    index.add(9);
                    break;
                case "red,dark red":
                    index.add(3);
                    index.add(10);
                    break;
                case "red,light blue sky":
                    index.add(3);
                    index.add(11);
                    break;
                case "red,light red":
                    index.add(3);
                    index.add(12);
                    break;
                case "red,green":
                    index.add(3);
                    index.add(16);
                    break;
                case "red,light green":
                    index.add(3);
                    index.add(13);
                    break;

                case "yellow,silver":
                    index.add(4);
                    index.add(15);
                    break;
                case "yellow,orange":
                    index.add(4);
                    index.add(7);
                    break;
                case "yellow,pink":
                    index.add(4);
                    index.add(5);
                    break;
                case "yellow,purple":
                    index.add(4);
                    index.add(6);
                    break;
                case "yellow,dark blue":
                    index.add(4);
                    index.add(8);
                    break;
                case "yellow,brown":
                    index.add(4);
                    index.add(9);
                    break;
                case "yellow,dark green":
                    index.add(4);
                    index.add(10);
                    break;
                case "yellow,dark red":
                    index.add(4);
                    index.add(11);
                    break;
                case "yellow,light blue sky":
                    index.add(4);
                    index.add(12);
                    break;
                case "yellow,light red":
                    index.add(4);
                    index.add(13);
                    break;
                case "yellow,green":
                    index.add(4);
                    index.add(16);
                    break;
                case "yellow,light green":
                    index.add(4);
                    index.add(14);
                    break;

                case "pink,silver":
                    index.add(5);
                    index.add(15);
                    break;
                case "pink,orange":
                    index.add(5);
                    index.add(7);
                    break;
                case "pink,purple":
                    index.add(5);
                    index.add(6);
                    break;
                case "pink,dark blue":
                    index.add(5);
                    index.add(8);
                    break;
                case "pink,brown":
                    index.add(5);
                    index.add(9);
                    break;
                case "pink,dark green":
                    index.add(5);
                    index.add(10);
                    break;
                case "pink,dark red":
                    index.add(5);
                    index.add(11);
                    break;
                case "pink,light blue sky":
                    index.add(5);
                    index.add(12);
                    break;
                case "pink,light red":
                    index.add(5);
                    index.add(13);
                    break;
                case "pink,green":
                    index.add(5);
                    index.add(16);
                    break;
                case "pink,light green":
                    index.add(5);
                    index.add(14);
                    break;

                case "purple,silver":
                    index.add(6);
                    index.add(15);
                    break;
                case "purple,orange":
                    index.add(6);
                    index.add(7);
                    break;
                case "purple,dark blue":
                    index.add(6);
                    index.add(8);
                    break;
                case "purple,brown":
                    index.add(6);
                    index.add(9);
                    break;
                case "purple,dark green":
                    index.add(6);
                    index.add(10);
                    break;
                case "purple,dark red":
                    index.add(6);
                    index.add(11);
                    break;
                case "purple,light blue sky":
                    index.add(6);
                    index.add(12);
                    break;
                case "purple,light red":
                    index.add(6);
                    index.add(13);
                    break;
                case "purple,green":
                    index.add(6);
                    index.add(16);
                    break;
                case "purple,light green":
                    index.add(6);
                    index.add(14);
                    break;

                case "orange,silver":
                    index.add(7);
                    index.add(15);
                    break;
                case "orange,dark blue":
                    index.add(7);
                    index.add(8);
                    break;
                case "orange,brown":
                    index.add(7);
                    index.add(9);
                    break;
                case "orange,dark green":
                    index.add(7);
                    index.add(10);
                    break;
                case "orange,dark red":
                    index.add(7);
                    index.add(11);
                    break;
                case "orange,light blue sky":
                    index.add(7);
                    index.add(12);
                    break;
                case "orange,light red":
                    index.add(7);
                    index.add(13);
                    break;
                case "orange,green":
                    index.add(7);
                    index.add(16);
                    break;
                case "orange,light green":
                    index.add(7);
                    index.add(14);
                    break;

                case "green,silver":
                    index.add(16);
                    index.add(15);
                    break;
                case "green,dark blue":
                    index.add(16);
                    index.add(8);
                    break;
                case "green,brown":
                    index.add(16);
                    index.add(9);
                    break;
                case "green,dark green":
                    index.add(16);
                    index.add(10);
                    break;
                case "green,dark red":
                    index.add(16);
                    index.add(11);
                    break;
                case "green,light blue sky":
                    index.add(16);
                    index.add(12);
                    break;
                case "green,light red":
                    index.add(16);
                    index.add(13);
                    break;
                case "green,light green":
                    index.add(16);
                    index.add(14);
                    break;

                case "silver,dark blue":
                    index.add(15);
                    index.add(8);
                    break;
                case "silver,brown":
                    index.add(15);
                    index.add(9);
                    break;
                case "silver,dark green":
                    index.add(15);
                    index.add(10);
                    break;
                case "silver,dark red":
                    index.add(15);
                    index.add(11);
                    break;
                case "silver,light blue sky":
                    index.add(15);
                    index.add(12);
                    break;
                case "silver,light red":
                    index.add(15);
                    index.add(13);
                    break;
                case "silver,light green":
                    index.add(15);
                    index.add(14);
                    break;

                case "brown,dark blue":
                    index.add(9);
                    index.add(8);
                    break;
                case "brown,dark green":
                    index.add(9);
                    index.add(10);
                    break;
                case "brown,dark red":
                    index.add(9);
                    index.add(11);
                    break;
                case "brown,light blue sky":
                    index.add(9);
                    index.add(12);
                    break;
                case "brown,light red":
                    index.add(9);
                    index.add(13);
                    break;
                case "brown,light green":
                    index.add(9);
                    index.add(14);
                    break;

                case "blue sky,dark blue":
                    index.add(17);
                    index.add(8);
                    break;
                case "blue sky,dark green":
                    index.add(17);
                    index.add(10);
                    break;
                case "blue sky,dark red":
                    index.add(17);
                    index.add(11);
                    break;
                case "blue sky,light blue sky":
                    index.add(17);
                    index.add(12);
                    break;
                case "blue sky,light red":
                    index.add(17);
                    index.add(13);
                    break;
                case "blue sky,light green":
                    index.add(17);
                    index.add(14);
                    break;

                case "light green,dark blue":
                    index.add(14);
                    index.add(8);
                    break;
                case "light green,dark green":
                    index.add(14);
                    index.add(9);
                    break;
                case "light green,dark red":
                    index.add(14);
                    index.add(10);
                    break;
                case "light green,light blue sky":
                    index.add(14);
                    index.add(11);
                    break;
                case "light green,light red":
                    index.add(14);
                    index.add(13);
                    break;

                case "light red,dark blue":
                    index.add(13);
                    index.add(8);
                    break;
                case "light red,dark green":
                    index.add(13);
                    index.add(10);
                    break;
                case "light red,dark red":
                    index.add(13);
                    index.add(11);
                    break;
                case "light red,light blue sky":
                    index.add(13);
                    index.add(12);
                    break;

                case "light blue sky,dark blue":
                    index.add(12);
                    index.add(8);
                    break;
                case "light blue sky,dark green":
                    index.add(12);
                    index.add(10);
                    break;
                case "light blue sky,dark red":
                    index.add(12);
                    index.add(11);
                    break;

                case "dark red,dark green":
                    index.add(11);
                    index.add(10);
                    break;
                case "dark red,dark blue":
                    index.add(11);
                    index.add(8);
                    break;

                case "dark green,dark blue":
                    index.add(10);
                    index.add(8);
                    break;
            }
        }
        Log.e("select color",""+index);
        return index;
    }
}
