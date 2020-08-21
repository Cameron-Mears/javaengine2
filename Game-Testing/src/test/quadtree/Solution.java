package test.quadtree;

class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) 
    {
    	int[] newArray = new int[nums1.length + nums2.length];
    	
    	int nums1I=0;
    	int nums2I=0;
    	int n1=0, n2=0;
    	int size = newArray.length;
    	int medstart = 0;
    	boolean isEven = size % 2 == 0;
    	if (isEven)
    	{
    		medstart = size/2;
    	}
    	else
    	{
    		medstart = Math.floorDiv(size, 2);
    	}
    	int i = 0;
    	nums2I=0;
    	nums1I=0;
    	
    	if (nums1.length > 0) n1 = nums1[0];
    	if (nums2.length > 0) n2 = nums2[0];
    	
    	while(true)
    	{
    		if (nums1I >= nums1.length)
    		{
    			while (i < size)
    			{
    				newArray[i]=n2;
    				i++;
    				nums2I++;
    				if (nums2I >= nums2.length) break;
    				n2 = nums2[nums2I];
    				
    			}
    			break;
    		}
    		
    		if (nums2I >= nums2.length)
    		{
    			while (i < size)
    			{
    				newArray[i]=n1;
    				i++;
    				nums1I++;
    				if (nums1I >= nums1.length) break;
    				n1 = nums1[nums1I];
    				
    			}
    			break;
    		}
    		
    		n1 = nums1[nums1I];
    		n2 = nums2[nums2I];
    		
    		if (n1 < n2)
    		{
    			while (n1 < n2)
    			{
    				newArray[i]=n1;
    				i++;
    				nums1I++;
    				if (nums1I >= nums1.length) break;
    				n1 = nums1[nums1I];
    			}
    		}
    		if (n2 <= n1)
    		{
    			while (n2 <= n1)
    			{
    				newArray[i]=n2;
    				i++;
    				nums2I++;
    				if (nums2I >= nums2.length) break;
    				n2 = nums2[nums2I];
    			}
    		}
    			
    		
		}
    	
    	
    	if (isEven)
    	{
    		return (newArray[medstart-1]+newArray[medstart])/2d;
    	}
    	else
    	{
    		return newArray[medstart];
    	}
    	
    }
    
    
    public static void main(String[] args)
    {
    	int[] test = {
    		
    	};
    	int[] test2 = {
        		1
        	};
    	System.out.println(new Solution().findMedianSortedArrays(test, test2));
    	
    }
}