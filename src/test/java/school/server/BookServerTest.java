package school.server;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.junit.Test;

public class BookServerTest {
/*
	@Test
	public void testStartStop() {
		BookServer bookServer = new BookServer();
		new Thread(bookServer).start();
		bookServer.stop();
	}

	@Test
	public void test() {
		BookServer bookServer = new BookServer();
		new Thread(bookServer).start();
		try (Socket s = new Socket("localhost", 1443);) {
			Scanner scan1 = new Scanner(s.getInputStream());
			Scanner scan2 = new Scanner(new File("target\\classes\\Java.html"));
			
			PrintStream out = new PrintStream(s.getOutputStream());
			out.println("book:Java");
			
			while(scan1.hasNextLine())
				assertEquals(scan2.nextLine(), scan1.nextLine());
			scan1.close();
			scan2.close();
		} catch (UnknownHostException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}finally{
			bookServer.stop();
			
		}
	}
*/
}
