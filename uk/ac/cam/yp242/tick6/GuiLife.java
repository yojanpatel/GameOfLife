package uk.ac.cam.yp242.tick6;

import uk.ac.cam.acr31.life.World;
import java.awt.BorderLayout;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import java.util.List;
import java.io.IOException;

public class GuiLife extends JFrame {

	PatternPanel patternPanel;
	ControlPanel controlPanel;
	GamePanel gamePanel;

	public GuiLife() {
		super("GUI Life");
		setSize(640,480);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JComponent optionsPanel = createOptionsPanel();
		add(optionsPanel, BorderLayout.WEST);

		JComponent gamePanel = createGamePanel();
		add(gamePanel, BorderLayout.CENTER);

	}

	private void addBorder(JComponent component, String title) {
		Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border tb = BorderFactory.createTitledBorder(etch,title);
		component.setBorder(tb);
	}

	private JComponent createOptionsPanel() {
		Box result = Box.createVerticalBox();
		result.add(createSourcePanel());
		result.add(createPatternPanel());
		result.add(createControlPanel());
		return result;
	}

	private JComponent createGamePanel() {
		gamePanel = new GamePanel();
		JPanel holder = new JPanel();
		addBorder(holder,Strings.PANEL_GAMEVIEW);
		JPanel result = gamePanel;
		holder.add(result);

		return new JScrollPane(holder);
	}

	private JComponent createSourcePanel() {
		SourcePanel result = new SourcePanel();
		addBorder(result,Strings.PANEL_SOURCE);
		return result; 
	}

	private JComponent createPatternPanel() {
		patternPanel = new PatternPanel();
		JPanel result = patternPanel;
		addBorder(result, Strings.PANEL_PATTERN);
		return result;
	}

 	private JComponent createControlPanel() {
 		controlPanel = new ControlPanel();
 		JPanel result = controlPanel;
 		addBorder(result, Strings.PANEL_CONTROL);
 		return result;
 	}

	public static void main(String[] args) throws PatternFormatException, CommandLineException{
		GuiLife gui = new GuiLife();
		CommandLineOptions c = new CommandLineOptions(args);
		List<Pattern> list;

		try {
			if (c.getSource().startsWith("http://"))
				list = PatternLoader.loadFromURL(c.getSource());
			else
				list = PatternLoader.loadFromDisk(c.getSource());

			if (c.getIndex() == null) {
				int i = 0;
				for (Pattern p : list)
					System.out.println((i++)+" "+p.getName()+" "+p.getAuthor());
			} else {
				Pattern p = list.get(c.getIndex());
				World w = gui.controlPanel.initialiseWorld(p);
				gui.patternPanel.setPatterns(list);
 				gui.gamePanel.display(w);
 				w = w.nextGeneration(0);
 				int userResponse = 0;
 				gui.setVisible(true);
 				while(userResponse != 'q') {
 					gui.gamePanel.display(w);					
					userResponse = System.in.read();
					w = w.nextGeneration(0);
				}
			}	
		}
		catch (PatternFormatException pfe) { System.out.println(pfe.getMessage()); }
		//catch (CommandLineException cle) { System.out.println(cle.getMessage()); }
		catch (NumberFormatException nf) { System.out.println("Error: second argument (index) must be an integer.");}
		catch (ArrayIndexOutOfBoundsException iob) { System.out.println("Error. Array Index out of bounds. Check Input values.");}
		catch (IOException ioe) {}
	}
}