public class Person {

	int myAge;			// Age in years
	double myHeight;	// Height in meters
	String myName;		// Person's name

	public Person() {
		myAge = 0;
		myHeight = 0;
		myName = "no name";
	}

	public void printInfo() {
		String response;
		response = "Hello, my name is " + myName;
		response = response + " and I'm " + myAge + " years old!";
		System.out.println(response);
	}

	public void increaseAge(int amount) {
		myAge = myAge + amount;
	}
}