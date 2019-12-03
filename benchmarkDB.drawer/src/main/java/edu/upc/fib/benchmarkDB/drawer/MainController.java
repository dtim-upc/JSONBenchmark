package edu.upc.fib.benchmarkDB.drawer;

import javafx.geometry.Rectangle2D;
import java.io.File; 
import java.net.URL; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import edu.upc.fib.benchmarkDB.drawer.object.ConsolidatedResults;
import edu.upc.fib.benchmarkDB.drawer.object.DrawNumericObject;
import edu.upc.fib.benchmarkDB.drawer.object.Pair;
import edu.upc.fib.benchmarkDB.drawer.object.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField; 
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color; 
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Screen;
import java.util.Collections;

public class MainController extends Thread implements Initializable {

    @FXML
    private Pane pnCanvas;

    @FXML
    private Canvas cnvMainCanvas;

    @FXML
    private Label lblError;

    @FXML
    private TextField txtMinSize;
    
    @FXML
    private TextField txtFilePath;

    @FXML
    private Button btnSearchFile;

    @FXML
    private CheckBox chkNormalize;

    @FXML
    private Button btnPlotChart;

    @FXML
    private Button btnLoadObjects;

    @FXML
    private CheckComboBox<String> cmbTest;

    @FXML
    private ChoiceBox<String> cmbObject;
    
    List<ConsolidatedResults> lstConsolidatedResults = null; 
    
    
    @FXML
    void loadObjects(ActionEvent event) {
    	loadFile();
    	loadObjectsComboBox();
    	loadTestComboBox();
    }

    
    @FXML
    void searchFile(ActionEvent event) {
    	Stage stage = (Stage) cnvMainCanvas.getScene().getWindow();
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Dataset File");
    	
    	fileChooser.getExtensionFilters().addAll(
    	         new FileChooser.ExtensionFilter ("CSV and CST Files","*.csv","*.cst"));
    	
    	File file = fileChooser.showOpenDialog(stage);
    	
    	if (file != null) {
            txtFilePath.setText(file.getAbsolutePath());
        }
    }

    
    GraphicsContext gc =null;
    PlotDrawer objPloter = null;
    
    
	public void initialize(URL arg0, ResourceBundle arg1) {
		int xMinVal=0;
		int xMaxVal=2;
		int yMinVal=0; 
		int yMaxVal=200;
		int xLeftBorder=100;
		int xRightBorder=200;
		int yUpBorder=30;
		int yDownBorder=50; 
	   	    
	    
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();

        double ratio = screenWidth/screenHeight;

        
        int canvasH = (int)cnvMainCanvas.getHeight(); 
		int canvasW = (int)Math.round(canvasH*ratio);//(int)cnvMainCanvas.getWidth();
		
		objPloter = new PlotDrawer(xLeftBorder, xRightBorder, yUpBorder, yDownBorder, canvasW, canvasH, xMinVal, xMaxVal, yMinVal, yMaxVal);
		
		
		gc = cnvMainCanvas.getGraphicsContext2D();
    	gc.setFill(Color.BEIGE); 
        gc.setLineWidth(0.5); 
        gc.setLineDashOffset(10d);
           
		chkNormalize.setSelected(false);

		if(chkNormalize.isSelected()) {
			yMaxVal = 100;
			xMaxVal = 100;
		}
		
		objPloter.drawAxis(gc,null,false);
		
	}   	    
   
  
    @FXML
    void plotCSV(ActionEvent event) {
    	loadFile();
    	
    	boolean blnNormalized = chkNormalize.isSelected();
    	double minSize = 0;
    	if(txtMinSize.getText().trim().length()>0) {
    		minSize = Double.parseDouble(txtMinSize.getText());
    	}
    	
    	ArrayList<DrawNumericObject>  lstObjectForDrawing = groupByObject(blnNormalized,minSize);
    	
    	objPloter.drawLines(gc, lstObjectForDrawing,blnNormalized);
    	
    }
    private void loadFile() {
    	if (lstConsolidatedResults!=null) {
    		lstConsolidatedResults = null; 
    	}
    	String csvFilePath = txtFilePath.getText();
    	lstConsolidatedResults= CSVReader.readConsolidatedCSV(csvFilePath);
    }
    private void loadObjectsComboBox() {
    	HashMap<String, Double> lstObjects=new HashMap<String, Double>();

    	for (ConsolidatedResults result : lstConsolidatedResults) {
			if(lstObjects.get(result.getObject())==null) {
				lstObjects.put(result.getObject(), result.getRawSize()); 
			}
		} 
    	
    	String[] values = sortArray(lstObjects);

    	cmbObject.setItems(FXCollections.observableArrayList(values)); 
    }
    private void loadTestComboBox() {
    	final ObservableList<String> strings = FXCollections.observableArrayList();
    	List<String> lstTest = new ArrayList<String>();
    	for (ConsolidatedResults result : lstConsolidatedResults) {
    		boolean added = false;
    		for (int i = 0; i < lstTest.size(); i++) {
				if(lstTest.get(i).equals(result.getTestTypeDetail())) {
		    		added=true;
		    		break;
				}
			}
    		if(!added) {
    			lstTest.add(result.getTestTypeDetail());
    		}
		} 
    	Collections.sort(lstTest);
    	cmbTest.getItems().addAll(lstTest);
    }
     
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
    
