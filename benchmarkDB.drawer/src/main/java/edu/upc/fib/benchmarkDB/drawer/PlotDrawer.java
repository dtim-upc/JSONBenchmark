package edu.upc.fib.benchmarkDB.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.upc.fib.benchmarkDB.drawer.object.*;

public class PlotDrawer {
	private Color[] colors = null;
    double xLeftBorder, xRightBorder, yUpBorder, yDownBorder,canvasW,canvasH,xMinVal,xMaxVal,yMinVal,yMaxVal;
    double pointDiameter=5;
    Map<String, Entry<Color,Integer>> lstConfig = null;
    
	public PlotDrawer(double xLeftBorder, double xRightBorder, double yUpBorder, double yDownBorder,
					double canvasW, double canvasH,
					double xMinVal, double xMaxVal, double yMinVal, double yMaxVal) {
		initializeColors();
		
		
		this.xLeftBorder= xLeftBorder;
		this.xRightBorder = xRightBorder;
		this.yUpBorder = yUpBorder;
		this.yDownBorder = yDownBorder;
		this.canvasH = canvasH;
		this.canvasW = canvasW;
		this.xMinVal = xMinVal;
		this.xMaxVal = xMaxVal;
		this.yMinVal = yMinVal;
		this.yMaxVal = yMaxVal;
		
	}
	private void initializeColors() {
		colors = new Color[15];
		colors[0] = Color.BLACK;
		
		colors[1] = Color.RED;
		colors[2] = Color.FUCHSIA;
		colors[3] = Color.BLUE;
		colors[4] = Color.CORNFLOWERBLUE;
		colors[5] = Color.DARKMAGENTA;
		colors[6] = Color.VIOLET;
		colors[7] = Color.GREEN;
		colors[8] = Color.GREENYELLOW;
		colors[9] = Color.GOLD;
		colors[10] = Color.CADETBLUE;
		colors[11] = Color.BROWN;
		colors[12] = Color.INDIGO;
		colors[13] = Color.PURPLE;
		colors[14] = Color.OLIVEDRAB;
		
	}
	public void drawAxis(GraphicsContext gc,ArrayList<DrawNumericObject> lstResult, boolean blnNormalized) {
	 	gc.setFill(Color.BURLYWOOD); 
	 	gc.fillRect(xLeftBorder, yUpBorder+2, canvasW-xRightBorder-xLeftBorder, canvasH-yDownBorder-yUpBorder+2);
	 	
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        
        //Y Line 
        gc.strokeLine(xLeftBorder, yUpBorder+3, xLeftBorder, canvasH-yDownBorder+3);
        //X Line
        gc.strokeLine(xLeftBorder, canvasH-yDownBorder+3, canvasW-xRightBorder,canvasH-yDownBorder+3);

		if(!blnNormalized) {
	        gc.fillText("SELECT Time (s)", 25, 10);	
	        gc.fillText("INSERT Time (s)", canvasW/2-xLeftBorder/2, canvasH-10);
		}else {
	        gc.fillText("SELECT Time (%Max)", 25, 10);	
	        gc.fillText("INSERT Time (%Max)", canvasW/2-xLeftBorder/2, canvasH-10);			
		}
        //Draw numbers in X Axis
        int numberOfXBlocks=10;
        for (int i = 0; i <= numberOfXBlocks; i++) {
        	String text=Math.round(i*((int)xMaxVal/(int)numberOfXBlocks))+"";
	        gc.fillText(text, xLeftBorder+i*(canvasW-xLeftBorder-xRightBorder)/numberOfXBlocks-4, canvasH-yDownBorder/2);	
	        gc.strokeLine(xLeftBorder+i*(canvasW-xLeftBorder-xRightBorder)/numberOfXBlocks, canvasH-yDownBorder+3,
	        				xLeftBorder+i*(canvasW-xLeftBorder-xRightBorder)/numberOfXBlocks, canvasH-yDownBorder+13);
		}

        //Draw numbers in Y Axis
        int numberOfYBlocks=10;
        for (int i = 0; i <= numberOfYBlocks; i++) {
        	String text=Math.round((numberOfYBlocks-i)*((int)(yMaxVal)/(int)numberOfYBlocks))+"";
	        gc.fillText(text, xLeftBorder/2-8, yUpBorder+i*(canvasH-yUpBorder-yDownBorder)/numberOfYBlocks+2);	
	        gc.strokeLine(xLeftBorder-8, yUpBorder+i*(canvasH-yUpBorder-yDownBorder)/numberOfYBlocks+3,
	        				xLeftBorder, yUpBorder+i*(canvasH-yUpBorder-yDownBorder)/numberOfYBlocks+3);
		} 
        
		if(lstResult!=null) {
			HashMap<String, Entry<Color,Integer>> lstConfigTemp = new HashMap<String, Entry<Color,Integer>>();
	        int iColor=0;
	        for (DrawNumericObject drawNumericObject : lstResult) {
				if(lstConfigTemp.get(drawNumericObject.getClassName())==null) {
					lstConfigTemp.put(drawNumericObject.getClassName(), 
							new AbstractMap.SimpleEntry<Color,Integer>(colors[iColor%14+1],1));
					iColor++;
				}else {
					lstConfigTemp.get(drawNumericObject.getClassName()).setValue(lstConfigTemp.get(drawNumericObject.getClassName()).getValue()+1);
				}
			}
	        
	        //Draw Leyend
	        lstConfig = new TreeMap<String, Entry<Color, Integer>>(lstConfigTemp);
	        double startX1 = canvasW-xRightBorder+10;
	        double startY1 = yUpBorder+30;
	        
	        gc.setFill(new Color(0.956862745d, 0.956862745d, 0.956862745d,1)); 
		 	gc.fillRect(startX1, startY1-30, 230, 300);

	        if(lstResult!=null) {
	        	Iterator<Entry<String,Entry<Color,Integer>>> it=lstConfig.entrySet().iterator();
	        	while(it.hasNext()) {
	        		Entry<String,Entry<Color,Integer>> oPair = (Entry)it.next();
	        		gc.setFill(oPair.getValue().getKey());
		        	gc.fillText(oPair.getKey() + "(" + oPair.getValue().getValue() + ")",startX1, startY1-15);
		            startY1+=20;
		            iColor++;	
	        	}
	        }
		}
	}
	
	
	private void drawPoint(GraphicsContext gc, Color c, double xValue, double yValue, boolean isDiamant) {
		gc.setFill(c); 
        gc.setStroke(c); 
        if(isDiamant) {
        	gc.fillPolygon(new double[]{xValue*(canvasW-xLeftBorder-xRightBorder)/(xMaxVal-xMinVal)+xLeftBorder, 
					        			xValue*(canvasW-xLeftBorder-xRightBorder)/(xMaxVal-xMinVal)+xLeftBorder-4, 
					        			xValue*(canvasW-xLeftBorder-xRightBorder)/(xMaxVal-xMinVal)+xLeftBorder, 
					        			xValue*(canvasW-xLeftBorder-xRightBorder)/(xMaxVal-xMinVal)+xLeftBorder+4}, 
		        			new double[]{(yMaxVal-yValue)*(canvasH-yUpBorder-yDownBorder)/(yMaxVal-yMinVal)+yUpBorder-4,
		        					(yMaxVal-yValue)*(canvasH-yUpBorder-yDownBorder)/(yMaxVal-yMinVal)+yUpBorder, 
		        					(yMaxVal-yValue)*(canvasH-yUpBorder-yDownBorder)/(yMaxVal-yMinVal)+yUpBorder+4, 
		        					(yMaxVal-yValue)*(canvasH-yUpBorder-yDownBorder)/(yMaxVal-yMinVal)+yUpBorder}, 4);
        }
        else {
	        gc.fillOval(xValue*(canvasW-xLeftBorder-xRightBorder)/(xMaxVal-xMinVal)+xLeftBorder, 
	        		(yMaxVal-yValue)*(canvasH-yUpBorder-yDownBorder)/(yMaxVal-yMinVal)+yUpBorder, 
	        		pointDiameter, pointDiameter);
        }
	}
	
