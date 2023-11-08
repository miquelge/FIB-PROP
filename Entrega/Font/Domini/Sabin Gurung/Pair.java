package model;

/** simple data structure that lets us store two values */
public class Pair <T, V> 
{
	private T key;
	private V value;

	/** constructor */
	public Pair (T a, V b)
	{
		key = a;
		value = b;
	}

	/** returns key */
	public T getKey()
	{
		return key;
	}

	/** returns value */
	public V getValue()
	{
		return value;
	}
}