    private String[] sortArray(HashMap<String, Double> arr) {  
        int n = arr.size()+1;  
        double temp = 0; 
        String sTemp = "";
        String [] sArr = new String[arr.size()+1];
        Double [] dArr = new Double[arr.size()+1];
        
        Iterator<Entry<String, Double>> it = arr.entrySet().iterator();
        int index=1;
        sArr[0] = "---All---";
        dArr[0] = 0d;
        while (it.hasNext()) {
        	Map.Entry pair = (Map.Entry)it.next();
            sArr[index] = (String)pair.getKey();
            dArr[index] = (Double)pair.getValue();
            it.remove();
            index++;
        }
        
        for(int i=1; i < n; i++){  
        	 for(int j=i+1; j < n; j++){  
        		 if(dArr[i] < dArr[j]){
        			 //swap elements
        			 temp = dArr[i];
        			 dArr[i] = dArr[j];  
                     dArr[j] = temp;  

                     sTemp = sArr[i];  
                     sArr[i] = sArr[j];  
                     sArr[j] = sTemp;
                  }  
             }  
         }  
         
         for (int i = 1; i < sArr.length; i++) {
			sArr[i] = sArr[i] + " (" + Math.round(dArr[i]*100d)/100d + " MB)"; 
		 }
         return sArr;
  
    }  
    
    private boolean isSelected(String testType) {
    	if(cmbTest.getCheckModel().getCheckedIndices().size()==0)
    		return true;
    	for (int i =0; i< cmbTest.getItems().size();i++) {
			if(cmbTest.getCheckModel().isChecked(i) && testType.equals(cmbTest.getCheckModel().getItem(i))){
				return true;
			}
		}
    	
    	return false;
    }
    
    private ArrayList<DrawNumericObject> groupByObject(boolean normalized, double minSize) {
    	String object = cmbObject.getValue();
    	ArrayList<DrawNumericObject> lstObjectForDrawing = new ArrayList<DrawNumericObject>();
    	
    	for (ConsolidatedResults results : lstConsolidatedResults) {
    		//Showing Results per object
    		if(!object.contains("All")) {
				if(object.split(" ")[0].equals(results.getObject())) {
					boolean added=false;
					for (DrawNumericObject drawNumericObject : lstObjectForDrawing) {
						if(drawNumericObject.getClassName().equals(results.getTestTypeDetail())){
							Pair oPair = drawNumericObject.getXyValues();
							if(results.getOperation().trim().toUpperCase().equals("SEL")) {
								oPair.setyValue(oPair.getyValue()+results.getTime());
							}else if(results.getOperation().trim().toUpperCase().equals("INS")) {
								oPair.setxValue(oPair.getxValue()+results.getTime());	
							}
							added=true;
						}
					}
					if(!added) {
						if(isSelected(results.getTestTypeDetail())){
							DrawNumericObject oDrawObject= new DrawNumericObject();
							Pair oPair = new Pair();
							if(results.getOperation().trim().toUpperCase().equals("SEL")) {
								oPair.setyValue(results.getTime());
							}else if(results.getOperation().trim().toUpperCase().equals("INS")) {
								oPair.setxValue(results.getTime());	
							}
							oDrawObject.setClassName(results.getTestTypeDetail());
							oDrawObject.setXyValues(oPair);
							oDrawObject.setSize(results.getSizeMB());
							lstObjectForDrawing.add(oDrawObject);
						}
					}
				}
    		}else {//Showing all the objects
    			if(results.getRawSize() >= minSize) {
        			boolean added=false;
    				for (DrawNumericObject drawNumericObject : lstObjectForDrawing) {
    					if(drawNumericObject.getClassName().equals(results.getTestTypeDetail())){
    						if(drawNumericObject.getSubClassName().equals(results.getObject())){
    							Pair oPair = drawNumericObject.getXyValues();
    							if(results.getOperation().trim().toUpperCase().equals("SEL")) {
    								oPair.setyValue(oPair.getyValue()+results.getTime());
    							}else if(results.getOperation().trim().toUpperCase().equals("INS")) {
    								oPair.setxValue(oPair.getxValue()+results.getTime());	
    							}
    							added=true;	
    						}
    					}
    				}
    				if(!added) {
						if(isSelected(results.getTestTypeDetail())){
	    					DrawNumericObject oDrawObject= new DrawNumericObject();
	    					Pair oPair = new Pair();
	    					if(results.getOperation().trim().toUpperCase().equals("SEL")) {
	    						oPair.setyValue(results.getTime());
	    					}else if(results.getOperation().trim().toUpperCase().equals("INS")) {
	    						oPair.setxValue(results.getTime());	
	    					}
	    					oDrawObject.setClassName(results.getTestTypeDetail());
	    					oDrawObject.setSubClassName(results.getObject());
	    					oDrawObject.setSize(results.getSizeMB());
	    					oDrawObject.setXyValues(oPair);
	    					lstObjectForDrawing.add(oDrawObject);
						}
    				}	
    			}
    		}
		}
    	if(normalized) {
    		double maxX = 0;
    		double maxY = 0;
    		for (DrawNumericObject drawNumericObject : lstObjectForDrawing) {
    			if(maxX < drawNumericObject.getXyValues().getxValue()) {
    				maxX= drawNumericObject.getXyValues().getxValue();
    			}
    			if(maxY < drawNumericObject.getXyValues().getyValue()) {
    				maxY= drawNumericObject.getXyValues().getyValue();
    			}
    		} 
    		for (DrawNumericObject drawNumericObject : lstObjectForDrawing) {
    			if(maxX>0) {
    				double xValue = drawNumericObject.getXyValues().getxValue()/maxX; 
    				drawNumericObject.getXyValues().setxValue(xValue*100d);
    			}
    			if(maxY>0) {
    				double yValue = drawNumericObject.getXyValues().getyValue()/maxY; 
    				drawNumericObject.getXyValues().setyValue(yValue*100d);
    			}
    		}
    	}
    	return lstObjectForDrawing;
    }
    
}
