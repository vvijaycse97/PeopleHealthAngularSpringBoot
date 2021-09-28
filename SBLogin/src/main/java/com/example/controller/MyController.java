package com.example.controller;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.HashMap; 
import java.util.LinkedHashMap; 
import java.util.Map; 
import java.util.Map.Entry;

@RestController
public class MyController {

	private static final Logger LOG = LoggerFactory.getLogger(MyController.class);	
	File f = new File("E:\\Contents\\PeopleHealth\\SBLogin\\healthdata_2019_2020_new_data.xlsx");
	@RequestMapping(value="/getDetails", method=RequestMethod.GET)
	public HashMap<String,Double> getDetails(Model model, HttpSession session,@RequestParam ("excelsheetname") String excelsheetname)
	throws IOException, InvalidFormatException {
		LOG.info("getDetails() and param value excelsheetname is ----->"+excelsheetname);
		  HashMap<String,Double>healthhm=new HashMap<String,Double>(); 
		 
		List<String>al=new ArrayList<String>(); 	
		List<String>alstates=new ArrayList<String>();   
		List<Double> alpct = new ArrayList<Double>();	
		LOG.info("before try block");
		try {
			LOG.info("before creating workbook");
			ZipSecureFile.setMinInflateRatio(-1.0d);
			Workbook workbook = WorkbookFactory.create(f);
			// Retrieving the number of sheets in the Workbook
			LOG.info("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
			int totalSheets=workbook.getNumberOfSheets();
			DataFormatter dataFormatter = new DataFormatter();
			for(int i=0;i<totalSheets;i++)
			{
				if(excelsheetname.equals(workbook.getSheetName(i)))
				{
					LOG.info("Input name and sheet name match found at location "+i);
					Sheet s = workbook.getSheetAt(i);
					Iterator<Row> rowIterator = s.rowIterator();
					while (rowIterator.hasNext()) {
						
						Row row = rowIterator.next();			
						// Now let's iterate over the columns of the current row
						Iterator<Cell> cellIterator = row.cellIterator();			
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							String cellValue = dataFormatter.formatCellValue(cell);
							
							if(!(cellValue.equals("State")|| cellValue.equals("Percentage"))
							&&!(cellValue == null || cellValue.trim().length() == 0))
							{	
							LOG.info(cellValue + "\t");
							al.add(cellValue);							
							}							
						}					
				}
			}
		}	
		for(int k=0;k<al.size();k++)
		 {
			 if(k%2==0)
			 {
				alstates.add(al.get(k));				 
			 }
			 else
			 {
			    alpct.add((Double.parseDouble(al.get(k))));
			 }			
		  }		
		   for(int m=0;m<alstates.size();m++)
		   {			   
			healthhm.put(alstates.get(m),alpct.get(m));			
		   }	
		   LOG.info("health hash map contents are ");
		   for(Map.Entry<String,Double> hhm: healthhm.entrySet())
		   {
			   if(hhm.getKey().equals("")||hhm.getValue().equals(""))
			   {
				   LOG.info("removing empty value");
				healthhm.remove(hhm.getKey());
			   }			   
		   }		
		LOG.info("HealthMap latest contents after sorting by Value is ");		
		healthhm=healthhm.entrySet()
                   .stream()
                   .sorted(Entry.comparingByValue((o1, o2) -> Double.compare(o1, o2)))
                   .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1,  e2) -> e1, LinkedHashMap::new));
				   for(Map.Entry<String,Double> hop: healthhm.entrySet())
				   {				   
				    LOG.info(hop.getKey()+"<***********************>"+hop.getValue());
				   }		
		 return healthhm;
		} catch (Exception e) {
			LOG.info("In catch block");
			LOG.info("Exception occurred "+e.getMessage());			
		}
		return healthhm;
	}
}
