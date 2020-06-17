package engine.util.string;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class StringHexConverter 
{
	public static String toHex(String str)
	{	
		/*
		byte[] data = str.getBytes(Charset.forName("US-ASCII"));
		String ret = "";
		for (int index = 0; index < data.length; index++) 
		{
			ret += toHexFromNum(unsignedToBytes(data[index]));
		}
		return ret;
		*/
		
		StringBuffer sb = new StringBuffer();
	      //Converting string to character array
	      char ch[] = str.toCharArray();
	      for(int i = 0; i < ch.length; i++) {
	         String hexString = Integer.toHexString(ch[i]);
	         sb.append(hexString);
	      }
	      return sb.toString();
	}
	
	public static String hexToAscii(String hexStr) 
	{
	    StringBuilder output = new StringBuilder("");
	     
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i+2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	     
	    return output.toString();
	}
	
	public static String fromHex(String str)
	{
		/*
		byte[] data = new byte[str.length()/2];
		if (str.length() % 2 != 0) return null;
		
		
		for (int index = 0; index < str.length(); index += 2) 
		{				
			int num = ((toNumFromHex(str.charAt(index)) * 16) + toNumFromHex(str.charAt(index + 1)));			
			data[index/2] = (byte) num;
		
		}
	
		
		return new String(data, Charset.forName("US-ASCII"));
		*/
		
		byte[] raw = new byte[str.length()/2];
		
		for (int index = 0; index < str.length(); index += 2) 
		{
			String s = new String();
			s += str.charAt(index) + str.charAt(index + 1);
			int x = Integer.parseInt(s, 16);
			raw[index/2] = getByteFromInt(x);
		}
		
		return new String(raw);
	}
	
	
	public static byte[] toByteArray(String hex) //must be ASCII
	{
		byte[] data = new byte[hex.length()/2];
		if (hex.length() % 2 != 0) return null;
		else
		{
			for (int index = 0; index < hex.length(); index += 2) 
			{				
				int num = ((toNumFromHex(hex.charAt(index)) * 16) + toNumFromHex(hex.charAt(index + 1)));			
				
				data[index/2] = getByteFromInt(num);
			
			}
		}
		
		return data;
	}
	
	public static byte getByteFromInt(int x)
	{
		byte[] bytes = ByteBuffer.allocate(4).putInt(x).array();

		ByteOrder endianness = ByteOrder.nativeOrder();
		
		if (endianness.equals(ByteOrder.BIG_ENDIAN))
		{
			return bytes[0];
		}
		else
		{
			return bytes[3];
		}
	}
	
	public static int unsignedToBytes(byte b) 
	{
	    return b & 0xFF;
	  }
	
	
	public static String toHexFromNum(int b)
	{
		String ret = "";
		int toAdd = (int) (b % 16);
		int multiple = (b-toAdd)/16;
		
		if (multiple >= 10)
		{
			multiple -= 10;
			ret += (int) ('a' + multiple);
		}
		else ret += Integer.toString(multiple);
		if (toAdd >= 10)
		{
			toAdd -= 10;
			ret += (int) ('a' + toAdd);
		}
		else ret += Integer.toString(toAdd);
		return ret;
		
	}
	
	
	public static int toNumFromHex(char c)
	{
		if (c >= '0' && c <= '9')
		{
			return (int)(c - '0');
		}
		else
		{
			c = Character.toLowerCase(c);
			
			int ret = 10;
			return (ret + (int)(c - 'a'));
		}
		
	}
	
	
	private static int fromByteArray(byte[] bytes)
	{
	     return ((bytes[3] & 0xFF) << 24) | 
	             ((bytes[2] & 0xFF) << 16) | 
	             ((bytes[1] & 0xFF) << 8 ) | 
	             ((bytes[0] & 0xFF) << 0 );
	 }
}
