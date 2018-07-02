/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.research;

import com.neuralnets.entities.Modelsettings;
import com.neuralnets.entities.Nairobi;
import com.neuralnets.entities.Research;
import com.neuralnets.entities.Users;
import com.neuralnets.facades.ModelsettingsFacade;
import com.neuralnets.facades.NairobiFacade;
import com.neuralnets.facades.ResearchFacade;
import com.neuralnets.facades.UsersFacade;
import java.io.FileInputStream;
import java.io.InputStream;
import static java.lang.Math.sqrt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.TransferFunctionType;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author tmwamalwa
 */
@ManagedBean
@SessionScoped
public class UsersController {
     private Users authUser = new Users();
     private Users loadUser = new Users();
     private Research data = new Research();
     private Modelsettings model = new Modelsettings();
      private Modelsettings loadModel = new Modelsettings();
      private double minlevel = 0.0D;
      private List output = new ArrayList();
     RequestContext rtx;
     private LineChartModel lineModel;
     ExternalContext ext;
     private String selecteddata;
     private DataModel<Users> usersDM = new ListDataModel<>();
     private DataModel<Modelsettings> modelDM = new ListDataModel<>();
     private List<Predictions> predsDM = new ArrayList<>();
     private List<Research> researchDM = new ArrayList<>();
     private List<Nairobi> nrbDM = new ArrayList<>();
     //private List<Research> nrbDM = new ArrayList<>();
     HttpServletRequest request;
      private int maxCounter;
     private String[] valuesRow;
     private String totalnetworkError;
     private String madresult;
     private String rmse;
     private String renedered;
     private String rendergraph;
     private Date month;
     private String maperesult;
     private String selectedregion;
     private String predictedvalue;
     private String showpredictedvalue;
     private String selectedmode;
     private String unirenedered;
     private String multirendered;
     private double normolizer2 = 100000.0D;
    private double normolizer3 = 10000000.0D;
     private UploadedFile excelFile;
     private static DecimalFormat df2 = new DecimalFormat(".######");
      private static DecimalFormat df3 = new DecimalFormat(".##");
      FacesContext ctx;
      @EJB
    private UsersFacade usersFacade = new UsersFacade();
      
    @EJB
    private ModelsettingsFacade modelsettingsFacade = new ModelsettingsFacade();
    
    @EJB
    private ResearchFacade dataFacade = new ResearchFacade();
    
    @EJB
    private NairobiFacade nrbFacade = new NairobiFacade();
     public String login(){
       
         ctx = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        
       
        String redirectPage = "";
        Date date = new Date();
        
        System.out.println("Getting username ############"+authUser.getUsername());
        
        //Call LDAP
        try{
       //if (ADAuthenticator(authUser.getUsername(),authUser.getPassword()) == 1) {
            
           
            //Check if user is locked
            
            if(usersFacade.fetchUserByUsername
        (authUser.getUsername())==null || authUser.getUsername().isEmpty() ||authUser.getUsername().equals("")){
                System.out.println("User Result ###### "+usersFacade.fetchUserByUsername
        (authUser.getUsername()));
                 ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "User not found. Contact Admin", ""));

                    redirectPage = "index.nnet";
            }else{
           
         
           
            
                 if(usersFacade.fetchUserByUsername
        (authUser.getUsername()).getUsername()==null || usersFacade.fetchUserByUsername
        (authUser.getUsername()).getUsername().isEmpty()){
                System.out.println("No user was found");
                
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Account Inactive. Contact Admin", ""));
              redirectPage = "index.nnet";
           
            
                
             

                    //redirectPage = "index.xhtml";
            }else{
                //check roles
                System.out.println("Success redirection ");
                redirectPage = "dashboard.nnet";
                     setRenedered("false");
                     setRendergraph("false");
                     setShowpredictedvalue("false");
            
                 }
            } 
              // }else {
                    
                  //  ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    //        "Username not found in AD", ""));

