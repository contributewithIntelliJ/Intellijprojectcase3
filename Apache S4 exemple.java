public class HelloPE extends ProcessingElement {
	
	private eventCount = 0;
	
	public void processEvent(Event wordEvent)
	{
		eventCount ++;
	} ;
	
	public void output()
	{
		System.out.println("le mot : " + wordEvent.get("name") + " a pour fréquence : " + eventCount;
	} ;
}

public class WordCountApp extends App {
		
	@Override
	protected void onStart() {
	}
	
	@Override
	protected void onInit() {
	    // Création des PE1
	    PE1 pe1 = createPE(PE1.class);
	    //..............................//
	}
