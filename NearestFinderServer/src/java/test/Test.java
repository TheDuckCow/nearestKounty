/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author fcarterwheatley
 */
public class Test {
    public static void main(String[] args) {
	System.out.print("Hello World"); 
    }
    
    public static String getLocationsInBound() {
        return "<here are locations in bound>";
    }
    
    public static String isWithinBound(int x, int y){
        return "Yes, that is within the bound";
    }
    
    public static String getLocationsAtCounty(){
        return "<here are locations at county>";
    }
    
    public static String getNearestKLocationsAtCoord(){
        return "<here are the nearest k locations at coord>";
    }
    
    public static String getNearestKLocationsAtCounty(){
        return "<here are the nearest k locations at county>"; 
    }
}
