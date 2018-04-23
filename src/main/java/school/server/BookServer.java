package school.server;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Classe main permettant l'exécution d'un serveur réseau donnant accès à des
 * dossiers de cours. Un BookServeur est définit par le fichier de propriété
 * "ListeCours.txt" situé dans le même répertoire que la classe. le fichier de
 * propriété à la forme :
 * <ul>
 * <li>clé : nom du cours</li>
 * <li>valeur : nom du fichier HTML présentant le dossier de cours.</li>
 * <ul>
 * les fichiers HTML se situe dans le même repertoire que la classe.
 * 
 * @author boogaerts
 */
public class BookServer implements Runnable{
	private Properties mapping;

	private boolean stop = false;
	
	private JFrame ecran = new JFrame("Serveur");
	
	public BookServer(){
		JButton close = new JButton("Stop");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				BookServer.this.stop();
			}
		});
		ecran.add(close);
		ecran.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		ecran.setSize(120, 60);
		ecran.setVisible(true);
	}

	/**
	 * Retourne et initialise une instance de <code>Properties</code> à partir
	 * du fichier "ListeCours.txt".
	 * 
	 * @return le mapping vers les fichiers ressources du serveur
	 * @throws IOException
	 *             si la ListeCours ne peut être chargé.
	 */
	public Properties getMapping() throws IOException {
		if (this.mapping == null) {
			this.mapping = new Properties();
			Properties prop = new Properties();
			String libelle, key;
			Enumeration<Object> liste;
			URL url = BookServer.class.getResource("/ListeCours.txt");
			if (url == null)
				throw new IOException();

			prop.load(url.openStream());
			liste = prop.keys();
			while (liste.hasMoreElements()) {
				libelle = (String) liste.nextElement();
				key = libelle.toUpperCase().trim();
				this.mapping.put(key, prop.getProperty(libelle));
			}
		}
		return this.mapping;
	}

	/**
	 * Lance le serveur qui écoute sur le port 1443.
	 * 
	 * @param args
	 *            pas utilisé.
	 */
	public static void main(String[] args) {
		BookServer serveur = new BookServer();	
		new Thread(serveur).start();
	}

	/**
	 * Met le seveur en ecoute.
	 */
	public void run() {
		try (ServerSocket s = getServerSoket();) {

			while (!stop) {
				new BookReader(s.accept(), this.getMapping()).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ecran.dispose();
	}

	public void stop() {
		try (Socket s = getClientSocket();) {
			s.getOutputStream().write("stop".getBytes());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.stop = true;
	}

	private ServerSocket getServerSoket() throws IOException {
		ServerSocket s = new ServerSocket(1443);
		return s;
	}
	
	private Socket getClientSocket() throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 1443);
		return s;
	}

}
