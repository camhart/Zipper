package zipper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Zipper extends JFrame {
	
	JButton addFiles, createZip, clearFiles;
	
	JTable table;
	DefaultTableModel tableModel;
	
	JFileChooser fileChooser;
	
	String startDir = "";

	private Zipper self;
	
	public Zipper() {
		
		self = this;
		
		this.setTitle("Zipper 0.0");
		
		this.getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500,  200);
		
		fileChooser = new JFileChooser("");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(true);
		
		table = new JTable();
		tableModel = new DefaultTableModel(new Object[]{"File", "Entry"}, 0);
		
		table.setModel(tableModel);
		
		JPanel buttonBar = new JPanel();
		
		addFiles = new JButton("Add file");
		
		addFiles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choice = fileChooser.showOpenDialog(self);
				if(choice == JFileChooser.APPROVE_OPTION) {				
					for(File file : fileChooser.getSelectedFiles()) {
						startDir = file.getParentFile().getAbsolutePath();
						handleDirectory(file);
					}
				}
			}
		});
		
		clearFiles = new JButton("Clear all files");
		clearFiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(int c  = 0; c < tableModel.getRowCount(); c++) {
					tableModel.removeRow(0);
				}
			}
		});
		
		createZip = new JButton("Create zip");
		
		buttonBar.add(addFiles);
		buttonBar.add(clearFiles);
		buttonBar.add(createZip);
		
		createZip.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addFiles.setEnabled(false);
				clearFiles.setEnabled(false);
				createZip.setEnabled(false);
				
				String output = JOptionPane.showInputDialog(self, "What do you want to name your zip?");
				
				File file = new File(output);
				if(!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				ZipWorker zipWorker = null;
				try {
					zipWorker = new ZipWorker(file);
					for(int c = 0; c < tableModel.getRowCount(); c++) {
						String filePath = (String) tableModel.getValueAt(c, 0);
						String entryPath = (String) tableModel.getValueAt(c, 1);
						zipWorker.addFile(new File(filePath), entryPath);
					}
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					if(zipWorker != null)
						try {
							zipWorker.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}				
				addFiles.setEnabled(true);
				clearFiles.setEnabled(true);
				createZip.setEnabled(true);
				JOptionPane.showMessageDialog(self, "Done");
			}
			
		});
		
		
		this.add(buttonBar, BorderLayout.NORTH);
		
				
		this.add(table,  BorderLayout.CENTER);
//		this.add(fileList, BorderLayout.CENTER);
		this.setVisible(true);		
	}
	
	public void handleDirectory(File file) {
		if(!file.isDirectory()) {
			addEntry(file);
			return;
		}
		
		File[] list = file.listFiles();
		if(list != null && list.length > 0) {
			for(File f : list) {
				if(file.isDirectory()) {
					handleDirectory(f);
				} else {
					addEntry(f);
				}
			}
		}
	}
	
	public void addEntry(File file) {
		String entry = null;
		String astring = file.getParentFile().getAbsolutePath().replace(startDir,  "");
		astring = astring.substring(1,  astring.length());
		entry = JOptionPane.showInputDialog(self, "Please set an entry for file:\n" + file.getAbsolutePath());
		if(entry == null)
			entry = "";
		entry = astring + "\\" + entry;
		String[] row = {file.getAbsolutePath(), entry};
		tableModel.addRow(row);		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Zipper zip = new Zipper();
				zip.showGui();
			}
			
		});
	}

	protected void showGui() {
		this.setVisible(true);
		
	}
}