                    //redirectPage = "index.xhtml";
                //}
        }catch(Exception ex){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            "Unknown error occured", ""));

                    redirectPage = "index.nnet"; 
        }
        
        return redirectPage;
     }
     
      public String logout(){
        String page = "";
         ctx = FacesContext.getCurrentInstance();
        //session = (HttpSession) ctx.getExternalContext().getSession(false);
       // String sessionId = session.getId();
        //System.out.println("Session Id logout " + sessionId);
             page = "/index.nnet?faces-redirect=true";
             Date date = new Date();
         try {
             
            
           
              FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
             
         } catch (Exception ex) {
             System.out.println("Error occured while loggig out "+ ex.getMessage());
         }
         return page;
    }
     
      public void  createConfigs(){
         Date date = new Date();
         try{
             ctx =FacesContext.getCurrentInstance();
             
             
             modelsettingsFacade.create(model);
             model = new Modelsettings();
             ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Data submitted successfully", ""));  
             
         }catch(Exception ex){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "An error occured", ""));  
         }
         
     }

    public DataModel<Modelsettings> getModelDM() {
        modelDM = new ListDataModel<>(modelsettingsFacade.findAll());
        return modelDM;
    }

    public void setModelDM(DataModel<Modelsettings> modelDM) {
        this.modelDM = modelDM;
    }
      
     
    
    public String loadDetails(){
        String page = "";
        
        
        
        page ="editConfigs.nnet";
        loadModel = modelDM.getRowData();
        
        
      
           
       
        return page;
    }
      
    
   public String editConfigs(){
        System.out.println("Editing configs");
        ctx = FacesContext.getCurrentInstance();
        
        String returnPage="";
        Date date = new Date();
       
        returnPage = "configs.nnet";
        try{
            
          
            modelsettingsFacade.edit(loadModel);
            
           
        
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Settings edited successsfully", ""));
        }
        catch(Exception ex){
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "An error occured", ""));
        }
        
        return returnPage;
    }
   
    public String Train(){
           ctx = FacesContext.getCurrentInstance();
           rtx = RequestContext.getCurrentInstance();
         String page = "train.nnet";
         String networsavedstatename = "";
         
         
         try{
            
             if(selecteddata.equals("0")){
               rtx.execute("PF('dlg3').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Please select region", ""));   
             }else{
                 
                 if(selecteddata.equals("NAIROBI")){
                     networsavedstatename = "nrbPerceptron.nnet";
                 }else if(selecteddata.equals("ELDORET")){
                    networsavedstatename = "eldPerceptron.nnet"; 
                 }else{
                    networsavedstatename = "ksmPerceptron.nnet"; 
                 }
                 loadModel = getModelDM().getRowData();
                 System.out.println("This is the Max error "+ loadModel.getMaxiterations());
                 double normolizer = loadModel.getNormalizer(); 
                  DAO dao = new DAO();
                  dao.readNRBData(selecteddata, normolizer,
                          loadModel.getTrainingdata());
                  
                   int maxIterations = loadModel.getMaxiterations();
                   
                    NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,4, loadModel.getNeurons(), 1);
        ((LMS) neuralNet.getLearningRule()).setMaxError(loadModel.getMaxerror());//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(loadModel.getLearningrate());//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        //TrainingSet trainingSet = new TrainingSet();
        TrainingSet trainingSet = dao.getTrainingSet();
        
         BackPropagation backPropagation = new BackPropagation();
                 backPropagation.setMaxIterations(maxIterations);
                 
                 
           neuralNet.learnInSameThread(trainingSet);
          
           
        System.out.println("Neural Total network Error " + ((LMS)neuralNet.getLearningRule()).getTotalNetworkError());
        double networkError = ((LMS)neuralNet.getLearningRule()).getTotalNetworkError();
                 setTotalnetworkError(String.valueOf(df2.format(networkError)));
        //System.out.println("Neural Total network Error " + neuralNet.getLearningRule());
        // save trained neural network
          neuralNet.save(networsavedstatename);
          // load saved neural network
         NeuralNetwork loadedMlPerceptron = NeuralNetwork.load(networsavedstatename);
         HashMap hm = new HashMap();
         Statement stmt = null; 
        
        PreparedStatement preparedstatement = null;
        String values="";
        String id = "";
        Connection connection = null;
        
        int counter = 0;
        
        String date = "";
        
         try {
          
             connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="SELECT ID,DATE,DATA FROM "+selecteddata +" ORDER BY ID DESC";
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
           
           String name = "\"1-02\"";
            
            ResultSet result = preparedstatement.executeQuery();
            while(result.next()){
               
                values=result.getString("DATA");
                id = String.valueOf(result.getInt("ID"));
                date = result.getString("DATE");
                System.out.println("\""+date +"\","+ values);
                hm.put(id, values);
               counter = counter + 1;
            }
            
             connection.close();
        } catch (Exception ioe) {
            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);
        }
        int maxCount = (int)(loadModel.getTestingdata()*counter); 
        System.out.println("full number of values = " + counter + " Percentage "+ maxCount/100); 
        setMaxCounter(maxCount/100);
        Set s = hm.keySet();
        Iterator i = s.iterator();
        valuesRow = new String[this.getMaxCounter()];
        
        int n = 0;
        while (i.hasNext()) {
            String key = (String) i.next();
            String value = (String) hm.get(key);
            //System.out.println(key + "->" + value);
            n = n + 1;
            //configurable percentage of
            if (counter - n < this.getMaxCounter()) {
                valuesRow[counter - n] = value;
                //System.out.println("Get values "+ value);
              // System.out.println(valuesRow[counter - n] );
            }
        }
        System.out.println("valuesRow.length=" + valuesRow.length);
         if (valuesRow.length < 5) {
            System.out.println("valuesRow.length < 5");
            
        }
         double error = 0.0;
         double rmse = 0.0;
         double sqrtrmse=0.0;
         double mape = 0.0;
         double mad =0.0;
         TrainingSet testSet = new TrainingSet();
         
        
         for (int j = 0; j + 4 < valuesRow.length; j++) {
                String s1 = valuesRow[j];
                String s2 = valuesRow[j + 1];
                String s3 = valuesRow[j + 2];
                String s4 = valuesRow[j + 3];
                String s5 = valuesRow[j + 4];
                double d1 = (Double.parseDouble(s1) - minlevel) / normolizer;
                //System.out.println("D1 "+d1 *normolizer + " S1 "+ s1);
                double d2 = (Double.parseDouble(s2) - minlevel) / normolizer;
                double d3 = (Double.parseDouble(s3) - minlevel) / normolizer;
                double d4 = (Double.parseDouble(s4) - minlevel) / normolizer;
                double d5 = (Double.parseDouble(s5) - minlevel) / normolizer;
               // System.out.print( "Actual"  + df2.format((d5*normolizer)));
                 testSet.addElement(new TrainingElement(new double[]{d1}));
                 loadedMlPerceptron.setInput(d1,d2,d3,d4);
                  loadedMlPerceptron.calculate();
               
                 //System.out.print(" Predicted "+ df2.format((loadedMlPerceptron.getOutput().firstElement())*normolizer));
                  //error =((loadedMlPerceptron.getOutput().firstElement())- (d5*normolizer)*normolizer);
                  error = ((loadedMlPerceptron.getOutput().firstElement())*normolizer)-(d5*normolizer);
                 //System.out.print(" Error "+ df2.format(error));
                 error = Double.parseDouble(df2.format(error));
                 double actual = Double.parseDouble(df2.format(d5*normolizer));
                 rmse+= (error*error);
                 mad +=Math.abs(error);
                  mape+=Math.abs(error/actual) *100;
                  //System.out.println(" MAPE "+ Math.abs(error/actual) *100);
                   System.out.println("Actual"  + df2.format((d5*normolizer))+" Predicted "+ 
                           df2.format((loadedMlPerceptron.getOutput()
                                   .firstElement())*normolizer)+" Error "+ 
                           df2.format(error)+" MAPE "+ Math.abs(error/actual) *100);
                 // double mad2 = (error/actual)*100;
                // List output = null;
                 //call function to add errors to list
                 results(df2.format((d5*normolizer)),
                         df2.format((loadedMlPerceptron.getOutput()
                                 .firstElement())*normolizer),
                         String.valueOf(error),String.valueOf((error/actual)*100));
            }
              setRenedered("true");
             sqrtrmse=sqrt((rmse/valuesRow.length));
              System.out.println(" Total RMSE  "+ df2.format(sqrtrmse));
                 setRmse(String.valueOf(df2.format(sqrtrmse)));
              mad = (mad/valuesRow.length);
                 setMadresult(String.valueOf(df2.format(mad)));
              mape = (mape/valuesRow.length); 
              setMaperesult(String.valueOf(df2.format(mape)));
              System.out.println(" MAD  "+ df2.format(mad));
              System.out.println(" MAPE  "+ df2.format(mape));
               setPredsDM(output);
               
               System.out.println("Final list count == > "+ output.size()); 
             rtx.execute("PF('dlg3').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Training was successful", ""));  
             }
             
         }catch(Exception e){
              rtx.execute("PF('dlg3').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            "An error occured, please try again", ""));
         }
         
         return page;
    }
    
    public String TrainCountrywide(){
           ctx = FacesContext.getCurrentInstance();
           rtx = RequestContext.getCurrentInstance();
         String page = "countrywide.nnet";
         String networsavedstatename = "";
         
         if(data.getPolitics().equals("1")){
             networsavedstatename="kenyaPerceptron1.nnet";
         }else if(data.getPolitics().equals("2")){
           networsavedstatename="kenyaPerceptron2.nnet";  
         }else{
           networsavedstatename="kenyaPerceptron3.nnet";  
         }
         
         
         try{
            
            
                 loadModel = getModelDM().getRowData();
                 System.out.println("This is the Max error "+ loadModel.getMaxiterations());
                 double normolizer = loadModel.getNormalizer(); 
                  DAO2 dao = new DAO2();
                  dao.readNRBData(data.getPolitics());
                  
                   int maxIterations = loadModel.getMaxiterations();
                   
                    NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,8,loadModel.getNeurons(), 1);
        ((LMS) neuralNet.getLearningRule()).setMaxError(loadModel.getMaxerror());//0-1
        ((LMS) neuralNet.getLearningRule()).setLearningRate(loadModel.getLearningrate());//0-1
        ((LMS) neuralNet.getLearningRule()).setMaxIterations(maxIterations);//0-1
        //TrainingSet trainingSet = new TrainingSet();
        TrainingSet trainingSet = dao.getTrainingSet();
        
         BackPropagation backPropagation = new BackPropagation();
                 backPropagation.setMaxIterations(maxIterations);
                 
                 
           neuralNet.learnInSameThread(trainingSet, backPropagation);
          
           
        System.out.println("Neural Total network Error " + ((LMS)neuralNet.getLearningRule()).getTotalNetworkError());
        double networkError = ((LMS)neuralNet.getLearningRule()).getTotalNetworkError();
                 setTotalnetworkError(String.valueOf(df2.format(networkError)));
        //System.out.println("Neural Total network Error " + neuralNet.getLearningRule());
        // save trained neural network
          neuralNet.save(networsavedstatename);
          // load saved neural network
         NeuralNetwork loadedMlPerceptron = NeuralNetwork.load(networsavedstatename);
         HashMap hm = new HashMap();
         Statement stmt = null; 
        
        PreparedStatement preparedstatement = null;
        String values="";
        String id = "";
        Connection connection = null;
        
        int counter = 0;
        int rowcount = 0;
        String riceprice = "";
        String wheatprice="";
        String maizeprice="";
        String maizeproduction="";
        String rainfall="";
        String inflation ="";
         String beanprice="";
        String maizeimport = "";
        String politics = "";
        List rmses = new ArrayList();
         double sqrtrmse=0.0;
         double error = 0.0;
         double mape = 0.0;
         double mape1 = 0.0;
         double rmse = 0.0;
         double mad  =0.0;
         List mapes = new ArrayList();
        int maxCount = 0;
         try {
          
             connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="SELECT ID,MAIZEPRICE,INFLATION,MAIZEPRODUCTION,RAINFALL,RICEPRICE,WHEATPRICE,MAIZEIMPORT,BEANPRICE,POLITICS FROM RESEARCH ORDER BY 1 DESC";
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
           
           
            
            ResultSet result = preparedstatement.executeQuery();
            
            if (result.last()) {
            rowcount = result.getRow();
            result.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            maxCount = (int)(loadModel.getTestingdata()*rowcount); 
            System.out.println("full number of values = " + rowcount + " Percentage "+ maxCount/100); 
            counter = maxCount/100;
            //counter = rowcount;
           setMaxCounter(maxCount/100);
            for(int i =0; i<counter; i++){
               if(result.next()){
                riceprice=result.getString("RICEPRICE");
                inflation=result.getString("INFLATION");
                rainfall=result.getString("RAINFALL");
                maizeproduction=result.getString("MAIZEPRODUCTION");
                wheatprice=result.getString("WHEATPRICE");
                maizeprice=result.getString("MAIZEPRICE");
                id = String.valueOf(result.getInt("ID"));
                maizeimport=result.getString("MAIZEIMPORT");
                politics=result.getString("POLITICS");
                beanprice=result.getString("BEANPRICE");
                //for(int a=0; a<31;a ++){
               
                    double d1 = ((Double.parseDouble(riceprice) - minlevel)*0.001) / normolizer;
               //System.out.println("NORMALIZED "+d1 *normolizer + " REAL "+ riceprice);
                double d2 = ((Double.parseDouble(wheatprice) - minlevel)*0.001) / normolizer;
                double d3 = ((Double.parseDouble(maizeprice) - minlevel)*0.001) / normolizer;
                double d4 = (Double.parseDouble(inflation) - minlevel) / 100;
                double d5 = (Double.parseDouble(rainfall) - minlevel) / 1000;
                double d6 = (Double.parseDouble(maizeproduction) - minlevel) / normolizer2;
                double d7 = (Double.parseDouble(beanprice) - minlevel) / 100;
                double d8 = (Double.parseDouble(data.getPolitics()) - minlevel);
                double d9 = (Double.parseDouble(maizeimport) - minlevel) / normolizer3;
               
                  System.out.print( "Actual"  + df2.format((d3*normolizer)));
                 //testSet.addElement(new TrainingElement(new double[]{d1}));
                 loadedMlPerceptron.setInput(d1,d2,d4,d5,d6,d7,d8,d9);
                  loadedMlPerceptron.calculate();
                  System.out.print(" Predicted "+ df2.format((loadedMlPerceptron.getOutput().firstElement())*normolizer));
                  error = ((loadedMlPerceptron.getOutput().firstElement())*normolizer)-(d3*normolizer);
                  System.out.print(" Error "+ df2.format(error));
                 /*updateCountryWide(df2.format((d3*normolizer)/0.001),
                         df3.format((loadedMlPerceptron.getOutput().firstElement())*normolizer),df3.format(d3*normolizer));*/
                  //error =((loadedMlPerceptron.getOutput().firstElement())- (d5*normolizer)*normolizer);
                  //error = ((loadedMlPerceptron.getOutput().firstElement())*100000)-(d3*100000);
                 System.out.println(" Error "+ df2.format(error));
                 mad +=Math.abs(error);
                 error = Double.parseDouble(df2.format(error));
                 double actual = Double.parseDouble(df2.format(d3*normolizer));
                 mape1+=Math.abs(error/actual) *100;
                 //rmse += (error*error);
                 rmses.add((error*error));
                 mapes.add(Math.abs(error/actual) *100);
                // List output = null;
                 //call function to add errors to list
                 results(df2.format((d3*normolizer)),
                         df2.format((loadedMlPerceptron.getOutput()
                                 .firstElement())*normolizer),df2.format(error),String.valueOf(mape));
                      
               }   
            }
            
             connection.close();
        } catch (Exception ioe) {
            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);
        }
      
              setRenedered("true");
            
                 
                 
                 for(int i =0; i<rmses.size(); i++){
                  //System.out.println("RMSE VALUES "+ rmses.get(i));
                rmse+=Float.parseFloat(rmses.get(i).toString()); 
              }
              for(int i =0; i<mapes.size(); i++){
                 
                mape+=Double.parseDouble(mapes.get(i).toString()); 
                
              }
              //System.out.println("MAPE SUM "+ mape); 
             System.out.println(" Total RMSE COUNT  "+ rmses.size());
             sqrtrmse=sqrt((rmse/rmses.size()));
              System.out.println(" Total RMSE  "+ df2.format(sqrtrmse));
              
              mape = (mape/counter);
              //System.out.println("Count "+ counter);
              setMadresult(String.valueOf(df2.format(mad/counter)));
              setMaperesult(String.valueOf(df2.format(mape)));
              setRmse(df2.format(sqrtrmse));
              System.out.println("Network  MAPE  "+ df2.format(mape));
              System.out.println("Network  MAD  "+ df2.format(mad/counter));
                 
             
               setPredsDM(output);
               
               System.out.println("Final list count == > "+ output.size()); 
             rtx.execute("PF('dlg3').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Training was successful", ""));  
             
             
         }catch(Exception e){
              rtx.execute("PF('dlg3').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            "An error occured, please try again", ""));
         }
         
         return page;
    }
   
    public String PredictMultivariate(){
         ctx = FacesContext.getCurrentInstance();
         rtx = RequestContext.getCurrentInstance();
        String page = "dashboard.nnet";
        String networsavedstatename="";
         try{
             loadModel = getModelDM().getRowData();
                 System.out.println("This is the Max error "+ loadModel.getMaxiterations());
                 double normolizer = loadModel.getNormalizer(); 
            boolean numerals1 = isNumeric(data.getWheatprice());
            boolean numerals2 = isNumeric(data.getRiceprice());
            boolean numerals3 = isNumeric(data.getBeanprice());
            boolean numerals4 = isNumeric(data.getMaizeimport());
            boolean numerals5 = isNumeric(data.getMaizeproduction());
            boolean numerals6 = isNumeric(data.getRainfall());
            boolean numerals7 = isNumeric(data.getInflation());
             if(numerals1 == false || numerals2 == false ||
                     numerals3 == false || numerals4 == false
                     || numerals5 == false || numerals6 == false
                     || numerals7 == false){
              // rtx.execute("PF('dlg4').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "All values should be double data type e.g 89.12", ""));   
             }else{
                
              
                    //Go ahead and predict
                    if(data.getPolitics().equals("1")){
                  networsavedstatename="kenyaPerceptron1.nnet";
                }else if(data.getPolitics().equals("2")){
                networsavedstatename="kenyaPerceptron2.nnet";  
               }else{
           networsavedstatename="kenyaPerceptron3.nnet";  
                 }
                    NeuralNetwork loadedMlPerceptron = NeuralNetwork.load(networsavedstatename);
                    
                  double d1 = ((Double.parseDouble(data.getRiceprice()) - minlevel)*0.001) / normolizer;
               //System.out.println("NORMALIZED "+d1 *normolizer + " REAL "+ riceprice);
                double d2 = ((Double.parseDouble(data.getWheatprice()) - minlevel)*0.001) / normolizer;
               
                double d4 = (Double.parseDouble(data.getInflation()) - minlevel) / 100;
                double d5 = (Double.parseDouble(data.getInflation()) - minlevel) / 1000;
                double d6 = (Double.parseDouble(data.getMaizeproduction()) - minlevel) / normolizer2;
                double d7 = (Double.parseDouble(data.getBeanprice()) - minlevel) / 100;
                double d8 = (Double.parseDouble(data.getPolitics()) - minlevel);
                double d9 = (Double.parseDouble(data.getMaizeimport()) - minlevel) / normolizer3;
                  loadedMlPerceptron.setInput(d1,d2,d4,d5,d6,d7,d8,d9);
                  loadedMlPerceptron.calculate();
                  System.out.print(" Predicted "+ df2.format((loadedMlPerceptron.getOutput().firstElement())*normolizer));
                  
                
                 /*updateCountryWide(df2.format((d3*normolizer)/0.001),
                         df3.format((loadedMlPerceptron.getOutput().firstElement())*normolizer),df3.format(d3*normolizer));*/
                    setShowpredictedvalue("true");
                    setPredictedvalue(String.valueOf(df3.format(((loadedMlPerceptron.getOutput().firstElement())/1.88)*normolizer)));
                    
                    //Do an update where that month = to that value
                   // updatePredictions(month,selectedregion,predicted);
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Success", ""));  
                    rtx.execute("PF('dlg3').show()"); 
               
             }
         }catch(Exception ex){
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            "Error occured", "")); 
              ex.printStackTrace();
         }
        return page;
    }
    public String errorCheck(String predicted, String actual){
        String value = "";
        
        double error = Double.parseDouble(predicted) - Double.parseDouble(actual);
        
        value = String.valueOf(df2.format(error));
        //value = df2.format(value);
        return value;
        
    }
    public void resetMultivariate(){
       data = new Research();  
        setPredictedvalue(null);
    }
    public String Predict(){
         ctx = FacesContext.getCurrentInstance();
         rtx = RequestContext.getCurrentInstance();
        String page = "dashboard.nnet";
        String networsavedstatename="";
         try{
            
             if(selectedregion.equals("0")){
              // rtx.execute("PF('dlg4').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Please select region", ""));   
             }else if(month == null){
                  //rtx.execute("PF('dlg4').hide()");
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Please select date", ""));
                //check if previous month has a value
               
             }else{
                 System.out.println("I am checking previous month");
               boolean check = CheckPreviousMonth(month,selectedregion);
                if(check == false){
                    System.out.println("I am checking previous month check fail ");
                     //rtx.execute("PF('dlg4').hide()");
                   ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Previous month data is not available", ""));  
                }else{
                    System.out.println("I am checking previous month check success ");
                    //Go ahead and predict
                    if(selectedregion.equals("NAIROBI")){
                     networsavedstatename = "nrbPerceptron.nnet";
                 }else if(selectedregion.equals("ELDORET")){
                    networsavedstatename = "eldPerceptron.nnet"; 
                 }else{
                    networsavedstatename = "ksmPerceptron.nnet"; 
                 }
                    NeuralNetwork loadedMlPerceptron = NeuralNetwork.load(networsavedstatename);
                    
                    //TrainingSet testSet = new TrainingSet();
                    loadModel = getModelDM().getRowData();
                    //testSet.addElement(new TrainingElement(new double[]{d1}));
                    List d= getVars(month,selectedregion);
//                    for(int i = 0; i<d.size();i++){
//                    System.out.println("Value example "+d.get(i).toString());
//                    }
                  System.out.println("Value example "+d.get(1).toString() + " Example 2 "+d.get(2).toString());
                 loadedMlPerceptron.setInput((Double.parseDouble(d.get(0).toString())- minlevel)/loadModel.getNormalizer()
                         ,(Double.parseDouble(d.get(1).toString())- minlevel)/loadModel.getNormalizer(),
                         (Double.parseDouble(d.get(2).toString())- minlevel)/loadModel.getNormalizer(),
                         (Double.parseDouble(d.get(3).toString())- minlevel)/loadModel.getNormalizer());
                
                  loadedMlPerceptron.calculate();
                  String predicted = String.valueOf(df2.format((loadedMlPerceptron.getOutput().firstElement())*loadModel.getNormalizer()));
                 System.out.print(" Predicted "+predicted);
                    setShowpredictedvalue("true");
                    setPredictedvalue(String.valueOf(df3.format(Double.parseDouble(predicted))));
                    
                    //Do an update where that month = to that value
                    updatePredictions(month,selectedregion,predicted);
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Success", ""));  
                }
             }
         }catch(Exception ex){
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            "Error occured", ""));  
         }
        return page;
    }
    
    
     public String UpdateHistoricalPrediction(){
         ctx = FacesContext.getCurrentInstance();
         rtx = RequestContext.getCurrentInstance();
        String page = "dashboard.nnet";
        String networsavedstatename="";
         try{
            
             if(selectedregion.equals("0")){
              // rtx.execute("PF('dlg4').hide()");
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Please select region", ""));   
             }else if(month == null){
                  //rtx.execute("PF('dlg4').hide()");
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Please select date", ""));
                //check if previous month has a value
               
             }else{
                 System.out.println("I am checking previous month");
               boolean check = CheckPreviousMonth(month,selectedregion);
                if(check == false){
                    System.out.println("I am checking previous month check fail ");
                     //rtx.execute("PF('dlg4').hide()");
                   ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Previous month data is not available", ""));  
                }else{
                    System.out.println("I am checking previous month check success ");
                    //Go ahead and predict
                    if(selectedregion.equals("NAIROBI")){
                     networsavedstatename = "nrbPerceptron.nnet";
                 }else if(selectedregion.equals("ELDORET")){
                    networsavedstatename = "eldPerceptron.nnet"; 
                 }else{
                    networsavedstatename = "ksmPerceptron.nnet"; 
                 }
                    NeuralNetwork loadedMlPerceptron = NeuralNetwork.load(networsavedstatename);
                    
                    //TrainingSet testSet = new TrainingSet();
                    loadModel = getModelDM().getRowData();
                    //testSet.addElement(new TrainingElement(new double[]{d1}));
                    List d= getVars(month,selectedregion);
//                    for(int i = 0; i<d.size();i++){
//                    System.out.println("Value example "+d.get(i).toString());
//                    }
                  System.out.println("Value example "+d.get(1).toString() + " Example 2 "+d.get(2).toString());
                 loadedMlPerceptron.setInput((Double.parseDouble(d.get(0).toString())- minlevel)/loadModel.getNormalizer()
                         ,(Double.parseDouble(d.get(1).toString())- minlevel)/loadModel.getNormalizer(),
                         (Double.parseDouble(d.get(2).toString())- minlevel)/loadModel.getNormalizer(),
                         (Double.parseDouble(d.get(3).toString())- minlevel)/loadModel.getNormalizer());
                
                  loadedMlPerceptron.calculate();
                  String predicted = String.valueOf(df2.format((loadedMlPerceptron.getOutput().firstElement())*loadModel.getNormalizer()));
                 System.out.print(" Predicted "+predicted);
                    setShowpredictedvalue("true");
                    setPredictedvalue(predicted);
                    
                    //Do an update where that month = to that value
                    updatePredictions(month,selectedregion,predicted);
                    ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Success", ""));  
                }
             }
         }catch(Exception ex){
              ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                            "Error occured", ""));  
         }
        return page;
    }
    
    public Users getAuthUser() {
        return authUser;
    }

    public void setAuthUser(Users authUser) {
        this.authUser = authUser;
    }

    public Users getLoadUser() {
        return loadUser;
    }

    public void setLoadUser(Users loadUser) {
        this.loadUser = loadUser;
    }

    public Modelsettings getModel() {
        return model;
    }

    public void setModel(Modelsettings model) {
        this.model = model;
    }

    public Modelsettings getLoadModel() {
        return loadModel;
    }

    public void setLoadModel(Modelsettings loadModel) {
        this.loadModel = loadModel;
    }

    public String getSelecteddata() {
        return selecteddata;
    }

    public void setSelecteddata(String selecteddata) {
        this.selecteddata = selecteddata;
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
     public UsersController() {
        //this.setMaxCounter(145);
    }

    public UsersController(int maxCounter) {
        this.setMaxCounter(maxCounter);
    }
    
     public UsersController(String[] valuesRow) {
        this.setValuesRow(valuesRow);
    } 

    public List<Predictions> results(String actual, String predicted, String error,String mape) {
      
      System.out.println("Setting training results");
      Predictions preds = new Predictions();
      preds.setActual(actual);
      preds.setPredicted(String.valueOf(df3.format(Math.abs(Double.parseDouble(predicted)))));
      preds.setError(String.valueOf(df3.format(Math.abs(Double.parseDouble(error)))));
      preds.setMape(String.valueOf(df3.format(Math.abs(Double.parseDouble(mape)))));
      output.add(preds);
      
      return output;
    }

    public List<Predictions> getPredsDM() {
        return predsDM;
    }

    public void setPredsDM(List<Predictions> predsDM) {
        this.predsDM = predsDM;
    }

    public String getTotalnetworkError() {
        return totalnetworkError;
    }

    public void setTotalnetworkError(String totalnetworkError) {
        this.totalnetworkError = totalnetworkError;
    }

    public String getMadresult() {
        return madresult;
    }

    public void setMadresult(String madresult) {
        this.madresult = madresult;
    }

    public String getRmse() {
        return rmse;
    }

    public void setRmse(String rmse) {
        this.rmse = rmse;
    }

    public String getRenedered() {
        return renedered;
    }

    public void setRenedered(String renedered) {
        this.renedered = renedered;
    }
    
    @PostConstruct
  public void init() {
      
      createLineModels();
     

  }

  public LineChartModel getLineModel() {
      //getPredsDM();
      return lineModel;
  }

    public LineChartModel createLineModels() {
        System.out.println("I am here");
        lineModel = new LineChartModel();
      LineChartSeries s = new LineChartSeries();
      LineChartSeries s2 = new LineChartSeries();
      s.setLabel("Actual");
      s2.setLabel("Predicted");
      for(int i = 0; i<getPredsDM().size(); i++){
          
          s.set(i, Double.parseDouble(getPredsDM().get(i).getActual()));
          s2.set(i, Double.parseDouble(getPredsDM().get(i).getPredicted()));
       
      }
     

      lineModel.addSeries(s);
      lineModel.addSeries(s2);
      lineModel.setLegendPosition("e");
      Axis y = lineModel.getAxis(AxisType.Y);
      y.setMin(5);
      y.setMax(50);
      y.setLabel("Kshs Per Kg");

      Axis x = lineModel.getAxis(AxisType.X);
      x.setMin(0);
      x.setMax(getPredsDM().size());
      x.setTickInterval("1");
      x.setLabel("Number of data used for testing");
      return lineModel;
    }
     
    
    public LineChartModel createLineNrbModels() {
        System.out.println("I am here");
        lineModel = new LineChartModel();
      LineChartSeries s = new LineChartSeries();
      LineChartSeries s2 = new LineChartSeries();
      s.setLabel("Actual");
      s2.setLabel("Predicted");
      for(int i = 0; i<getNrbDM().size(); i++){
          
          s.set(i, Double.parseDouble(getNrbDM().get(i).getData()));
          s2.set(i, Double.parseDouble(getNrbDM().get(i).getPredictedvalue()));
       
      }
     

      lineModel.addSeries(s);
      lineModel.addSeries(s2);
      lineModel.setLegendPosition("e");
      Axis y = lineModel.getAxis(AxisType.Y);
      y.setMin(5);
      y.setMax(50);
      y.setLabel("Maize Price Kshs Per Kg");

      Axis x = lineModel.getAxis(AxisType.X);
      x.setMin(0);
      x.setMax(getNrbDM().size());
      x.setTickInterval("10");
      x.setLabel("Nairobi Region (Duration From Jan-06 to Apr-2018)");
      return lineModel;
    }

    
     public LineChartModel createLineRearchModels() {
        System.out.println("I am here");
        lineModel = new LineChartModel();
      LineChartSeries s = new LineChartSeries();
      LineChartSeries s2 = new LineChartSeries();
      s.setLabel("Actual");
      s2.setLabel("Predicted");
      for(int i = 0; i<getResearchDM().size(); i++){
          
          s.set(i, Double.parseDouble(getResearchDM().get(i).getPriceinkg()));
          s2.set(i, Double.parseDouble(getResearchDM().get(i).getPredictedvalue()));
       
      }
     

      lineModel.addSeries(s);
      lineModel.addSeries(s2);
      lineModel.setLegendPosition("e");
      Axis y = lineModel.getAxis(AxisType.Y);
      y.setMin(5);
      y.setMax(50);
      y.setLabel("Maize Price Kshs Per Kg");

      Axis x = lineModel.getAxis(AxisType.X);
      x.setMin(0);
      x.setMax(getResearchDM().size());
      x.setTickInterval("1");
      x.setLabel("Duration From 1992 to 2017");
      return lineModel;
    }
    public String getRendergraph() {
        return rendergraph;
    }

    public void setRendergraph(String rendergraph) {
        this.rendergraph = rendergraph;
    }
    public void viewGraph(){
        setRendergraph("true");
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public String getSelectedregion() {
        return selectedregion;
    }

    public void setSelectedregion(String selectedregion) {
        this.selectedregion = selectedregion;
    }

    private boolean CheckPreviousMonth(Date month, String table) {
       Statement stmt = null; 
        boolean test = true;
        PreparedStatement preparedstatement = null;
        String values="";
        int count = 0;
        Date dt = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(month); 
        c.add(Calendar.MONTH, -1);
        dt = c.getTime();
        Connection connection = null;
       
        SimpleDateFormat format = new SimpleDateFormat("MMM-YY");
        String date = format.format(dt);
        System.out.println("Date being searched "+ date);
        
        try {
          
             connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="SELECT COUNT(DATA) AS TOTAL FROM "+table+ " WHERE DATE = '"+date +"'";
            System.out.println(query);
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
             //preparedstatement.setString(1,date );
           
            
            ResultSet result = preparedstatement.executeQuery();
            while(result.next()){
               
               
                count = result.getInt("TOTAL");
                if(count== 0){
                    test = false;
                }else{
                    test = true;
                }
               
            }
            
             connection.close();
        } catch (Exception ioe) {
            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
//            System.out.println("Returning count result  ===> "+ test);
            test = true;
        }
        System.out.println("This is what is returned "+ test);
      return test;
    }

    private List getVars(Date month1, String table) {
        
        List vars = new ArrayList();
         Statement stmt = null; 
         
        String data = "";
        boolean test = true;
        PreparedStatement preparedstatement = null;
        String values="";
        int count = 0;
       Date dt = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(month1); 
        c.add(Calendar.MONTH, -1);
        dt = c.getTime();
        
        Date dt2 = new Date();
        Calendar c2 = Calendar.getInstance(); 
        c2.setTime(month1); 
        c2.add(Calendar.MONTH, -2);
        dt2 = c2.getTime();
        
        Date dt3 = new Date();
        Calendar c3 = Calendar.getInstance(); 
        c3.setTime(month1); 
        c3.add(Calendar.MONTH, -3);
        dt3 = c3.getTime();
        
        Date dt4 = new Date();
        Calendar c4 = Calendar.getInstance(); 
        c4.setTime(month1); 
        c4.add(Calendar.MONTH, -4);
        dt4 = c4.getTime();
        
        Connection connection = null;
       
        SimpleDateFormat format = new SimpleDateFormat("MMM-YY");
        String date = format.format(dt);
        String date2 = format.format(dt2);
        String date3 = format.format(dt3);
        String date4 = format.format(dt4);
        System.out.println("Date being searched == TWO == "+ date +
                " NEXT "+date2+" NEXT NEXT "+date3+" NEXT NEXT NEXT "+date4);
        
        
        try {
          
             connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="SELECT DATA FROM "+table+ " WHERE DATE IN (?,?,?,?)";
            System.out.println(query);
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
             preparedstatement.setString(1,date );
             preparedstatement.setString(2,date2 );
             preparedstatement.setString(3,date3 ); 
             preparedstatement.setString(4,date4 );
            
            ResultSet result = preparedstatement.executeQuery();
            while(result.next()){
               
               
                data = result.getString("DATA");
                System.out.println("Got all four data <==> " + data);
                vars.add(data);
               
            }
            
             connection.close();
        } catch (Exception ioe) {
            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
//            System.out.println("Returning count result  ===> "+ test);
            
        }
        System.out.println("This is what is returned "+ test);
      return vars;
    }

    public String getPredictedvalue() {
        return predictedvalue;
    }

    public void setPredictedvalue(String predictedvalue) {
        this.predictedvalue = predictedvalue;
    }

    public String getShowpredictedvalue() {
        return showpredictedvalue;
    }

    public void setShowpredictedvalue(String showpredictedvalue) {
        this.showpredictedvalue = showpredictedvalue;
    }

    public List<Nairobi> getNrbDM() {
        nrbDM = new ArrayList<>(nrbFacade.findAll());
        return nrbDM;
    }

    public void setNrbDM(List<Nairobi> nrbDM) {
        this.nrbDM = nrbDM;
    }

   
    
    

    private void updatePredictions(Date month, String table, String predicted) {
        Statement stmt = null;
       System.out.println("Updating table "+month + " Predicted vale "+ predicted + " Table "+ table);
      PreparedStatement preparedstatement = null;
       SimpleDateFormat format = new SimpleDateFormat("MMM-YY");
        String date = format.format(month);
        Connection connection = null;
        
         try{
            connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="UPDATE "+table+" SET PREDICTEDVALUE = ? WHERE"
                    + " DATE = ?";
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
            preparedstatement.setString(1,String.valueOf(df3.format(Double.parseDouble(predicted))));
            preparedstatement.setString(2,date);
           
            
            preparedstatement.execute();
            
            
            connection.close();
        }catch(Exception ex){
          ex.printStackTrace();
        } 
    }
     public String bulkUpload(){
        ctx = FacesContext.getCurrentInstance();
         String page = "bulkcreation.nnet";
         
         FileInputStream fis = null;
         String id="";
         String year="";
         String riceprice ="";
         String wheatprice="";
         String maizeprice="";
         String maizeyield="";
         String precipitation="";
         String inflation="";
         String politics = "";
         String beanprice="";
         String maizeimport = "";
         Date date = new Date();
          ctx = FacesContext.getCurrentInstance();
          rtx = RequestContext.getCurrentInstance();       
        String[] fileInfo = {"0", "1", "2", "3", "4", "5", "6"};
        
        //Set selected cours
        //String selected = students.getSelectedcourse();
         //setSelectedCourse(selected);
       // rtx.execute("PF('dlg1').show();");
              System.out.println(excelFile.getContentType());
              if(excelFile==null || !excelFile.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
               ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Wrong file format. Only xlsx files accepted",""));   
              }else{
        try(InputStream input = excelFile.getInputstream()){
           
            //First read 
             //Finds the workbook instance for XLSX file
                XSSFWorkbook myWorkBook = new XSSFWorkbook(input);
                //Return first sheet from the XLSX workbook
                
                int numberOfSheets = myWorkBook.getNumberOfSheets();
                for (int i = 0; i < numberOfSheets; i++) {
                XSSFSheet mySheet = myWorkBook.getSheetAt(i);
                Iterator<Row> rowIterator = mySheet.iterator();
                //Traversing over each row of XLSX file
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    //For each row, iterate through each columns
                    Iterator<Cell> cellIterator = row.cellIterator();

                    // The most important content
                    if (row.getRowNum() <= 0) {
                        continue; //just skip the rows if row number is 0 with titles
                    } else {
                        //get file details and set them
                         try {
                            year = row.getCell(0).toString();
                            //Put into the fileInfo Array index 0
                            data.setYear(year);
                            System.out.println("Admission number\t"
                                    + year + "\n");
                        } catch (Exception e) {
                           data.setYear("not provided");
                            System.out.println("Admission number not "
                                    + "provided " + e.getMessage());
                        }
                         
                         try {
                            riceprice = row.getCell(1).toString();
                            //Put into the fileInfo Array index 0
                            data.setRiceprice(riceprice);
                            System.out.println("riceprice\t"
                                    + riceprice + "\n");
                            
                            
                        } catch (Exception e) {
                             data.setRiceprice("not provided");
                            System.out.println("riceprice "
                                    + "provided " + e.getMessage());
                        }

                        try {
                            wheatprice = row.getCell(2).toString();
                            //Put into the fileInfo Array index 0
                            data.setWheatprice(wheatprice);
                            System.out.println("wheatprice \t"
                                    + wheatprice + "\n");
                        } catch (Exception e) {
                            data.setWheatprice("not provided");
                            System.out.println("wheatprice "
                                    + "provided " + e.getMessage());
                        }
                        
                         try {
                            maizeprice = row.getCell(3).toString();
                            //Put into the fileInfo Array index 0
                           data.setMaizeprice(maizeprice);
                            System.out.println("maizeprice \t"
                                    + maizeprice + "\n");
                        } catch (Exception e) {
                           data.setMaizeprice("not provided");
                            System.out.println("maizeprice not "
                                    + "provided " + e.getMessage());
                        }
                         
                          try {
                            maizeyield =row.getCell(4).toString();
                            //Put into the fileInfo Array index 0
                            data.setMaizeproduction(maizeyield);
                            System.out.println("maizeyield \t"
                                    + maizeyield + "\n");
                        } catch (Exception e) {
                            data.setMaizeproduction("not provided");
                            System.out.println("maizeyield not "
                                    + "provided " + e.getMessage());
                        }
                          
                          try {
                             inflation= row.getCell(5).toString();
                            //Put into the fileInfo Array index 0
                            data.setInflation(inflation);
                            System.out.println("inflation \t"
                                    + inflation + "\n");
                        } catch (Exception e) {
                            data.setInflation("not provided");
                            System.out.println("inflation not "
                                    + "provided " + e.getMessage());
                        }
                          
                           try {
                            precipitation = row.getCell(6).toString();
                            //Put into the fileInfo Array index 0
                           data.setRainfall(precipitation);
                            System.out.println("precipitation \t"
                                    + precipitation + "\n");
                        } catch (Exception e) {
                            data.setRainfall("not provided");
                            System.out.println("precipitation not "
                                    + "provided " + e.getMessage());
                        }
                           
                           try {
                            maizeimport = row.getCell(7).toString();
                            //Put into the fileInfo Array index 0
                           data.setMaizeimport(maizeimport);
                            System.out.println("Maize import \t"
                                    + maizeimport + "\n");
                        } catch (Exception e) {
                            data.setRainfall("not provided");
                            System.out.println("Maize import not "
                                    + "provided " + e.getMessage());
                        }
                        try {
                            politics = row.getCell(8).toString();
                            //Put into the fileInfo Array index 0
                           data.setPolitics(politics);
                            System.out.println("politics \t"
                                    + politics + "\n");
                        } catch (Exception e) {
                            data.setRainfall("not provided");
                            System.out.println("politics not "
                                    + "provided " + e.getMessage());
                        }
                         try {
                            beanprice = row.getCell(9).toString();
                            //Put into the fileInfo Array index 0
                           data.setBeanprice(beanprice);
                            System.out.println("Bean price \t"
                                    + beanprice + "\n");
                        } catch (Exception e) {
                            data.setBeanprice("not provided");
                            System.out.println("Bean price not "
                                    + "provided " + e.getMessage());
                        }
                       
                        dataFacade.create(data);
                        data = new Research();
                        
                        }
                       // 
                    
                     
                    }
                   
    
                 }
          ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "File details successfully uploaded",""));
            
        }catch(Exception ex){
           ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error occured on file upload",""));
           ex.printStackTrace();
        }
              }
        rtx.execute("PF('dlg1').hide();");
         return page;
     }

    public UploadedFile getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(UploadedFile excelFile) {
        this.excelFile = excelFile;
    }

    public Research getData() {
        return data;
    }

    public void setData(Research data) {
        this.data = data;
    }

    public List<Research> getResearchDM() {
        researchDM = dataFacade.findAll();
        return researchDM;
    }

    public void setResearchDM(List<Research> researchDM) {
        this.researchDM = researchDM;
    }

    public String getMaperesult() {
        return maperesult;
    }

    public void setMaperesult(String maperesult) {
        this.maperesult = maperesult;
    }
    
    private void updateCountryWide(String actual, String predicted,String priceinkg) {
       Statement stmt = null;
       //double priceinkg = Double.parseDouble(df2.format(actual))* 0.001;
      PreparedStatement preparedstatement = null;
       
       
        Connection connection = null;
        
         try{
            connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="UPDATE RESEARCH SET PREDICTEDVALUE = ?, PRICEINKG = ? WHERE"
                    + " MAIZEPRICE = ?";
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
            preparedstatement.setString(1,predicted);
            preparedstatement.setString(2,priceinkg);
            preparedstatement.setString(3,actual);
           
            
            preparedstatement.execute();
            
            
            connection.close();
        }catch(Exception ex){
          ex.printStackTrace();
        } 
        
    }

    public String getSelectedmode() {
        return selectedmode;
    }

    public void setSelectedmode(String selectedmode) {
        this.selectedmode = selectedmode;
    }

    public String getMultirendered() {
        return multirendered;
    }

    public void setMultirendered(String multirendered) {
        this.multirendered = multirendered;
    }

    public String getUnirenedered() {
        return unirenedered;
    }

    public void setUnirenedered(String unirenedered) {
        this.unirenedered = unirenedered;
    }
     
     public void modeController(){
         if(selectedmode.equals("0")){
             setMultirendered("false");
             setUnirenedered("false");
         }else if(selectedmode.equals("uni")){
             setUnirenedered("true");
             setMultirendered("false");
         }else if(selectedmode.equals("multi")){
             setUnirenedered("false");
             setMultirendered("true");
         }
     }
     
      public static boolean isNumeric(String number){
        
        try{
            
          //int a = Integer.parseInt(id);
           double a = Double.parseDouble(number);
        }catch(Exception ex){
            
            return false;
        }
        return true;
    }
}
