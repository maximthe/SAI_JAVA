package adversarialsearch;

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World");
		State s = new State();
		s.read("data/board.txt");
		System.out.println(s.toString());
		System.out.println(s.agentX[0]);
		System.out.println(s.agentY[0]);
		System.out.println(s.agentX[1]);
		System.out.println(s.agentY[1]);
		s.execute("up");
		s.execute("left");
		s.execute("eat");
		System.out.println(s.agentX[0]);
		System.out.println(s.agentY[0]);
		System.out.println(s.agentX[1]);
		System.out.println(s.agentY[1]);
		System.out.println(s.toString());
		System.out.println(s.food);
		
	}
}
