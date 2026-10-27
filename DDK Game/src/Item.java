
public class Item {
	private String name;
	private String description;
	private int count;
	private String type;
	
	
	public Item (String n, String d, int c)  {
	
		this.name = n;
		this.description = d;
		this.count = c;
		
		
	//Instance Variables 
	}
    public Item(String string, String string2) {
    	
	}
	public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }
    
    public String getType() {
    	return type;
    }
    
    //Setters
    public void setCount(int count) {
    	this.count = count;
    }
    
    public void setType(String type) {
    	this.type = type;
    }
     
    public void countItem(int citem) {
    	this.count += citem;
    	if (this.count < 0) {
    		this.count = 0;
    	}
    }
   
    }
    
    


