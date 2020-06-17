package engine.util.string;

public class HexToAscii 
{
    public static String asciiToHex(String asciiStr) 
    {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) 
        {
        	String str = Integer.toHexString((int) ch);
        	if (str.length() < 2)
        	{
        		str += "0";
        	}
        	if (str.length() > 2)
        	{
        		System.out.println((int) ch);
        		System.out.println((int)Character.MAX_VALUE);
        	}
        	//if (str.length() != 2) System.out.println("haha lol");
            hex.append(str);
        }

        return hex.toString();
    }

    
    public static String hexToAscii(String hexStr)
    {
    	System.out.println(hexStr.length());
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) 
        {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

}