	public void drawLines(GraphicsContext gc, ArrayList<DrawNumericObject> lstResult, boolean blnNormalized) {
        //Lookig for maxValues
        yMaxVal = 0;
        xMaxVal = 0;
    	for (DrawNumericObject result: lstResult) {
			if(xMaxVal < result.getXyValues().getxValue()) {
    			xMaxVal = result.getXyValues().getxValue();
    		}
			if(yMaxVal < result.getXyValues().getyValue()) {
    			yMaxVal = result.getXyValues().getyValue();
    		}
		}
    	//yMaxVal = xMaxVal;
    	//xMaxVal = xMaxVal;
    	
    	gc.clearRect(0, 0, canvasW, canvasH);
	 	drawAxis(gc,lstResult,blnNormalized);   
	 	 
		double[] xValues = new double[lstResult.size()];
		double[] yValues = new double[lstResult.size()];
		String[] sValues = new String[lstResult.size()];
		xValues = convertNumbers(lstResult, true);
		yValues = convertNumbers(lstResult, false); 
		sValues = convertLabels(lstResult);
		gc.setLineDashOffset(0);   
		int i=0;
		for (double xvalue : xValues) {
			   
			if(xvalue>=convertSingleNUmber(xMaxVal, true)) { 
				xvalue-=5;
			}
			if(xvalue<=3) { 
				xvalue+=3;
			}
			if(yValues[i]<=6) { 
				yValues[i]+=6;
			}
			String label = sValues[i].substring(0,2);
			gc.setFill(colors[0]);
        	gc.fillText(label,xvalue+3, yValues[i]+5);
            
        	gc.setFill(lstConfig.get(lstResult.get(i).getClassName()).getKey()); 
			gc.fillOval(xvalue, yValues[i]+2, 6, 6);
			i++;
		}
	}
	
	private double[] convertNumbers(ArrayList<DrawNumericObject> values , boolean isX) {
		double[] new_values= new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			if(isX) {
				new_values[i] = convertSingleNUmber(values.get(i).getXyValues().getxValue(), isX);
			}else {
				new_values[i] = convertSingleNUmber(values.get(i).getXyValues().getyValue(), isX);
			}
		}
		return new_values;
	}
	
	private String[] convertLabels(ArrayList<DrawNumericObject> values) {
		String[] new_values= new String[values.size()];
		for (int i = 0; i < values.size(); i++) {
				new_values[i] = values.get(i).getSubClassName();
		}
		return new_values;
		
	}
	
	private double convertSingleNUmber(double number, boolean isX) {
		if(isX) {
			return number*(canvasW-xLeftBorder-xRightBorder)/(xMaxVal-xMinVal)+xLeftBorder;
		}else {
			return (yMaxVal-number)*(canvasH-yUpBorder-yDownBorder)/(yMaxVal-yMinVal)+yUpBorder; 
		}
	}
	
	private void drawNumbers(GraphicsContext gc, Color c, double[] xValues,double[] yValues, boolean isDiamant)
	{
		for (int i = 0; i < xValues.length; i++) {
			drawPoint(gc, c, xValues[i], yValues[i], isDiamant);
		}
	}
}
