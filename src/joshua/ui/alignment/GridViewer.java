/* This file is part of the Joshua Machine Translation System.
 * 
 * Joshua is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA
 */
package joshua.ui.alignment;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.ObjectInput;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import joshua.corpus.Corpus;
import joshua.corpus.alignment.Alignments;
import joshua.corpus.alignment.mm.MemoryMappedAlignmentGrids;
import joshua.corpus.mm.MemoryMappedCorpusArray;
import joshua.corpus.vocab.Vocabulary;
import joshua.ui.StartupWindow;
import joshua.util.io.BinaryIn;

/**
 * 
 * @author Lane Schwartz
 */
public class GridViewer extends JFrame implements ActionListener {

	/** Logger for this class. */
	private static final Logger logger =
		Logger.getLogger(GridViewer.class.getName());

	private final GridScrollPanel gridScrollPanel;
	
	public GridViewer(GridScrollPanel gridScrollPanel) {
		super("Sentence Alignment");
		
		this.gridScrollPanel = gridScrollPanel;
		
		this.setJMenuBar(new GridViewerMenu(this));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.add(gridScrollPanel);
//		this.setContentPane(gridScrollPanel);

		//Display the window.
		this.pack();
		this.setVisible(true);
	}
	


	public void actionPerformed(ActionEvent e) {
		if (logger.isLoggable(Level.FINER)) logger.finer("Action: " + e.toString());
		
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equalsIgnoreCase("Print")) {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(gridScrollPanel);
			boolean ok = job.printDialog();
			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
					// The job did not successfully complete
					logger.warning("Print job was unsuccessful: " + ex.getLocalizedMessage());
				}
			}
		} else if (actionCommand.equalsIgnoreCase("Exit")) {
			this.setVisible(false);
			System.exit(0);
		}
	}
	
	/**
	 * Constructs a runnable capable of 
	 * initializing and displaying an alignment grid.
	 * 
	 * @param joshDirName
	 * @param sentenceNumber
	 * @return a runnable capable of 
	 *         initializing and displaying an alignment grid.
	 */
	public static Runnable displayGrid(final String joshDirName, final int sentenceNumber) {

		return new Runnable() {
			public void run() {

				try {
					
					String binaryVocabFileName = joshDirName + File.separator + "common.vocab";
					String binarySourceFileName = joshDirName + File.separator + "source.corpus";
					String binaryTargetFileName = joshDirName + File.separator + "target.corpus";
					String binaryAlignmentFileName = joshDirName + File.separator + "alignment.grids";

					logger.fine("Loading vocabulary...");
					Vocabulary commonVocab = new Vocabulary();
					ObjectInput in = BinaryIn.vocabulary(binaryVocabFileName);
					commonVocab.readExternal(in);

					logger.fine("Loading source corpus...");
					Corpus sourceCorpus = new MemoryMappedCorpusArray(commonVocab, binarySourceFileName);

					logger.fine("Loading target corpus...");		
					Corpus targetCorpus = new MemoryMappedCorpusArray(commonVocab, binaryTargetFileName);

					logger.fine("Loading alignment grids...");
					Alignments alignments = new MemoryMappedAlignmentGrids(binaryAlignmentFileName, sourceCorpus, targetCorpus);

					logger.fine("Constructing panel...");
					GridPanel gridPanel = new GridPanel(sourceCorpus, targetCorpus, alignments, sentenceNumber);

					//Create and set up the content pane.
					GridScrollPanel scrollPanel = new GridScrollPanel(gridPanel);
					scrollPanel.setOpaque(true); //content panes must be opaque


					//Create and set up the window.
					new GridViewer(scrollPanel);//new JFrame("Sentence Alignment");
					
					splashScreen.setVisible(false);
					
				} catch (Throwable e) {
					logger.severe("Unable to start program: " + e.getLocalizedMessage());
				}

			} 

		};

	}

	private static StartupWindow splashScreen = new StartupWindow("Alignment Viewer", Color.BLACK, 5);

	public static void main(String[] args) {
		
		String joshDirName = args[0];

		int sentenceNumber = 0;
		if (args.length > 1) sentenceNumber = Integer.valueOf(args[1]) - 1;
		
		// Ask Swing to start the user interface
		javax.swing.SwingUtilities.invokeLater(
				displayGrid(joshDirName, sentenceNumber)
		);


	}
	
}