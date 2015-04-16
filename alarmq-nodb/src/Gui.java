import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Gui extends JPanel implements PropertyChangeListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem menuItem;
	private JProgressBar progressBar;
	private JFileChooser fc;
	private Main task;
	private JPanel pane;

	
	public Gui() {
		
		//Create a file chooser
		fc = new JFileChooser();
		
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        
        pane = new JPanel();
        pane.add(progressBar);
               
        add(pane);
        
	}
	
	public void actionPerformed(ActionEvent arg0) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma separated values", "csv");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(Gui.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
	
			task = new Main(file, pane);
			task.addPropertyChangeListener(this);
			task.execute();
		}
	}	



	


	public JMenuBar createMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");

		menuItem = new JMenuItem("Open CSV file", KeyEvent.VK_F);

		menu.add(menuItem);
		menuBar.add(menu);

		menuItem.addActionListener(this);

		return menuBar;

	}		

	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
		case "progress":
		{
			int progress = (Integer) evt.getNewValue();
			if (progress > 100)
				progress = 100;
			System.out.println("Progress is " + progress);
			progressBar.setValue(progress);
		} 
		break;
		}
	}



	private static void createAndShowGUI() {

		JFrame frame = new JFrame("Alarm header statistics");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		Gui newContentPane = new Gui();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		frame.setJMenuBar(newContentPane.createMenuBar());
		frame.setSize(300, 200);
		
		frame.pack();
		frame.setVisible(true);

		newContentPane.setLayout(new FlowLayout());

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
