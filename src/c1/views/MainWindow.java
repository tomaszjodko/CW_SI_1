package c1.views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import c1.common.GeneratorExhaustive;
import c1.common.GeneratorLEM;
import c1.common.GeneratorSekwencyjny;
import c1.common.SystemDecyzyjny;

public class MainWindow extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfFilePath;
	private JButton btnChooseFile;
	private JScrollPane scrollPane;
	private JTextPane tpOutput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setTitle("c1 v2");
		initComponents();
		createEvents();
	}

	// elementy okienka
	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 459, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tfFilePath = new JTextField();
		tfFilePath.setEditable(false);
		tfFilePath.setText("File path...");
		tfFilePath.setColumns(10);

		btnChooseFile = new JButton("Choose File");
		
		scrollPane = new JScrollPane();

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(scrollPane, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(tfFilePath, 336, 336, 336)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnChooseFile)))
					.addGap(4))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfFilePath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnChooseFile))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
		);
		
		tpOutput = new JTextPane();
		tpOutput.setEditable(false);
		scrollPane.setViewportView(tpOutput);
		contentPane.setLayout(gl_contentPane);
	}

	// eventy
	private void createEvents() {
		// klikniecie przycisku
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				// jesli wybierzemy plik
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					// odczyt wybranego pliku
					File selectedFile = fileChooser.getSelectedFile();
					// wyswietlenie sciezki
					tfFilePath.setText(selectedFile.getPath());
					
					SystemDecyzyjny system = new SystemDecyzyjny(selectedFile);
					GeneratorExhaustive exhaustive = new GeneratorExhaustive(system);
					GeneratorSekwencyjny seq = new GeneratorSekwencyjny(system);
					GeneratorLEM lem = new GeneratorLEM(system);
					tpOutput.setText(system.toString() +"\n" + exhaustive.toString() + "\n" + seq.toString() + "\n" + lem.toString());

				}
			}
		});
	}
}


