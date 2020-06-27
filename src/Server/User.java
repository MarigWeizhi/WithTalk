package Server;

class User {
	protected String name = "";
	protected String ip;
	protected int port;
	protected int id = 0;
	protected String passWord = "";
	public boolean isOnline = false;
		
	public User(String name,String passWord) {
		this.name = name;
		this.passWord = passWord;
		id = ++Server.Forward.number;
	}
	
}
