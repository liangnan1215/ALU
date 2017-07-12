import java.math.BigDecimal;

import javax.swing.plaf.synth.SynthSpinnerUI;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_COLOR_BURNPeer;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @161250067 梁楠 [161250067 梁楠]
 *
 */

public class ALU {

	

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation (String number, int length) {		
		long num=Integer.parseInt(number);
		char[] result = new char[length];
		for (int i = 0; i < length; i++)
			result[length - 1 - i] = (char) (((num >> i) & 1) + '0');				
		return new String(result);
				
	}
	
	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		// TODO YOUR CODE HERE.	
		String []num=new String[2];
		if(number.contains(".")){
			num[0]=number.split("\\.")[0];
			num[1]=number.split("\\.")[1];
		}
		else{
			num[0]=number;
			num[1]="0";
		}		
		//分割整数与小数
		String sign="";//sign
		String exp="";//阶码
		String fac="";//尾数
		String result="";
		String intString="";
		String facString="";		
		if(number.equals("+Inf")){
			for(int i=0;i<eLength;i++)
				result=result+1;
			for(int i=0;i<sLength;i++)
				result=result+0;		
			return 0+result;
		}
		else if(number.equals("-Inf")){
			for(int i=0;i<eLength;i++)
				result=result+1;
			for(int i=0;i<sLength;i++)
				result=result+0;			
			return 1+result;
		}		
		//sign确定
		if(num[0].toCharArray()[0]=='-'){
			sign="1";
			num[0]=num[0].substring(1);//去掉-
		}
		else
			sign="0";		
		//正负0的情况
		if(Double.parseDouble(number)==0&&sign.equals("1")){
			result=sign;
			for(int i=0;i<eLength+sLength;i++)
				result=result+"0";			
			return result;
		}
		else if(Double.parseDouble(number)==0&&sign.equals("0")){
			result=sign;
			for(int i=0;i<eLength+sLength;i++)
				result=result+"0";
			return result;
		}		
		//正负无穷
		int intPart = Integer.parseInt(num[0]);
		num[1] = "0." + num[1];//转换成小数
		double facPart = Double.parseDouble(num[1]);
		System.out.println(num[0]);
		System.out.println(num[1]);		
		if(intPart!=0){
			//整数部分转换成二进制
			int temp=intPart%2;
			while(intPart!=0){
				intString=temp+intString;
				intPart=intPart/2;
				temp=intPart%2;				
			}			
			//正负无穷over
			if(intString.length()-1>(int)(Math.pow(2, eLength-1)))
				if(sign.equals("1"))
					return "-Inf";
				else
					return "+Inf";			
			//小数部分转换成二进制 			
			int tempfac=0;
			if(facPart*2>=1)
				 tempfac=1;
			while(facPart!=0){
				facString=facString+tempfac;
				facPart=facPart*2-tempfac;
				if(facPart*2>=1)
					 tempfac=1;
				else 
					tempfac=0;
			}
			//检测位数
			while(facString.length()!=sLength-intString.length()+1)
				facString=facString+0;			
			//计算exp
			int order=intString.length()-1;
			fac=intString.substring(1)+facString;
			int tempintnumber=(int)(Math.pow(2,eLength-1)-1+order);
			String transferexp=String.valueOf(tempintnumber);
			exp=integerRepresentation(transferexp,eLength);
			//输出结果
			result=sign+exp+fac;					
		}
		else{		
			//小数部分转换成二进制 		
			int tempfac=0;
			if(facPart*2>=1)
				 tempfac=1;
			while(facPart!=0){
				facString=facString+tempfac;
				facPart=facPart*2-tempfac;
				if(facPart*2>=1)
					 tempfac=1;
				else 
					tempfac=0;
			}
			//检测位数
			System.out.println(facString);	
			String judge="";
			int move=0;
			while(true){
				if(facString.charAt(0)=='1'){
					judge="1";
				}
				facString=facString.substring(1);
				move+=1;			
				if(judge.equals("1"))
					break;
			}			
			System.out.println(facString);	
			System.out.println(move);
			if(move==(int)Math.pow(2, eLength-1)){
				result=sign+result;
				for(int i=0;i<eLength;i++)
					result=result+0;
				result=result+"0"+"1"+facString;
				
				if(result.length()<1+sLength+eLength){
					int sizetemp=result.length();
					
					for(int i=0;i<1+sLength+eLength-sizetemp;i++){
						result=result+0;						
					}
				}				
				return result;					
			}			
			if(move==(int)Math.pow(2, eLength-1)-1){
				result=sign+result;
				for(int i=0;i<eLength;i++)
					result=result+0;
				result=result+1;
				for(int i=0;i<sLength-1;i++)
					result=result+0;
				return result;
			}
			
			if(move>(int)Math.pow(2, eLength-1)-1){
				result=sign+result;
				for(int i=0;i<eLength+sLength;i++)
					result=result+0;
				return result;
			}						
			if(facString.length()>sLength){
				facString=facString.substring(0,sLength);
			}
			while(facString.length()!=sLength)
				facString=facString+0;				
			//计算exp			
			fac=facString;
			int tempintnumber=(int)(Math.pow(2,eLength-1)-1-move);
			String transferexp=String.valueOf(tempintnumber);
			exp=integerRepresentation(transferexp,eLength);
			//输出结果
			result=sign+exp+fac;
		}				
		return result;				
	}
	
	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754 (String number, int length) {		
		int eLength=0;
		int sLength=0;		
		if(length==32){
			eLength=8;
			sLength=23;
		}
		if(length==64){
			eLength=11;
			sLength=52;
		}
		String result=floatRepresentation(number,eLength,sLength);
		// TODO YOUR CODE HERE.
		return result;
	}
	
	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {	
		char[]temp=operand.toCharArray();
		int sign=0;
		long number=0;
		//检测正负
		if(temp[0]=='1'){
			sign=1;
		}
		else
			sign=0;		
		char[]oper=operand.toCharArray();
		if(sign==0){
			for(int i=0;i<oper.length;i++){
				int a=(int)(Math.pow(2, i)*(oper[oper.length-1-i]-'0'));
				number+=a;
			}
		}
		else{			
			for(int i=0;i<oper.length;i++)
			{
				if(oper[i]=='1'){
					oper[i]='0';					
				}
				else {
					oper[i]='1';
				}
			}
			int cin=1;
			for(int i=0;i<oper.length;i++){
				if(oper[oper.length-1-i]=='1'&&cin==1){
					oper[oper.length-1-i]='0';
					cin=1;
				}
				else if(oper[oper.length-1-i]=='1'&&cin==0)
					oper[oper.length-1-i]='1';
				else if(oper[oper.length-1-i]=='0'&&cin==1){
					oper[oper.length-1-i]='1';
					cin=0;
				}
				else{
					oper[oper.length-1-i]='0';
					cin=0;
				}				
			}			
			for(int i=0;i<oper.length;i++){
				Long a=(long) (Math.pow(2, i)*(oper[oper.length-1-i]-'0'));
				number+=a;				
			}
		}		
		String result=String.valueOf(number);
		if(sign==1)
			result="-"+result;
		
		// TODO YOUR CODE HERE.
		return result;
	}
	
	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		// TODO YOUR CODE HERE.	
		String sign=operand.substring(0,1);
		String exp=operand.substring(1,eLength+1);		
		String fac=operand.substring(1+eLength);		
		System.out.println(exp+" "+fac);
		int expNumber=0;
		int facNumber=0;
		double doublePart=0;
		String result="";		
		int signFlag=Integer.parseInt(sign);
		char[]expNum=exp.toCharArray();
		char[]facNum=fac.toCharArray();
		
		if(exp.contains("1")==false&&fac.contains("1")==false&&sign.equals("0"))
			return "0.0";
		if(exp.contains("1")==false&&fac.contains("1")==false&&sign.equals("1"))
			return "-0.0";
		
		String testexp="";
		for(int i=0;i<eLength;i++)
			testexp+="1";
		String testfac="";
		for(int i=0;i<sLength;i++)
			testfac+="0";		
		if(testexp.equals(exp)&&testfac.equals(fac))
		{
			if(sign.equals("1"))
				return "-Inf";
			else
				return "+Inf";
		}
		else if(!(testfac.equals(fac))&&testexp.equals(exp))
			return "NaN";
		//计算exp
		else if(exp.contains("1")){		
			if(eLength>4){
				for(int i=0;i<expNum.length;i++){
					int a=(int)(Math.pow(2, i)*(expNum[expNum.length-1-i]-'0'));
					expNumber+=a;
				}
				expNumber=expNumber-(int)Math.pow(2, eLength-1)+1;
				//计算整数
				facNumber=(int)Math.pow(2, expNumber);
				for(int i=0;i<expNumber;i++){
					facNumber+=(int)(Math.pow(2, i)*(facNum[expNumber-1-i]-'0'));
				}
				//计算小数
				for(int i=0;i<facNum.length-expNumber;i++){
					doublePart+=Math.pow(2, -i-1)*(facNum[expNumber+i]-'0');			
				}	
				double resultDouble=facNumber+doublePart;			
				if(sign.equals("1"))
					result="-"+resultDouble;
				else
					result=result+resultDouble;
			}
			else{
				expNumber=(int) (Integer.valueOf(integerTrueValue(0+exp))-Math.pow(2,eLength-1)+1);				
				double floatVaule=Math.pow(2, expNumber);			
				double facPart=1;				
				for(int i=1;i<sLength;i++){				
					facPart=facPart+(facNum[i-1]-'0')*Math.pow(2, -i);				
				}		
				result=String.valueOf(facPart*floatVaule);					        
		        if(sign.equals("1"))
					result="-"+result;
				else	;
			}		
		}
		else if(exp.contains("1")==false&&fac.contains("1")){
			expNumber=1-(int)Math.pow(2,eLength-1);			
			double floatVaule=Math.pow(2, expNumber);			
			double facPart=0;
			for(int i=0;i<fac.length();i++){				
				 facPart = facPart+(facNum[i]-'0')*Math.pow(2, -i);				
			}			
			BigDecimal bigDecimal = new BigDecimal(floatVaule*facPart);  
	        result = bigDecimal.toString();  
	        if(sign.equals("1"))
				result="-"+result;
			else	;					
		}	
		return result;
	}
	
	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {
		
		char[]oper=operand.toCharArray();		
		for(int i=0;i<oper.length;i++){
			if(oper[i]=='1')
				oper[i]='0';
			else
				oper[i]='1';
		}	
		String result=new String(oper);
		
		return result;
	}
	
	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift (String operand, int n) {	
		String result=operand;
		for(int i=0;i<n;i++)
			result=result.substring(1)+0;
		return result;
	}
	
	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift (String operand, int n) {
		// TODO YOUR CODE HERE.		
		String result=operand;
		for(int i=0;i<n;i++)
			result=0+result.substring(0, operand.length()-1);
		return result;
	}
	
	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift (String operand, int n) {
		// TODO YOUR CODE HERE.		
		String result=operand;
		String sign=operand.substring(0,1);
		if(sign.equals("1")){
			for(int i=0;i<n;i++)
				result=1+result.substring(0,operand.length()-1);
		}
		else
			for(int i=0;i<n;i++)
				result=0+result.substring(0,operand.length()-1);
		return result;
	}
	
	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder (char x, char y, char c) {
		// TODO YOUR CODE HERE.	
		String result="";
		if(x=='1'&&y=='1'){
			if(c=='1')
				result=result+"11";
			else
				result=result+"10";
		}
		else if(x=='1'&&y=='0'){
			if(c=='1')
				result+="10";
			else
				result+="01";
		}
		else if(x=='0'&&y=='0'){
			if(c=='1')
				result+="01";
			else
				result+="00";
		}
		else{
			if(c=='1')
				result+="10";
			else
				result+="01";
		}		
		return result;
	}
	
	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得??
	 */
	public String claAdder (String operand1, String operand2, char c) {		
		char[]plus=operand1.toCharArray();
		char[]bplus=operand2.toCharArray();
		String result="";		
		for(int i=0;i<operand1.length();i++){
			String temp=fullAdder(plus[operand1.length()-1-i],bplus[operand1.length()-1-i],c);			
			char[]tempc=temp.toCharArray();
			c=tempc[0];			
			result=tempc[1]+result;
		}
		result = c+result;
		// TODO YOUR CODE HERE.
		return result;
	}
	
	/**
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		// TODO YOUR CODE HERE.
		char[]oper=operand.toCharArray();		
		char sign=operand.toCharArray()[0];		
		String result="";
		int cin=1;				
			for(int i=0;i<oper.length;i++){
				int a=oper[oper.length-1-i]-'0';
				int s=a^cin;
				cin=a&cin;
				result=s+result;
			}		
	//判断溢出的方法		
		if(sign=='1')//异号相加一定不溢出
			return "0"+result;
		else if(sign==result.toCharArray()[0])
			return "0"+result;
		else
			return "1"+result;
	}
	
	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		// TODO YOUR CODE HERE.
		//填充符号位
		if(operand1.length()<length){
			String signtemp=operand1.substring(0,1);
			int size=operand1.length();
			if(signtemp.equals("1"))
				for(int i=0;i<length-size;i++)
					operand1="1"+operand1;				
			else
				for(int i=0;i<length-size;i++)
					operand1="0"+operand1;
		}		
		if(operand2.length()<length){
			int size=operand2.length();
			String signtemp2=operand2.substring(0,1);
			if(signtemp2.equals("1"))
				for(int i=0;i<length-size;i++)
					operand2="1"+operand2;
			else
				for(int i=0;i<length-size;i++)
					operand2="0"+operand2;
		}		
		String temp=claAdder(operand1,operand2,c);
		String sign=temp.substring(0,1);		
		String result="";
		result+=temp;	
		if(operand1.substring(0,1).equals(operand2.substring(0,1))){
			String cinn=claAdder(operand1.substring(1),operand2.substring(1),c).substring(0,1);
			String cin=temp.substring(0,1);
			int OF=Integer.valueOf(cinn)^Integer.valueOf(cin);
			if(OF==1)
				result="1"+result.substring(1);
			else
				result="0"+result.substring(1);
		}
		else//异号不溢出
		{
			result="0"+result.substring(1);
		}
		return result;
	}
	
	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		
		String result=adder(operand1,operand2,'0',length);
		return result;
	}
	
	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		char[]temp=operand2.toCharArray();
		for(int i=0;i<operand2.length();i++){
			if(temp[i]=='1')
				temp[i]='0';
			else
				temp[i]='1';
		}
		operand2=new String(temp);
		String result=adder(operand1,operand2,'1',length);
		return result;
	}
	
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		String sign1=operand1.substring(0,1);
		String sign2=operand2.substring(0,1);
		String resultsign=sign1.equals(sign2)?"0":"1";
		if(operand1.length()!=length/2){
			int j=length/2-operand1.length();
			for(int i=0;i<j;i++){
				if(operand1.substring(0, 1).equals("0"))
					operand1=0+operand1;
				else
					operand1=1+operand1;
			}
		}
		if(operand2.length()!=length/2){
			int j=length/2-operand2.length();
			for(int i=0;i<j;i++){
				if(operand2.substring(0, 1).equals("0"))
					operand2=0+operand2;
				else
					operand2=1+operand2;
			}
		}
		char[]temp=operand1.toCharArray();
		for(int i=0;i<operand1.length();i++){
			if(temp[i]=='1')
				temp[i]='0';
			else
				temp[i]='1';
		}
		String tempoperand1=new String(temp);
		String antioperand1=oneAdder(tempoperand1).substring(1);
		String start="";
		for(int i=0;i<operand1.length();i++){
			start+="0";
		}
		
		String judge=operand2+"0";
		
		String result=start;
		
		for(int i=0;i<operand2.length();i++){
			if(judge.substring(judge.length()-2).equals("00")||judge.substring(judge.length()-2).equals("11"))
				{judge=result.substring(result.length()-1)+ariRightShift(judge,1).substring(1);
				result=ariRightShift(result,1);					
				if(i==operand2.length()-1)
					result=result+judge.substring(0, operand2.length());
				}
			else if(judge.substring(judge.length()-2).equals("01"))
				{result=adder(result,operand1,'0',operand2.length()).substring(1);
				 judge=result.substring(result.length()-1)+ariRightShift(judge,1).substring(1);
				result=ariRightShift(result,1);
				
				if(i==operand2.length()-1)
					result=result+judge.substring(0, operand2.length());
				}
			else if(judge.substring(judge.length()-2).equals("10"))
				{result=adder(result,antioperand1,'0',operand2.length()).substring(1);
				
				judge=result.substring(result.length()-1)+ariRightShift(judge,1).substring(1);
				result=ariRightShift(result,1);	
				
				if(i==operand2.length()-1)
					result=result+judge.substring(0, operand2.length());
				}
			else
				;
			System.out.println(result);
		}
		
			
		if(result.length()>length){
			String overFlow=result.substring(0,result.length()-length);
			
			if(overFlow.contains("1")==false&&result.substring(result.length()-length,result.length()-length+1).equals(resultsign))
				return 0+result.substring(result.length()-length);
			else
				return 1+result.substring(result.length()-length);
		}
		if(result.length()<length){
			int size=result.length();
			for(int i=0;i<length-size;i++)
				result=1+result;
		}
			
		result=0+result;
					
		return result;
	}
	
	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		
		if(operand2.contains("1")==false)
			return "NaN";
		int size=operand2.length();
		for(int i=0;i<length-size;i++){
			if(operand1.substring(0,1).equals("1"))
				operand1=1+operand1;
			if(operand1.substring(0,1).equals("0"))
				operand1=0+operand1;
			if(operand2.substring(0,1).equals("1"))
				operand2=1+operand2;
			if(operand2.substring(0,1).equals("0"))
				operand2=0+operand2;
		}
		String overflow="";
		String temp=negation(operand2);
		String neoperand2=oneAdder(temp).substring(1);//[-y]补码
		//余数寄存器R
		String saveR="";
		for(int i=0;i<operand1.length();i++)
			saveR=operand1.substring(0,1)+saveR;
		//商寄存器Q
		String saveQ=operand1;
		
		System.out.println(saveR+" "+saveQ);
		
		if(operand1.substring(0, 1).equals(operand2.substring(0,1))){
			overflow=integerAddition(operand1,neoperand2,length).substring(1,2)==operand2.substring(0,1)?"1":"0";
		}else{
			overflow=integerAddition(operand1,operand2,length).substring(1,2)==operand2.substring(0,1)?"1":"0";
		}
		
		for(int i=0;i<length;i++){
			System.out.println(saveR+" "+saveQ);
			saveR=saveR.substring(1)+saveQ.substring(0,1);
			saveQ=saveQ.substring(1);

			if(saveR.substring(0,1).equals(operand2.substring(0,1))){
				saveR=integerAddition(saveR,neoperand2,length).substring(1);
			}else
				saveR=integerAddition(saveR,operand2,length).substring(1);
			
			if((saveR+saveQ.substring(0,length-1-i)).contains("1")==false){
				saveQ=saveQ.substring(length-1-i);
				if(operand1.substring(0,1).equals(operand2.substring(0,1))){
					saveQ=saveQ+1;
					int tempsize=saveQ.length();
					for(int j=0;j<length-tempsize;j++)
						saveQ=saveQ+0;
				}else{
					saveQ=saveQ+0;
					int tempsize=saveQ.length();
					for(int j=0;j<length-tempsize;j++)
						saveQ=saveQ+1;
				}
				System.out.println(saveR+" "+saveQ);
				break;
			}
			if(saveR.substring(0,1).equals(operand2.substring(0,1)))
				saveQ=saveQ+1;
			else
				saveQ=saveQ+0;
		}
		System.out.println(saveR+" "+saveQ);
		if(operand1.substring(0,1).equals(operand2.substring(0,1))==false)
			saveQ=oneAdder(saveQ).substring(1);
		if(saveR.contains("1")==true&&saveR.substring(0,1).equals(operand1.substring(0,1))==false){
			if(operand1.substring(0,1).equals(operand2.substring(0,1)))
				saveR=integerAddition(saveR,operand2,length).substring(1);
			else
				saveR=integerAddition(saveR,neoperand2,length).substring(1);
		}
		String result=overflow+saveQ+saveR;
		return result;
	}
	
	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		//比较符号位，对加法 同号求和，若最高位产生进位，则结果溢出，和的符号位被加数的符号
		String sign1=operand1.substring(0,1);
		String sign2=operand2.substring(0,1);
		operand1=operand1.substring(1);
		operand2=operand2.substring(1);

		int size1=operand1.length();
		int size2=operand2.length();
		for(int i=0;i<length - size1;i++){
			operand1="0"+operand1;
			
		}
		
		for(int i=0;i<length - size2;i++){
			
			operand2="0"+operand2;
		}
		
		
		
		//-operand2 补码
		String temp=negation(operand2);
	
		String result="";
		if(sign1.equals(sign2)){
			 result=adder(0+operand1,0+operand2,'0',length+1);
			
			if(result.substring(0, 1).equals("1"))
				result=1+sign1+result.substring(2);
			else
				result=0+sign1+result.substring(2);
		}
		else{
			 result=adder(operand1,temp,'1',length);
			 String judge=adder(0+operand1.substring(1),0+temp.substring(1),'1',length).substring(1,2);
		
			if(judge.substring(0,1).equals("1"))
				result=0+sign1+result.substring(1);
			else{
				result=result.substring(1);
				result=negation(result);
				result=oneAdder(result).substring(1);
				if(sign1.equals("1"))
					sign1="0";
				else
					sign1="1";
				result=0+sign1+result;
			}
		}
		
		
		return result;
	}
	
	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		
		
		String exp1=operand1.substring(1,1+eLength);
		String exp2=operand2.substring(1,1+eLength);
		
		String fac1=1+operand1.substring(1+eLength);
		String fac2=1+operand2.substring(1+eLength);
		
		
		String expresult="";//指数位
		String facresult="";//尾数位
		String upOf="0";//上溢

		String result="";
		
		System.out.println(exp1+" "+exp2);
		System.out.println(fac1+" "+fac2);
		
		String signresult="0";
		//符号位判断
		
		while (fac1.length() < sLength + gLength) {
			fac1 = fac1 + "0";
		}
		while (fac2.length() < sLength + gLength) {
			fac2 = fac2 + "0";
		}
		
		if(operand1.substring(0,1).equals(operand2.substring(0,1))==false){
			return floatSubtraction(operand1,operand1.substring(0,1)+operand2.substring(1),eLength,sLength,gLength);
		}
		//相反数 特殊情况
		if(operand1.substring(0, 1).equals(operand2.substring(0,1))==false&&exp1.equals(exp2)&&fac1.equals(fac2))
		{
			for(int i=0;i<2+eLength+sLength;i++)
				result=0+result;
			
			return result;
		}
		
		//inf情况
		if(exp1.contains("0")==false||exp2.contains("0")==false){
			if(exp2.contains("0")==true){		
					return 1+operand1;
			}
			else if(exp1.contains("0")==true)
				return 1+operand2;
			else if(operand1.substring(0,1).equals(operand2.substring(0,1)))
				return 1+operand1;
			else {
				for(int i=0;i<2+eLength+sLength;i++){
					result=result+0;
				}
				return result;
			}
		}
		
		//对阶
		if (operand1.substring(1).contains("1")==false) {
			upOf= "0";
			signresult=operand2.substring(0,1);
			result = upOf + signresult + operand2.substring(1);
		} 
		else if (operand2.substring(1).contains("1")==false) {
			upOf = "0";
			signresult=operand1.substring(0,1);
			result = upOf + signresult + operand1.substring(1);
		}
		else{
			
			if(exp1.equals(exp2)){
				expresult=exp1;
				expresult=oneAdder(expresult);
				//尾数加减
				String temp=signedAddition(operand1.substring(0,1)+fac1,operand2.substring(0,1)+fac2,1+sLength+gLength);
			
			
				if (expresult.substring(0,1).equals(1)) {
					// 上溢
					upOf= "1";
					signresult = "0";
					for (int i = 0; i < eLength+sLength; i++) {
						result += "1";
					}
					result = upOf +signresult+ result;
				} 
				else {
					facresult = temp.substring(3,3+sLength);
					upOf = temp.substring(0,1);
					signresult=temp.substring(1,2);
					result = upOf+signresult+expresult.substring(1)+facresult;
					}
			}
		
			else{
				int times=0;
				int a=Integer.valueOf(exp1,2);
				int b=Integer.valueOf(exp2,2);
		
				
				if(a>b)//△大于等于0时	
					{	
						expresult=exp1;
						times=a-b;
						for(int i=0;i<times;i++){
							fac2=logRightShift(fac2, 1);
						}
						
						System.out.println(fac2);
						if (fac2.contains("1")==false) {
							upOf = "0";
							signresult = operand1.substring(0,1);
							result =upOf+signresult+ operand1.substring(1);
							return result;
						}
					}
				else{
						expresult=exp2;
						times=b-a;
				 		for(int i=0;i<times;i++){
				 			fac1=logRightShift(fac1,1);
				 		}
				 		
				 		
				 		if (fac1.contains("1")==false) {
							upOf = "0";
							signresult=operand2.substring(0,1);
							result =upOf +signresult+ operand2.substring(1);
							
							return result;
						}
				 		
			 	}
				
			
				//尾数加减
				
				String temp=signedAddition(operand1.substring(0,1)+fac1,operand2.substring(0,1)+fac2,1+sLength+gLength);
				facresult=temp;
				System.out.println(facresult);
				if (facresult.substring(3,4).equals("0")&&(fac1.substring(0, 1).equals("1")||fac2.substring(0, 1).equals("1"))) {
					// 上溢
					upOf= "1";
					signresult = "0";
					for (int i = 0; i < eLength; i++) {
						result += "1";
					}
					for (int i = 0; i < sLength; i++) {
						result += "0";
					}
					result = upOf +signresult+ result;
				} 
				else {
					facresult = facresult.substring(4,4+sLength);
					upOf = temp.substring(0,1);
					signresult=temp.substring(1,2);
					result = upOf+signresult+expresult+facresult;
				}
			}
		}
		
		 
		
	
		
		
			
			
		
		return result;
	}
	
	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		
		String sign1=operand1.substring(0,1);
		String sign2=operand2.substring(0,1);
		
		String exp1=operand1.substring(1,1+eLength);
		String exp2=operand2.substring(1,1+eLength);
		 
		String fac1=operand1.substring(1+eLength);
		String fac2=operand2.substring(1+eLength);
	
		String result="";
		String signresult="0";
		String upOf="0";
		String expresult="";
		String facresult="";
	
		
		while (fac1.length() < sLength + gLength) {
			fac1 = fac1 + "0";
		}
		while (fac2.length() < sLength + gLength) {
			fac2 = fac2 + "0";
		}
		
		// 0 的情况
		if(operand1.equals(operand2)){
			for(int i=0;i<2+eLength+sLength;i++)
				result=result+0;
			return result;
		}
		
		//inf的情况
		if(exp1.contains("0")==false||exp2.contains("0")==false){
			if(exp2.contains("0")==true){	
					return 1+operand1;
			}
			else if(exp1.contains("0")==true){
				if(sign2.equals("1"))
					return "1"+"0"+operand2.substring(1);
				else
					return "1"+"1"+operand2.substring(1);
			}
			else if(sign1.equals(sign2)==false)
				return 1+operand1;
			else {
				for(int i=0;i<2+eLength+sLength;i++){
					result=result+0;
				}
				return result;
			}
		}
		
		String negfac2=negation(fac2);
		
		negfac2=oneAdder(negfac2).substring(1);//(-operand2)
		
		
		if(sign1.equals(sign2)==false){
			return floatAddition(operand1,sign1+operand2.substring(1),eLength,sLength,gLength);
		}
		
		
		
		//对阶
		if (operand1.substring(1).contains("1")==false) {
			upOf= "0";
			if(operand2.substring(0,1).equals("1"))
				signresult="0";
			else
				signresult="1";
			result = upOf + signresult + operand2.substring(1);
		} 
		else if (operand2.substring(1).contains("1")==false) {
			upOf = "0";
			signresult=operand1.substring(0,1);
			result = upOf + signresult + operand1.substring(1);
		}
		else{
			
			if(exp1.equals(exp2)){
				expresult=exp1;
				
				String temp="";
			
				//尾数加减
				
				temp=signedAddition(sign1+fac1,sign2+negfac2,1+sLength+gLength);
				
				
				
				
					int time=0;
					
					facresult = temp.substring(3,3+sLength);
				
					
					while(facresult.substring(0,1).equals("0")){
						facresult=facresult.substring(1);
						time+=1;
					}
					time=time+1;
					time=-time;
					
					facresult=facresult.substring(1);
					
					if(facresult.length()>sLength)
						facresult=facresult.substring(0,sLength-1);
					else
					{
						while(facresult.length()!=sLength)
							facresult+=0;
					}
					
					
					
					signresult=temp.substring(1,2);
					
					String changexp=integerRepresentation(String.valueOf(time),eLength);
					
					expresult=integerAddition(0+expresult,changexp,1+eLength).substring(2);
					
					result = 0+signresult+expresult+facresult;
					
					return result;
				
			}
		
			else{
				int times=0;
				int a=Integer.valueOf(exp1,2);
				int b=Integer.valueOf(exp2,2);
		
				if(a>b)//△大于等于0时	
					{	
						expresult=exp1;
						times=a-b;
						fac2=1+fac2;
						for(int i=0;i<times;i++){
							fac2=logRightShift(fac2, 1);
						}
						
						
						if (fac2.contains("1")==false) {
							upOf = "0";
							signresult = operand1.substring(0,1);
							result =upOf+signresult+ operand1.substring(1);
							return result;
						}
						
						String temp="";
						negfac2=negation(fac2);
						negfac2=oneAdder(negfac2).substring(1);
						
						temp=signedAddition(sign1+1+fac1,sign2+negfac2,1+sLength+gLength);
						System.out.println(temp);
						int time=0;
						
						facresult = temp.substring(2);
						
						System.out.println(facresult);
						while(facresult.substring(0,1).equals("0")){
							facresult=facresult.substring(1);
							time+=1;
						}
						time=-time;
						facresult=facresult.substring(1);
						System.out.println(facresult);
						if(facresult.length()>sLength)
							facresult=facresult.substring(0,sLength);
						else
						{
							while(facresult.length()!=sLength)
								facresult+=0;
						}
						signresult=temp.substring(0,1);
						String changexp=integerRepresentation(String.valueOf(time),eLength);			
						expresult=integerAddition(0+expresult,changexp,1+eLength).substring(2);
						result = 0+signresult+expresult+facresult;
						return result;
					}
				else{
						expresult=exp2;
						times=b-a;
						fac1=1+fac1;
				 		for(int i=0;i<times;i++){
				 			fac1=logRightShift(fac1,1);
				 		}
				 		
				 		System.out.println(fac1);
				 		if (fac1.contains("1")==false) {
							upOf = "0";
							signresult=operand2.substring(0,1);
							if(signresult.equals("1"))
								signresult="0";
							else
								signresult="1";
							result =upOf +signresult+ operand2.substring(1);
							
							return result;
						}
				 		String negfac1="";
				 		String temp="";
						negfac1=negation(fac1);
						negfac1=oneAdder(negfac1).substring(1);	
						temp=signedAddition(sign1+negfac1,sign2+1+fac2,1+sLength+gLength);		
						System.out.println(temp);	
						int time=0;	
						facresult = temp.substring(2);	
						while(facresult.substring(0,1).equals("0")){
							facresult=facresult.substring(1);
							time+=1;
						}	
						time=-time;	
						facresult=facresult.substring(1);			
						if(facresult.length()>sLength)
							facresult=facresult.substring(0,sLength);
						else
						{
							while(facresult.length()!=sLength)
								facresult+=0;
						}					
						signresult=temp.substring(0,1);
						String changexp=integerRepresentation(String.valueOf(time),eLength);					
						expresult=integerAddition(0+expresult,changexp,1+eLength).substring(2);					
						result = 0+signresult+expresult+facresult;						
						return result;
			 	}			
			}
		}
					return result;

	}
	
	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		
		
		String sign1=operand1.substring(0,1);
		String sign2=operand2.substring(0,1);
		
		String exp1=operand1.substring(1,1+eLength);
		String exp2=operand2.substring(1,1+eLength);
		
		String fac1=operand1.substring(1+eLength);
		String fac2=operand2.substring(1+eLength);
		
		String result="";
		fac1=1+fac1;
		fac2=1+fac2;
		
		
		System.out.println(exp1+" "+exp2);
		System.out.println(fac1+" "+fac2);
		String signresult="";
		if(sign1.equals(sign2))
			signresult="0";
		
		else
			signresult="1";
		
		if(operand1.substring(1).contains("1")==false)
			return 0+operand1;
		if(operand2.substring(1).contains("1")==false)
			return 0+operand2;
		
		
		if(exp1.contains("0")==false)
			return 1+operand1;
		else if(exp2.contains("0")==false)
			return 1+operand2;
		
		
		String facresult=Multiplication(fac1,fac2,sLength+1);
		
		
		String judge=facresult.substring(0,2);
		
		System.out.println(facresult);
		System.out.println(judge);
		
		String expresult=signedAddition(0+exp1,0+exp2,1+eLength).substring(2);
		System.out.println(expresult);
		
		int temp=-((int)Math.pow(2, eLength-1)-1);
		
		String plus=integerRepresentation(String.valueOf(temp),eLength);		
		
		
		System.out.println(plus);
		expresult=signedAddition(expresult,0+plus,1+eLength).substring(3);
		
		System.out.println(expresult);
		
		
		
		
			
		if(judge.equals("01")){
			if(expresult.contains("0")==false){
				
				for(int i=0;i<eLength;i++){
					result=result+1;
				}
				for(int i=0;i<sLength;i++){
					result=result+0;
				}
				return 1+signresult+result;
			}
			else if(expresult.substring(0,1).equals("0")&&exp1.substring(0,1).equals("1")&&exp2.substring(0,1).equals("1")){
				for(int i=0;i<eLength;i++){
					result=result+1;
				}
				for(int i=0;i<sLength;i++){
					result=result+0;
				}
				return 1+signresult+result;
			}
			
			if(expresult.contains("1")==false)
				result=0+signresult+expresult+facresult;
			else
				result=0+signresult+expresult+facresult.substring(2);
			
			
			if(result.length()<2+eLength+sLength){
				int length=Integer.valueOf(sLength)-facresult.substring(2).length();
				
				for(int  i=0;i<length;i++)
					result=result+0;
			}
			if(result.length()>2+eLength+sLength){
				
				
				result=result.substring(0,2+eLength+sLength);
			}
			
		}
		
		else if(judge.equals("10")){
			expresult=signedAddition(expresult,0+integerRepresentation(String.valueOf("1"),eLength),1+eLength).substring(2,2+eLength);
			
			if(expresult.contains("0")==false){
				
				for(int i=0;i<eLength;i++){
					result=result+1;
				}
				for(int i=0;i<sLength;i++){
					result=result+0;
				}
				return 1+signresult+result;
			}
			else if(expresult.substring(0,1).equals("0")&&exp1.substring(0,1).equals("1")&&exp2.substring(0,1).equals("1")){
				for(int i=0;i<eLength;i++){
					result=result+1;
				}
				for(int i=0;i<sLength;i++){
					result=result+0;
				}
				return 1+signresult+result;
			}
			
			if(expresult.contains("1")==false)
				result=0+signresult+expresult+facresult;
			else
				result=0+signresult+expresult+facresult.substring(1);
			if(result.length()<2+eLength+sLength){
				int length=sLength-facresult.substring(1).length();
				
				for(int  i=0;i<length;i++)
					result=result+0;
			}
			
		}
		else if(judge.equals("11")){
			expresult=signedAddition(0+expresult,0+integerRepresentation(String.valueOf("1"),eLength),1+eLength).substring(3,3+eLength);
			System.out.println(expresult);
			
			if(expresult.contains("0")==false){
				
				for(int i=0;i<eLength;i++){
					result=result+1;
				}
				for(int i=0;i<sLength;i++){
					result=result+0;
				}
				return 1+signresult+result;
			}
			else if(expresult.substring(0,1).equals("0")&&exp1.substring(0,1).equals("1")&&exp2.substring(0,1).equals("1")){
				for(int i=0;i<eLength;i++){
					result=result+1;
				}
				for(int i=0;i<sLength;i++){
					result=result+0;
				}
				return 1+signresult+result;
			}
			
			if(expresult.contains("1")==false)
				result=0+signresult+expresult+facresult;
			else
				result=0+signresult+expresult+facresult.substring(1);
			
			if(result.length()<2+eLength+sLength){
				int length=sLength-facresult.substring(1).length();
				
				for(int  i=0;i<length;i++)
					result=result+0;
			}
		
			
		}
		
		//溢出判断
				
					
		// TODO YOUR CODE HERE.
		return result;
	}
	/**
	 * 普通乘法
	 * 
	 */
	public String Multiplication(String operand1, String operand2, int length) {
		String result = "";
		String saveQ = "";
		String saveR = "";
		while(operand1.length()<length){
			operand1="0"+operand1;
		}
		while(operand2.length()<length){
			operand2="0"+operand2;
		}
		for (int i = 0; i < length; i++) {
			saveQ=0+saveQ;
		}
		saveR = operand2;
		result = saveQ + saveR;
		for (int i = 0; i < operand2.length(); i++) {
			if ((operand2.charAt(operand2.length() - i - 1)) == '0') {
				result = logRightShift(result, 1);
				saveQ = result.substring(0, length);
			} else if ((operand2.charAt(operand2.length() - i - 1)) == '1') {
				saveQ = integerAddition(saveQ, operand1, length).substring(1);
				result = saveQ + result.substring(length);
				result = logRightShift(result, 1);
				saveQ = result.substring(0, length);
			}
		} 
		return result.substring(0, length);

	}
	
	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		
		
		
		String sign1=operand1.substring(0,1);
		String sign2=operand2.substring(0,1);
		
		String exp1=operand1.substring(1,1+eLength);
		String exp2=operand2.substring(1,1+eLength);
		
		String fac1=operand1.substring(1+eLength);
		String fac2=operand2.substring(1+eLength);
		
		String result="";
		String signresult="";
		
		
		if(sign1.equals(sign2))
			signresult="0";
		
		else
			signresult="1";
		
		
		fac1=1+fac1;
		fac2=1+fac2;
		
		System.out.println(exp1+" "+exp2);
		System.out.println(fac1+" "+fac2);
		
		if(operand2.substring(1).contains("1")==false){
			result=0+signresult+result;
			for(int i=0;i<eLength;i++)
				result=result+1;
			for(int i=0;i<sLength;i++)
				result=result+0;
			return result;
		}
		if(operand1.substring(1).contains("1")==false)
			return 0+signresult+operand1.substring(1);
		if(exp1.contains("0")==false)
			return 1+signresult+operand1.substring(1);
		if(exp2.contains("0")==false){
			for(int i=0;i<sLength+eLength;i++)
				result=result+0;
			return 0+signresult+result;
		}
			 
		
		
		String facresult=Division(fac1,fac2,1+sLength);
		if(facresult.length()>sLength)
			facresult=facresult.substring(0,sLength);
		
		String judge=facresult.substring(0,1);
		System.out.println(facresult);
		
		String tempexp2=negation(exp2);
		tempexp2=oneAdder(tempexp2).substring(1);
		
		String expresult=signedAddition(0+exp1,0+tempexp2,1+eLength).substring(2);
		
		System.out.println(expresult);
		
		int temp=(int)Math.pow(2, eLength-1)-1;
		
		String plus=integerRepresentation(String.valueOf(temp),eLength);		
		System.out.println(plus);
		
		expresult=signedAddition(0+expresult.substring(1),0+plus,1+eLength).substring(3);
		
		System.out.println(expresult);
		
		//溢出判断
		if(expresult.contains("0")==false){
					
			for(int i=0;i<eLength;i++){
				result=result+1;
			}
			for(int i=0;i<sLength;i++){
				result=result+0;
			}
			return 1+signresult+result;
		}
		else if(exp1.substring(0,1).equals("1")&&expresult.substring(0,1).equals("0")&&exp2.substring(0,1).equals("0")){
			for(int i=0;i<eLength;i++){
				result=result+1;
			}
			for(int i=0;i<sLength;i++){
				result=result+0;
			}
			return 1+signresult+result;
		}
		
		if(judge.equals("0")){
		
			expresult=signedAddition(expresult,0+integerRepresentation(String.valueOf("-1"),eLength),1+eLength).substring(2,2+eLength);
			result=0+signresult+expresult+facresult.substring(2);
			
			if(result.length()<2+eLength+sLength){
				int length=2+eLength+sLength-result.length();
				
				for(int  i=0;i<length;i++)
					result=result+0;
			}
			return result;
		}
		
		else if(judge.equals("1")){
			result=0+signresult+expresult+facresult.substring(1);
			if(result.length()<2+eLength+sLength){
				int length=2+eLength+sLength-result.length();
				
				for(int  i=0;i<length;i++)
					result=result+0;
			}
			return result;
		}
		
		
		
		return null;
	}
	
	/**
	 * 原码除法，用于浮点数除法;
	 */
	public String Division(String operand1, String operand2, int length) {
		//3.3.7定点原码小数除法 
		String result = "";
		String savrR = "";
		String saveQ = "";
		while (operand1.length() < length) {
			operand1 = "0" + operand1;
		}
		while (operand2.length() < length) {
			operand2 = "0" + operand2;
		}
		for (int i = 0; i < length; i++) {
			saveQ += "0";
		}
		savrR = operand1;
		result = savrR + saveQ;
		
		for (int i = 0; i < savrR.length(); i++) {
			int a = Integer.parseInt(savrR);
			int b = Integer.parseInt(operand2);
			if (a < b) {
				result = leftShift(result, 1);
				savrR = result.substring(0, length);
			} else {		
				String temp=oneAdder(operand2).substring(1);
				savrR = integerAddition(0+savrR,0+temp,1+ length).substring(2);
				result = savrR + result.substring(length);
				result = result.substring(1) + "1";
				savrR = result.substring(0, length);
			}
		}
		return result.substring(length);

	}
}
