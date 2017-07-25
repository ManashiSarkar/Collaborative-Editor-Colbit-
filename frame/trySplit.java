import java.io.*;
public class trySplit
{
	public static void main(String[] args) 
	{
		String s = "$pop$$skgfjsgfj$shmfgmh$sfvhsgvf";

		String a[] = s.split("shm");

		System.out.println(a.length);

		for(String part : a)
			System.out.println(part);
	}
}