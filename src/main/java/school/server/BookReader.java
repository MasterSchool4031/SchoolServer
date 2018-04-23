package school.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

/**
 * Thread gérant la réponse à une requête du protocole "book".<br>
 * Un <code>BookReader</code> est définit par :
 * <ul>
 * <li>socket : Socket connecté.</li>
 * <li>mapping : Propeties contenant les nom et chemin vers les dossiers de
 * cours.</li>
 * </ul>
 * 
 * @author boogaerts
 * 
 */
public class BookReader extends Thread {

	private Socket socket;

	private Properties mapping;

	/**
	 * Construit un BookReader en initialisant ses propriétés avec les
	 * paramèteres.
	 * 
	 * @param socket
	 * @param mapping
	 */
	public BookReader(Socket socket, Properties mapping) {
		this.socket = socket;
		this.mapping = mapping;
	}

	/**
	 * Retourne, via le socket de la classe, le dossier sous forme HTML. La
	 * requête doit avoir le format :
	 * <code>book:&lt;nom de cours&gt;</code>.<br>
	 * Si le nom de cours ne correspond à aucune clé dans le mapping ou que le
	 * fichier HTML n'est pas présent, un message d'erreur est renvoyé.
	 */
	@Override
	public void run() {
		String[] req;
		String fileName;
		InputStream file;
		URL urlFile = null;

		try (OutputStream out = this.socket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));) {

			String readLine = in.readLine();
			req = readLine == null ? new String[0] : readLine.split(":");

			if (req.length == 2 && req[0].equals("book")) {

				req[1] = req[1].toUpperCase().trim();
				fileName = this.mapping.getProperty(req[1], "Erreur.html");
				urlFile = BookReader.class.getResource(fileName);
				if (urlFile == null) {
					runErreur(out);
				} else {
					file = urlFile.openStream();
					while (file.available() > 0) {
						out.write(file.read());
					}
				}
			} else if (req.length != 1 || !req[0].equals("stop")) {
				runErreur(out);
			} else {
				// System.out.println("arret du serveur");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void runErreur(OutputStream out) {
		PrintStream ps = new PrintStream(out);
		ps.println("aucun Dossier ne répond à la requête");
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
