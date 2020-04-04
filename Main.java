// importing library //
import java.util.*;


public class Main {
  static boolean run = true;
  public static void main(String[] args) {
   System.out.println("Welcome to this program! It will compute arithmetic operations between integers and/or fractions. Basically, a calculator for addition, subtraction, multiplicatin and division.");
    // Checkpoint 1 //
    while (run) {
      // requesting user's arithmetical expression by using Scanner//
      Scanner userInput = new Scanner(System.in);
      System.out.println("\nEnter an arithmetic expression or \"quit\": ");
      String expression = userInput.nextLine();
      // making sure that expression is understood //
      expression = expression.toLowerCase();
      if (!(expression.equals("quit"))) {
        // calling out main method - "calculate" //
        if(!(expression.contains("++") || expression.contains("--") || expression.contains("**") || expression.contains("//"))) {
          calculate(expression);
        }
        else {
          error(1);
          run = true;
        }
      }
      // in case user does not want to continue //
      else {
        System.out.println("Thanks...bye!");
        run = false;
      }
    }
  }

  public static void calculate(String expression) {
    // creating array list type string to split every section of the expression and seperating it by spaces //
    String[] splitExpression = expression.split(" ", 0); // we want to split whole expression, so we set a parameter of 0 //

    // creating an operation sign to determine which operator was used // 
    int sign = 0; // it is much easier to use int because it makes the checking of the operator much easier //

    // creating two significant varibles, allNum and allDenom, in order to sum all numerators and denominators that were entered //
    int totalNums = 0;
    int totalDenoms = 1;

    // the for loop is working until it reaches the end of the expression //
    /* Extra Credit - Multiple Operations */
    for (int item = 0; item < splitExpression.length; item++) {
      // creating another variable responsible for the specific part of the expression //
      String partOfex = splitExpression[item];

      // these two varibles will only be used inside the specific part, num and denom //
      int num = 0;
      int denom = 0;

      int underScore = partOfex.indexOf("_"); // underscore //
      int fracLine = partOfex.indexOf("/"); // fraction line //
      String add = "+";
      String sub = "-";
      String mult = "*";
      String div = "/";
      
      // now we determine what sign is being used, so that we can assign a certain number to the operation mode //
      if(partOfex.equals(add) || partOfex.equals(sub) || partOfex.equals(mult) || partOfex.equals(div)) {
        switch(partOfex) {
          case "+": sign = 0; break;
          case "-": sign = 1; break;
          case "*": sign = 2; break;
          case "/": sign = 3; break;
        }
      }
      // checking if the part contains a mixed fraction. if it does, it extracts each part the fraction, whole, num, and denom //
      else if (partOfex.contains("_")) {

        int whole = Integer.parseInt(partOfex.substring(0, underScore));

        num = Integer.parseInt(partOfex.substring(underScore + 1, fracLine));

        denom = Integer.parseInt(partOfex.substring(fracLine + 1, partOfex.length()));
        // calling method to convert mixed fraction into improper and assign it to numerator //
        num = improperFraction(whole, num, denom);
      }
      // if underscore is not present, then it's a regular fraction. we check that by testing whether the part has fraction line and it is not a division sign //
      else if (partOfex.length() != 1 && partOfex.contains("/")) {
        num = Integer.parseInt(partOfex.substring(0, fracLine));
        denom = Integer.parseInt(partOfex.substring(fracLine + 1, partOfex.length()));
      }
      // if none of the following is presente in the part, then it simply must be a whole number //
      else {
        // since most of the inputs will be fractions, the whole number will also then be in the form of a fraction //
        num = Integer.parseInt(partOfex);
        denom = 1;
      }
      // this is the calculation part //
      // it will loop every time with new partOfex value and check whether it can calculate or add values to variables such as totalNums and totalDenoms //
      if(num != 0 && denom != 0) {
        switch(sign) {
        // For instance "1/2 + 3/4" --> totalNums: 0, 1, 4, 10(finalNUM) totalDenoms: 2, 8(finalDENOM). Therefore, the expression is 10/8; //
         case 0:
          totalNums *= denom; 
          totalNums += num * totalDenoms;
          totalDenoms *= denom;
          break;
         case 1: // subtraction //
          totalNums *= denom;
          totalNums -= num * totalDenoms;
          totalDenoms *= denom;
          break;
         case 2: // multiplication // 
          totalNums *= num;
          totalDenoms *= denom;
          break;
         case 3: // division //
          totalNums *= denom;
          totalDenoms *= num;
          break;
        }
        if (totalNums < 0) { // checking whether the denom is negative //
          totalNums = -totalNums;
          totalDenoms = -totalDenoms;
        }
      }
      else if (num == 0) {
        totalNums += 0;
      }
      else if (denom == 0) { // check for error //
        error(0);
        run = true;
        return;
      }
    }
    // calling out method to reduce fraction //
    reduceFraction(totalNums, totalDenoms);
  }
  public static int improperFraction(int whole, int num, int denom) {
    // if the whole number is negative, the whole part of the expression must be negative, and therefore, we change the numerator //
    if (!(whole > 0)) {
      num = -num + whole * denom;
    }
    // if its not, then just convert it to improper //
    else {
      num = num + (whole * denom);
    }
    return num;
  }
  // hardest part of the code, to simplify the fraction //
  public static void reduceFraction(int totalNums, int totalDenoms) {
    // calling gcd method to find out the greates common factor of numerator and denominator //
    int gcd = gcd(totalNums, totalDenoms);
    int wholeNumber = 0;
    totalNums = totalNums / gcd;
    totalDenoms = totalDenoms / gcd;
    // if the combined nums are greater than combine denoms, divide them and store it into the whole number variable, and using mode, find the remainder of the division and assign it to num //
    if (totalNums >= totalDenoms) {
      wholeNumber = totalNums / totalDenoms;
      totalNums %= totalDenoms;
    }
    if(totalDenoms == 1) {
      wholeNumber += totalNums / totalDenoms;
      totalNums = 0;
      result(wholeNumber, totalNums, totalDenoms);
    }
    else {
    // calling result method to print the final statement on the output screen //
    result(wholeNumber, totalNums, totalDenoms);
    }
  }
  //gcd (greatest common factor) to reduce denominator to simplest form //
  public static int gcd(int num, int denom) {
    // cannot find any common factor //
    if(denom == 0) {
      return num;
    }
    return gcd(denom, num%denom);
  }
  public static void result(int wholeNumber, int totalNums, int totalDenoms) {
    // if there is no whole number, then just print num and denom //
    if (wholeNumber == 0 && !(totalNums == 0 || totalDenoms == 0)) {
      System.out.println("The answer is: " + (totalNums + "/" + totalDenoms));
    }
    else if (totalNums == 0 || totalDenoms == 0) {
      if (totalNums == 0) {
        System.out.println("The answer is: " + wholeNumber);
      }
      else if (totalDenoms == 0) {
        error(0);
      }
    }
    else {
      System.out.println("The answer is: " + (wholeNumber + "_" + (Math.abs(totalNums) + "/" + Math.abs(totalDenoms))));
    }
  }
  /* Extra Credit - Error Handling */
  public static void error(int type) {
    switch(type) {
      case 0: System.out.println("ERROR: Cannot divide by zero"); 
      break;
      case 1: System.out.println("ERROR: Input is in an invalid format"); 
      break;
      case 2: System.out.println("ERROR: No operator entered");
      break;
    }
  }
}