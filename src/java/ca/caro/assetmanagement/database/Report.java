/*
 * Copyright (C) 2022 CARO Analytical Services
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.caro.assetmanagement.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>Report Class</b>
 * <p>Class that handles the calculation of depreciation. 2 types of calculation Basic and Custom</p>
 * 
 * @author Neil Gilbert (761581)
 * @version 1.0
 */
public class Report {
    
    /**
     *  This method takes Asset ID and Depreciation ID and calculate the Depreciated Cost
     *  @param asset_id String Asset ID for the Asset that will be calculated for a depreciated cost
     *  @param deprec_info_id String Depreciation ID for the Depreciation that will be used for calculating the Asset's Depreciated Cost
     *  @return double Returns the depreciated cost value
     */
    public static double calcDepreciation(String asset_id, String deprec_info_id)
    {
        Connection conn = MSSQLConnection.getConnection();
        boolean customMode = false;
        //System.out.println("calcDepreciation |asset_id:"+asset_id + " |deprec_info_id:"+deprec_info_id);

        try
        {
            String queryA = "SELECT deprec_custom_mode FROM "+MSSQLConnection.getDBName()+".dbo.CARO_depreciation_information "
                    + "WHERE deprec_info_id = ?;";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            ResultSet rs = st.executeQuery();
            st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            rs = st.executeQuery();
            while (rs.next())
            {
                customMode = rs.getBoolean("deprec_custom_mode");
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        if(!customMode) {
            return calcRegular(asset_id, deprec_info_id);
        } else {
            return calcCustom(asset_id, deprec_info_id);
        }
    }
    
    /**
     *  This method takes Asset ID and Depreciation ID and calculate the Depreciated Cost using a Basic Method
     *  @param asset_id String Asset ID for the Asset that will be calculated for a depreciated cost
     *  @param deprec_info_id String Depreciation ID for the Depreciation that will be used for calculating the Asset's Depreciated Cost
     *  @return double Returns the depreciated cost value
     */
    private static double calcRegular(String asset_id, String deprec_info_id)
    {
        double deprec_rate = 0.0;
        double time_interval_value = 0.0;
        double c = 0.0;
        double n = 0.0;
        
        // PART A: GET DEPRECIATION VALUES
        Connection conn = MSSQLConnection.getConnection();
        try
        {
            // Get depreciation formula
            String queryA = "SELECT di.deprec_rate, dti.time_interval_value  FROM "+MSSQLConnection.getDBName()+".dbo.CARO_depreciation_information di "
                    + "LEFT JOIN "+MSSQLConnection.getDBName()+".dbo.CARO_depreciation_time_interval dti ON (di.time_interval_id=dti.time_interval_id)  "
                    + "WHERE di.deprec_info_id = ?;";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            ResultSet rs = st.executeQuery();
            st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            rs = st.executeQuery();
            while (rs.next())
            {
                deprec_rate = rs.getDouble("deprec_rate");
                time_interval_value = rs.getDouble("time_interval_value");
            }
            st.close();
            conn.close();
            //System.out.println(deprec_rate);
            //System.out.println(time_interval_value);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        // PART B: GET ASSET VALUES
        conn = MSSQLConnection.getConnection();
        try
        {
            //Add joins here if you want more items to be accessible
            String queryA = "SELECT cost, DATEDIFF(DAY, CURRENT_TIMESTAMP ,purchase_date) datediff "
                    + "FROM "+MSSQLConnection.getDBName()+".dbo.CARO_asset "
                    + "WHERE asset_id = ? ";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setString(1, asset_id);
            
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                c = rs.getDouble("cost");
                n = Math.abs(rs.getDouble("datediff"));
            }
            
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        //System.out.println("deprec_rate: "+deprec_rate);
        //System.out.println("time_interval_value: "+time_interval_value);
        //System.out.println("c: "+c);
        //System.out.println("n: "+n);
        
        // PART C: PLUG VARIABLES IN A FORMULA (for exponential)
        //return c * Math.pow((1 - (deprec_rate/time_interval_value)), (n*time_interval_value));
        
        // for linear
        Double ans = c * (1 - deprec_rate * (n/time_interval_value));
        return (ans<0? 0:ans);
    }
    
    /**
     *  This method takes Asset ID and Depreciation ID and calculate the Depreciated Cost using a Custom Method
     *  @param asset_id String Asset ID for the Asset that will be calculated for a depreciated cost
     *  @param deprec_info_id String Depreciation ID for the Depreciation that will be used for calculating the Asset's Depreciated Cost
     *  @return double Returns the depreciated cost value
     */
    private static double calcCustom(String asset_id, String deprec_info_id)
    {
        // PART A: GET DEPRECIATION
        Connection conn = MSSQLConnection.getConnection();
        String formula = "";
        // Check if depreciation exists and get formula
        try
        {
            // Get depreciation formula
            String queryA = "SELECT deprec_formula FROM "+MSSQLConnection.getDBName()+".dbo.CARO_depreciation_information "
                    + "WHERE deprec_info_id = ?;";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            ResultSet rs = st.executeQuery();
            st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            rs = st.executeQuery();
            while (rs.next())
            {
                formula = rs.getString("deprec_formula");
            }
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        // PART B: SUBSTITUTE VAR NAMES WITH DB VALUES
        ArrayList<String> paramVars = new ArrayList<String>();
        Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(formula);
        while (m.find()) {
            paramVars.add(m.group(1));
        }
        //System.out.println("OLD:"+formula);
        for(int i = 0; i<paramVars.size(); i++)
        {
            String retrievedVal = retrieveVar(asset_id, paramVars.get(i));
            if(retrievedVal!=null){
                formula = formula.replace("["+paramVars.get(i)+"]", retrievedVal);
            } else {
                formula = formula.replace("["+paramVars.get(i)+"]", 0+"");
            }
        }
        //System.out.println("NEW:"+formula);
        
        // PART C: PARSE FORMULA / EQUATION to POSTFIX method
        return calculateEquation(formula);
    }
    
    /**
     *  This method takes parses an equation and calculates it according to the Prefix and Postfix method
     *  @param equation String Equation that will be parsed
     *  @return double Returns the result of the calculation.
     */
    public static double calculateEquation(String equation)
    {
        //INFIX TO POSTFIX notation
        String eq = equation.replace(" ", "").toLowerCase();
        
        ArrayList<String> postfix = new ArrayList<String>();
        Stack symbols = new Stack();
        
        String valToParse = "";
        String functionToParse = "";
        for(int i = 0; i<eq.length(); i++)
        {
            //System.out.println(eq.charAt(i)+"-> "+valToParse+"-> "+postfix+" : "+symbols);
            switch(eq.charAt(i))
            {
                case '(':
                    symbols.push(eq.charAt(i));
                    if(functionToParse.length()>0){ symbols.add(functionToParse);functionToParse=""; }
                    break;
                case ')':
                    if(valToParse.length()>0){ postfix.add(valToParse);valToParse=""; }
                    while(symbols.peek().toString().charAt(0)!='(')
                    {
                        postfix.add(symbols.pop().toString());
                    }
                    symbols.pop();
                    
                    if(functionToParse.length()>0){ postfix.add(functionToParse);functionToParse=""; }
                    break; 
                case ',':
                    if(valToParse.length()>0){ postfix.add(valToParse);valToParse=""; }
                    while(symbols.peek().toString().charAt(0)!='(' &&
                            !(Character.isAlphabetic(symbols.peek().toString().charAt(0))))
                    {
                        postfix.add(symbols.pop().toString());
                    }
                    break; 
                default:
                    if(isOperator(eq.charAt(i)))
                    {
                        if(valToParse.length()>0){ postfix.add(valToParse);valToParse=""; }
                        symbols.push(eq.charAt(i));
                        functionToParse="";
                    }
                    
                    if(Character.isDigit(eq.charAt(i)) || eq.charAt(i)=='.') {
                        valToParse+=eq.charAt(i);
                    } else if(Character.isAlphabetic(eq.charAt(i))) {
                        functionToParse+=eq.charAt(i);
                    }
                    break;
            }
            if(i+1 >= eq.length()){
                while(symbols.size() > 0)
                {
                    if(valToParse.length()>0){ postfix.add(valToParse);valToParse=""; }
                    
                    if((char)symbols.peek()!='(')
                        postfix.add(symbols.pop().toString());
                    else
                        symbols.pop();
                }
            }
        }
        //System.out.println("INFIX:"+equation);
        //System.out.println("POSTFIX:"+postfix);
        
        //Evaluate postfix expression
        //postfix
        double operand1 = 0.0;
        double operand2 = 0.0;
        Stack operandBank = new Stack();
        for(int i = 0; i<postfix.size(); i++)
        {
            if(!Character.isDigit(postfix.get(i).charAt(0))) {
                switch(postfix.get(i).toString())
                {
                    //Math operations
                    case "+":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(operand1 + operand2);
                        break;
                    case "-":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(operand1 - operand2);
                        break;
                    case "*":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(operand1 * operand2);
                        break;
                    case "/":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(operand1 / operand2);
                        break;
                    case "%":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(operand1 % operand2);
                        break;
                        
                    //Math methods ( Based on https://www.javatpoint.com/java-math )
                    case "abs":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.abs(operand1));
                        break;
                    case "max":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.max(operand1, operand2));
                        break;
                    case "min":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.min(operand1, operand2));
                        break;
                    case "round":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.round(operand1));
                        break;
                    case "sqrt":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.sqrt(operand1));
                        break;
                    case "cbrt":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.cbrt(operand1));
                        break;
                    case "pow":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.pow(operand1, operand2));
                        break;
                    case "signum":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.signum(operand1));
                        break;
                    case "copysign":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.copySign(operand1, operand2));
                        break;
                    case "nextafter":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.nextAfter(operand1, operand2));
                        break;
                    case "nextdown":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.nextDown(operand1));
                        break;
                    case "random":
                        operandBank.push(Math.random());
                        break;
                    case "rint":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.rint(operand1));
                        break;
                    case "hypot":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.hypot(operand1, operand2));
                        break;
                    case "ulp":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.ulp(operand1));
                        break;
                    case "getexponent":
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.getExponent(operand1));
                        break;
                    case "ieeeremainder":
                        operand2 = Double.parseDouble(operandBank.pop().toString());
                        operand1 = Double.parseDouble(operandBank.pop().toString());
                        operandBank.push(Math.IEEEremainder(operand1, operand2));
                        break;
                }
                operand1 = 0.0;
                operand2 = 0.0;
            } else {
                operandBank.push(Double.parseDouble(postfix.get(i)));
            }
        }
        
        return Double.parseDouble(operandBank.pop().toString());
    }
    
    /**
     *  This method retrieves an asset value according to passed variable
     *  @param asset_id String Asset ID of the asset that the value will be retrieved from
     *  @param varName String The target variable's name
     *  @return String Returns value of an asset parameter from the database
     */
    private static String retrieveVar(String asset_id, String varName)
    {
        String[] var_option = varName.split("\\.");
        String result = "";
        
        switch(var_option[0])
        {
            case "currdate":
                long now = System.currentTimeMillis();
                Date d = new Date(now);
                result = d.toString();  
                break;
            default:
                Connection conn = MSSQLConnection.getConnection();
                try
                {
                    //Add joins here if you want more items to be accessible
                    String queryA = "SELECT * FROM "+MSSQLConnection.getDBName()+".dbo.CARO_asset "
                            + "WHERE asset_id = ?";
                    PreparedStatement st = conn.prepareStatement(queryA);
                    st.setString(1, asset_id);

                    ResultSet rs = st.executeQuery();
                    while (rs.next())
                    {
                        result = rs.getString(var_option[0]);
                    }

                    st.close();
                    conn.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
        
        if(var_option.length > 1)
            return parseOption(result, var_option)+"";
        return assertDouble(result);
    }
    
    /**
     *  This method parses a database value and returns a value depending on the selected option
     *  @param value String that will be parsed
     *  @param options String[] parse option
     *  @return double Returns parsed value
     */
    private static double parseOption(String value, String[] options) {
        if(value==null || value.equals(""))
            return 0.0;
        
        Calendar c = Calendar.getInstance();
        Date d;
        
        //System.out.println("TESTING: "+ value);
        switch(options[1])
        {
            case "month":
                d = Date.valueOf(value);
                c.setTime(d);
                return c.get(Calendar.MONTH);
            case "year":
                d = Date.valueOf(value);
                c.setTime(d);
                return c.get(Calendar.YEAR);
            case "daymonth":
                d = Date.valueOf(value);
                c.setTime(d);
                return c.get(Calendar.DAY_OF_MONTH);
            default:
                return Double.parseDouble(value);
        }
    }
    
    /**
     *  This method makes sure the passed parameter is a Double
     *  @param value String that will be parsed
     *  @return double Returns parsed value
     */
    private static String assertDouble(String value)
    {
        if(isValidDate(value)) {
            Date d = Date.valueOf(value);;
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.get(Calendar.SECOND)+"";
        }
        
        return value;
    }
    
    /**
     *  This method checks if passed parameter is a valid date
     *  @param inDate String that will be checked
     *  @return boolean Returns <b>true</b> if passed parameter is a valid date, <b>false</b> if not
     */
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (Exception pe) {
            return false;
        }
        return true;
    }
    
    /**
     *  This method checks if passed parameter is an operator
     *  @param valToParse char that will be checked
     *  @return boolean Returns <b>true</b> if passed parameter is an operator, <b>false</b> if not
     */
    private static boolean isOperator(char valToParse)
    {
        return valToParse=='+' || 
                valToParse=='-' ||
                valToParse=='*' ||
                valToParse=='/' ||
                valToParse=='%';
    }
    
    /**
     *  This method checks if depreciation with passed depreciation id has a valid formula
     *  @param deprec_info_id String Depreciation ID for the Depreciation that will be checked
     *  @return boolean Returns <b>true</b> if depreciation's formula is valid, <b>false</b> if not
     */
    public static boolean isDepFormulaValid(String deprec_info_id)
    {
        Connection conn = MSSQLConnection.getConnection();
        String formula = "";
        
        // Check if depreciation exists and get formula
        try
        {
            // Check if deprec info exists
            String queryA = "SELECT count(*) FROM "+MSSQLConnection.getDBName()+".dbo.CARO_depreciation_information "
                    + "WHERE deprec_info_id = ?;";
            PreparedStatement st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                int matches = rs.getInt(1);
                if(matches <= 0)
                    return false;
            }
            
            // Get depreciation formula
            queryA = "SELECT deprec_formula FROM "+MSSQLConnection.getDBName()+".dbo.CARO_depreciation_information "
                    + "WHERE deprec_info_id = ?;";
            st = conn.prepareStatement(queryA);
            st.setString(1, deprec_info_id);
            
            rs = st.executeQuery();
            while (rs.next())
            {
                formula = rs.getString("deprec_formula");
            }
            
            st.close();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return verifyFormula(formula);
    }
    
    /**
     *  This method checks if formula is valid
     *  @param formula String Formula that will be checked
     *  @return boolean Returns <b>true</b> if formula is valid, <b>false</b> if not
     */
    public static boolean verifyFormula(String formula)
    {
        if(formula.length() <= 0)
            return false;
        Stack group = new Stack();
        Stack varName = new Stack();
        
        for(int i = 0; i < formula.length(); i++)
        {
            switch(formula.charAt(i))
            {
                case '(':
                    group.push(formula.charAt(i));
                    break;
                case ')':
                    if(group.size()<=0)
                        return false;
                    if((char)group.peek()=='(') {
                        group.pop();
                    } else {
                        return false;
                    }
                    break;
                    
                case '[':
                    varName.push(formula.charAt(i));
                    break;
                case ']':
                    if(varName.size()<=0)
                        return false;
                    if((char)varName.peek()=='[') {
                        varName.pop();
                    } else {
                        return false;
                    }
                    break;
                default:
                    if(varName.size()==0)
                    {
                        if(Character.isAlphabetic(formula.charAt(i)))
                            return false;
                    }
            }
        }
        
        return true;
    }
}
