package TYPES;

public class TYPE_CLASS_VAR_DEC_LIST
{
	public TYPE_CLASS_VAR_DEC head;
	public TYPE_CLASS_VAR_DEC_LIST next;
	
	public TYPE_CLASS_VAR_DEC_LIST(TYPE_CLASS_VAR_DEC head,TYPE_CLASS_VAR_DEC_LIST next)
	{
		this.head = head;
		this.next = next;
	}	
}
