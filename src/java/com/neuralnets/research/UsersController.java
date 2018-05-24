/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neuralnets.research;

import com.neuralnets.entities.Modelsettings;
import com.neuralnets.entities.Nairobi;
import com.neuralnets.entities.Users;
import com.neuralnets.facades.ModelsettingsFacade;
import com.neuralnets.facades.NairobiFacade;
import com.neuralnets.facades.UsersFacade;
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
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.TransferFunctionType;
import org.primefaces.context.RequestContext;
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
     private List<Nairobi> nrbDM = new ArrayList<>();
     HttpServletRequest request;
      private int maxCounter;
     private String[] valuesRow;
     private String totalnetworkError;
     private String madresult;
     private String rmse;
     private String renedered;
     private String rendergraph;
     private Date month;
     private String selectedregion;
     private String predictedvalue;
     private String showpredictedvalue;
     private static DecimalFormat df2 = new DecimalFormat(".######");
      FacesContext ctx;
      @EJB
    private UsersFacade usersFacade = new UsersFacade();
      
    @EJB
    private ModelsettingsFacade modelsettingsFacade = new ModelsettingsFacade();
    
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
                   
                    NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,4, 19,19, 1);
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
        
        
        
         try {
          
             connection = GetDatabaseConnection.getMysqlConnection();
            stmt =  connection.createStatement();
            String query ="SELECT ID,DATA FROM "+selecteddata;
             preparedstatement = (PreparedStatement) connection.prepareStatement(query);
           
           
            
            ResultSet result = preparedstatement.executeQuery();
            while(result.next()){
               
                values=result.getString("DATA");
                id = String.valueOf(result.getInt("ID"));
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
                System.out.print( "Actual"  + df2.format((d5*normolizer)));
                 testSet.addElement(new TrainingElement(new double[]{d1}));
                 loadedMlPerceptron.setInput(d1,d2,d3,d4);
                  loadedMlPerceptron.calculate();
                 System.out.print(" Predicted "+ df2.format((loadedMlPerceptron.getOutput().firstElement())*normolizer));
                  error =((loadedMlPerceptron.getOutput().firstElement())- (d5*normolizer)*normolizer);
                 System.out.println(" Error "+ df2.format(error));
                
                 rmse =+ (error*error);
                // List output = null;
                 //call function to add errors to list
                 results(df2.format((d5*normolizer)),
                         df2.format((loadedMlPerceptron.getOutput()
                                 .firstElement())*normolizer),df2.format(error));
            }
              setRenedered("true");
             sqrtrmse=sqrt((rmse/valuesRow.length));
              System.out.println(" Total RMSE  "+ df2.format(sqrtrmse));
                 setRmse(String.valueOf(df2.format(sqrtrmse)));
              mape = (rmse/valuesRow.length);
                 setMadresult(String.valueOf(df2.format(mape)));
                 
              System.out.println(" MAD  "+ df2.format(mape));
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

    public List<Predictions> results(String actual, String predicted, String error) {
      
      
      Predictions preds = new Predictions();
      preds.setActual(actual);
      preds.setPredicted(predicted);
      preds.setError(error);
      
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
      y.setLabel("Nominal Kshs Per Kg");

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
      y.setLabel("Nominal Kshs Per Kg");

      Axis x = lineModel.getAxis(AxisType.X);
      x.setMin(0);
      x.setMax(getNrbDM().size());
      x.setTickInterval("10");
      x.setLabel("Nairobi Region (Duration From Jan-06 to Apr-2018)");
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
       System.out.println("Updating table");
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
            preparedstatement.setString(1,predicted);
            preparedstatement.setString(2,date);
           
            
            preparedstatement.execute();
            
            
            connection.close();
        }catch(Exception ex){
          ex.printStackTrace();
        } 
    }
    
}