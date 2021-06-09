/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.adpassignementproject3;

/**
 *
 * @author Martinez safari
 */
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SerializeClass{
    private final String stakeholderOut = "stakeholder.ser";
    
    //write content files
    FileWriter fileWriter;
    PrintWriter printWriter;
    
    // read objects from .ser
    FileInputStream readFile;
    ObjectInputStream objectInput;
    
    public void openFile(String fileName){
        try{
            fileWriter = new FileWriter(new File(fileName));
            printWriter = new PrintWriter(fileWriter);
            System.out.println(fileName + " has been created");
            
        } 
        catch (IOException ioe){  
            System.out.println("error opening ser file: " + ioe.getMessage());
            
        }
    }
    
    // read the customer objects from .ser file and add it to the arrayList
    private ArrayList<Customer> customersList(){
        ArrayList<Customer> customers = new ArrayList<>(); 
        
        try{
            readFile = new FileInputStream(new File(stakeholderOut));
            objectInput= new ObjectInputStream(readFile);
            
            // throws an EOFException 
            while (true) {
                Object obj = objectInput.readObject();
                
                if (obj instanceof Customer){
                    customers.add((Customer) obj);
                }
            }  
        } 
        catch (EOFException eofe){    
        }
        
        catch (IOException | ClassNotFoundException e){
           System.out.println("*error*: " +e.getMessage());
           System.exit(1);     
        } 
        finally{
            
            try{
                readFile.close();
                objectInput.close();   
            } 
            catch (IOException e){
                System.out.println("*error*: " +e.getMessage());
            }
        }
        if (!customers.isEmpty()) {
            // sort arrayList
            Collections.sort(customers, new Comparator<Customer>() {
                @Override
                public int compare(Customer c1, Customer c2) {
                    return c1.getStHolderId().compareTo(c2.getStHolderId());
                }
            });
        } 
        return customers;
    } 
    private void writeCustomerOutFile(){
        String header = "======================= CUSTOMERS =========================\n";
        String title = "%s\t%-10s\t%-10s\t%-10s\t%-10s\n";
        String line = "===========================================================\n";
        
        try{   
            printWriter.print(header);
            printWriter.printf(title, "ID", "Name", "Surname", "Date Of Birth", "Age");
            printWriter.print(line);
            
            for (int i = 0; i < customersList().size(); i++){  
                
                printWriter.printf( title , customersList().get(i).getStHolderId(),customersList().get(i).getFirstName(),
                        customersList().get(i).getSurName(),dateFormat(customersList().get(i).getDateOfBirth()),
                        calculOfAge(customersList().get(i).getDateOfBirth())
                );
            }
            printWriter.printf( "\nNumber of customers who can rent: %d", canRent());
            
            printWriter.printf("\nNumber of customers who can not rent: %d", canNotRent());    
        } 
        catch (Exception e){
            System.out.println("*error*: " +e.getMessage());
        }
    }
    private String dateFormat(String dob){
        // Date format
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

        LocalDate parseDob = LocalDate.parse(dob); // Parsing the given String to Date object
        return parseDob.format(format);
    }
    
    private int calculOfAge(String dob){
        LocalDate parseDob = LocalDate.parse(dob); 
        int dobYear  = parseDob.getYear();
        
        // getting the current dateTime
        ZonedDateTime todayDate = ZonedDateTime.now(); 
        int currentYear = todayDate.getYear();
        
        // return the customer's age
        return currentYear - dobYear;
    }
    private int canRent(){
        int canRent = 0;
        
        for (int i = 0; i < customersList().size(); i++){
            
            if (customersList().get(i).getCanRent()){
                canRent += 1;
            }
        }
        
        return canRent;
    }
    
    private int canNotRent() {
        int canNotRent = 0;
        
        for (int i = 0; i < customersList().size(); i++) {
            
            if (!customersList().get(i).getCanRent()){
                canNotRent += 1;
            }
        }
        
        return canNotRent;
    }
    
    // read supplier objects from .ser file and add it to the arrayList
    private ArrayList<Supplier> suppliersList(){
        ArrayList<Supplier> suppliers = new ArrayList<>();
        try{
            readFile = new FileInputStream(new File(stakeholderOut));
            objectInput = new ObjectInputStream(readFile);
            
            // throws an EOFException
            while (true){
                Object obj = objectInput.readObject();
                
                if (obj instanceof Supplier){
                    suppliers.add((Supplier) obj);
                }
            }    
        } 
        catch (EOFException eofe) {   
        } 
        catch (IOException | ClassNotFoundException e){
            System.out.println("*error*: " +e.getMessage());
        } 
        finally{
            try{
                readFile.close();
                objectInput.close();    
            } 
            catch (IOException e){
                System.out.println("*error*: " +e.getMessage());
            }
        }  
        // check if suppliers arrayList is not empty
        if (!suppliers.isEmpty()){
            // sort arrayList
            Collections.sort(suppliers, new Comparator<Supplier>() {
                @Override
                public int compare(Supplier s1, Supplier s2) {
                    return s1.getName().compareTo(s2.getName());
                }
            });
        } 
        return suppliers;
    }  
    private void writeSupplierOutFile(){
        String header = "======================= SUPPLIERS =========================\n";
        String title = "%s\t%-20s\t%-10s\t%-10s\n";
        String line = "===========================================================\n";
        
        try{
            printWriter.print(header);
            printWriter.printf(title, "ID", "Name", "Prod Type", "Description");
            printWriter.print(line);
            for (int i = 0; i < suppliersList().size(); i++)
            {
                printWriter.printf(title, suppliersList().get(i).getStHolderId(), suppliersList().get(i).getName(),
                        suppliersList().get(i).getProductType(), suppliersList().get(i).getProductDescription()
                );
            } 
        } 
        catch (Exception e){
            System.out.println("*error*: " +e.getMessage());
        }
    }
    
    public void closeFile(String fileName){
        try {
            fileWriter.close();
            printWriter.close();
            System.out.println(fileName + " has been closed");

        }
        catch (IOException ex){
            System.out.println("**error closing the file: " +ex.getMessage());
        }
    }
    
    public static void main(String[] args){
        SerializeClass result = new SerializeClass();
        
        result.openFile("customerOutFile.txt");
        result.writeCustomerOutFile();
        result.closeFile("customerOutFile.txt");
        result.openFile("supplierOutFile.txt");
        result.writeSupplierOutFile();
        result.closeFile("supplierOutFile.txt");
    }
}