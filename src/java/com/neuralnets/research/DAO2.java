/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.research;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *
 * @author tmwamalwa
 */
public class DAO2 {
        private int maxCounter;
    private String[] valuesRow;
     private double normolizer = 100000.0D;
    private double minlevel = 0.0D;
    private TrainingSet trainingSet = new TrainingSet();
    public String[] getValuesRow() {
        return valuesRow;
    }

    public void setValuesRow(String[] valuesRow) {
        this.valuesRow = valuesRow;
    }

    public int getMaxCounter() {
        return maxCounter;
    }

    public void setMaxCounter(int maxCounter) {
        this.maxCounter = maxCounter;
    }
    //percentage of training to be done
    public DAO2() {
        //this.setMaxCounter(145);
    }

    public DAO2(int maxCounter) {
        this.setMaxCounter(maxCounter);
    }
  
  
    public TrainingSet readNRBData() {
        
        String riceprice = "";
        String wheatprice="";
        String maizeprice="";
        String maizeproduction="";
        String rainfall="";
        String inflation ="";
        int rowcount = 0;
        HashMap hm = new HashMap();
       Statement stmt = null; 
        
        PreparedStatement preparedstatement = null;
        String values="";
        String id = "";
        Connection connection = null;
        
        int counter = 0;
        try {
          
             connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="SELECT ID,MAIZEPRICE,INFLATION,MAIZEPRODUCTION,RAINFALL,RICEPRICE,WHEATPRICE FROM RESEARCH ORDER BY 1 DESC";
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
           
           
            
            ResultSet result = preparedstatement.executeQuery();
            
            if (result.last()) {
            rowcount = result.getRow();
            result.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            int maxCount = (int)(70*rowcount); 
           System.out.println("full number of values = " + rowcount + " Number of training data"+ maxCount/100); 
            setMaxCounter(maxCount/100);
             for(int i =0; i<maxCount; i++){
               while(result.next()){
                riceprice=result.getString("RICEPRICE");
                inflation=result.getString("INFLATION");
                rainfall=result.getString("RAINFALL");
                maizeproduction=result.getString("MAIZEPRODUCTION");
                wheatprice=result.getString("WHEATPRICE");
                maizeprice=result.getString("MAIZEPRICE");
                id = String.valueOf(result.getInt("ID"));
                //for(int a=0; a<31;a ++){
               
                     double d1 = (Double.parseDouble(riceprice) - minlevel) / normolizer;
               //System.out.println("NORMALIZED "+d1 *normolizer + " REAL "+ riceprice);
                double d2 = (Double.parseDouble(wheatprice) - minlevel) / normolizer;
                double d3 = (Double.parseDouble(maizeprice) - minlevel) / normolizer;
                double d4 = (Double.parseDouble(inflation) - minlevel) / 100;
                double d5 = (Double.parseDouble(rainfall) - minlevel) / 1000;
                double d6 = (Double.parseDouble(maizeproduction) - minlevel) / normolizer;
               
                //System.out.println(i + " " + d1 + " " + d2 + " " + d3 + " " + d4 + " ->" + d5);
                //for(int k =0; k<6306; k++){
                trainingSet.addElement(new SupervisedTrainingElement(new double[]{d1,d2,d4,d5,d6}, new double[]{d3}));  
                      
               }   
                  } 
               
            
            
             connection.close();
        } catch (Exception ioe) {
            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);
        }
        

         return trainingSet;
    }

    public TrainingSet getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(TrainingSet trainingSet) {
        this.trainingSet = trainingSet;
    }
    

    public DAO2(String[] valuesRow) {
        this.setValuesRow(valuesRow);
    }

   
    
}
