package com.infoworld;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
	GitStream gitStream = new GitStream();
	//gitStream.main(args);
	//GitSocket gitSocket = new GitSocket();
	//gitSocket.main(args);
	CoinStream coinStream = new CoinStream();
	coinStream.main(args);
    }
}
