package test.quadtree;

import java.util.LinkedList;

import engine.util.TimeUtils;

public class CombineGroups 
{
	private static class Group
	{
		int size;
		public Group(int size) {
			this.size = size;
		}
		//other data
	}
	
	private static class JoinedGroup
	{
		LinkedList<Group> groups;
		int size;
		
		public JoinedGroup() {
			groups=new LinkedList<Group>();
		}
		
		void addGroup(Group p)
		{
			groups.add(p);
			size += p.size;
		}
	}
	
	public static void main(String[] args)
	{
		LinkedList<Group> groups = new LinkedList<Group>();
		LinkedList<JoinedGroup> joinedgroups = new LinkedList<JoinedGroup>();
		LinkedList<JoinedGroup> fullGroups = new LinkedList<JoinedGroup>();
		//the groups 
		Group g1 = new Group(1);
		Group g2 = new Group(3);
		Group g3 = new Group(2);
		Group g4 = new Group(1);
		Group g5 = new Group(1);
		Group g6 = new Group(1);
		
		
		groups.add(g1);
		groups.add(g2);
		groups.add(g3);
		groups.add(g4);
		groups.add(g5);
		groups.add(g6);
		fullGroups = slove(groups);
		
		for (JoinedGroup joinedGroup : fullGroups) 
		{
			System.out.println("Group -> " + Integer.toString(joinedGroup.size));
			System.out.print("\t");
			for (Group group : joinedGroup.groups) 
			{
				System.out.print(group.size);
				System.out.print(", ");
			}
			System.out.println();
		}
		System.out.println("RANDOM DATA ----------------------------------");
		
		groups.clear();
		
		for (int i = 0; i < 10000000; i++) 
		{
			int size = (int)((Math.random() * 3)) + 1;
			Group group = new Group(size);
			groups.add(group);
		}
		long now = System.nanoTime();
		fullGroups = slove(groups);
		System.out.println(TimeUtils.nanosToSeconds(System.nanoTime() - now));
		
		/*
		for (JoinedGroup joinedGroup : fullGroups) 
		{
			System.out.println("Group -> " + Integer.toString(joinedGroup.size));
			System.out.print("\t");
			for (Group group : joinedGroup.groups) 
			{
				System.out.print(group.size);
				System.out.print(", ");
			}
			System.out.println();
		}
		*/
		
		
	}
	
	public static LinkedList<JoinedGroup> slove(LinkedList<Group> groups)
	{
		LinkedList<JoinedGroup> joinedgroups = new LinkedList<JoinedGroup>();
		LinkedList<JoinedGroup> fullGroups = new LinkedList<JoinedGroup>();
		int capacity = 6;
		joinedgroups.add(new JoinedGroup());
		for (Group group : groups) 
		{
			int size = joinedgroups.size();
			for (int n = 0; n < size; n++) 
			{
				JoinedGroup joinedGroup = joinedgroups.poll();
				if (joinedGroup.size + group.size <= capacity) //group fits here
				{
					joinedGroup.addGroup(group);
					if (joinedGroup.size == capacity) //group full move
					{
						fullGroups.add(joinedGroup);
						if (joinedgroups.isEmpty()) joinedgroups.add(new JoinedGroup()); //ensure open group
					}
					else
					{
						joinedgroups.add(joinedGroup); //readd to list
					}
				}
				else
				{
					if (n == size-1)//end of loop need new group
					{
						joinedGroup = new JoinedGroup();
						joinedGroup.addGroup(group);
						joinedgroups.add(joinedGroup);
					}
				}
			}
		}
		
		for (JoinedGroup joinedGroup : joinedgroups) {
			if (joinedGroup.size > 0) fullGroups.add(joinedGroup);
		}
	
		return fullGroups;
	}
}
