
import java.util.*;
public class Calculator {

	public static void main(String[] args) {
		try {		
			beginCalculation(args);
			
		}catch(AlgebraFailException a) {System.out.println("Insufficient number");}
		 catch(QuitMashingOnYourKeyboardException a) {System.out.println("Incorrect operation");}
		 catch(UserIsADumbassException a) {System.out.println("Missing number");}
		 catch(ArithmeticException a) {System.out.println("error, dividing by 0");}
		 catch(Exception a) {System.out.println("Incorrect expression");}
	}
	public static void beginCalculation (String[] args) {
		Deque<String> s = new LinkedList<String>(); //learn using deque from https://docs.oracle.com/javase/8/docs/api/java/util/Deque.html
		int length = 0;
		
		while(length < args.length) {

			if(args[length].equals("(")) {    
				s.addLast(args[length++]);   /** pushing "(" */
				
				while(s.contains("(")) {
					length=removeP(args, length, s);
				}									
			}				
			else			
			{ s.addLast(args[length++]); }						
		}
		
		double b=calculation(s);
		
		if(b % 1 ==0 && Math.abs(b)< Long.MAX_VALUE) //https://stackoverflow.com/questions/15963895/how-to-check-if-a-double-value-has-no-decimal-part
			System.out.println((long)b);
		else
			System.out.println(b);
	
	}
	
	public static int removeP (String[] args, int length, Deque<String> s ) {
		Deque<String> q = new LinkedList<>();
		
		/** if missing ")" in the middle of expression throw exception  */
		if (length >= args.length) 
			throw new QuitMashingOnYourKeyboardException();	
		
		while (!args[length].equals(")")) {
			
			s.addLast(args[length++]);
			
			/** if missing ")" at the end of the expression throw exception  */
			if (length >= args.length) 
				throw new QuitMashingOnYourKeyboardException();	
			
		}
		length++;
		
		while(!s.peekLast().equals("(")  ) {
			q.addFirst(s.removeLast());				
		}	
		
		s.removeLast();  /**remove the latest "(" from stack*/		
		
		s.addLast(String.valueOf(calculation(q))); /**save the result inside parenthesis back to Deque s*/
		
		return length;
							
	}
	
	public static double calculation (Deque<String> q) {
		Deque <Double> p = new LinkedList<>();
		String hold =""; 
		int operator, i=0;
		double dividend;
		
		
		try {
			while(!q.isEmpty()) {
				
				/**in the correct format, the number is in the even index, the operator is in the odd index
				 *throw exception if it is not in right format
				 */
				if(i%2 == 0) {
					p.addLast( Double.parseDouble(q.removeFirst()));
					i++;
					
				}
				else {
					operator=findOperator(q.removeFirst());
					i++;
					
					/** if no number after operator * or / or + or -
					 * throw exception
					 */
					if(q.peek()==null)
						throw new UserIsADumbassException();
					
					if(operator > 1) {
						
						if(operator==2) {
							p.addLast(p.removeLast()*Double.parseDouble(q.removeFirst()));
							i++;
						}
						else {
							dividend=Double.parseDouble(q.removeFirst());						
							if(dividend==0) 
								throw new ArithmeticException();
							
							p.addLast(p.removeLast()/dividend);
							i++;
						}
					}
					else hold+=operator;
				}
			}	
			
			int num=0;
			
			while(num <hold.length()) {
				if(hold.charAt(num)=='0') {
					p.addFirst(p.removeFirst()+p.removeFirst());
				}
				else {					
					p.addFirst(p.removeFirst()-p.removeFirst());
				}
				num++;	
			}
			
		}catch(NumberFormatException n) {
			throw new AlgebraFailException(); 
		}
		
		return p.removeFirst();		
	}
	
	//learn this from https://www.geeksforgeeks.org/stack-set-2-infix-to-postfix/
	public static int findOperator (String s) {
		if(s.equals("+"))
			return 0;
		if (s.equals("-"))
			return 1;
		if(s.equals("x"))
			return 2;
		if(s.equals("/"))
			return 3;
		throw new QuitMashingOnYourKeyboardException();
	}
}

class AlgebraFailException extends IllegalArgumentException{  
	 
	AlgebraFailException() {}
}

class QuitMashingOnYourKeyboardException extends IllegalArgumentException{
	
	QuitMashingOnYourKeyboardException() {}
}

class UserIsADumbassException extends IllegalArgumentException{
	
	UserIsADumbassException() {}
}

