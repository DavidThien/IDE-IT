package PossibleKeyPressHandler;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

public class MyDocumentListener implements IDocumentListener {
	
	int prevOffset = -2;
	String prevInsert = null;

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// Runs first when new insertions are made into a document
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		// Runs second (after documentAboutToBeChanged) when new
		// insertions are made into a document
		
		// Grabs the document that is stored in the current window
		IDocument doc = event.getDocument();
		
		// Testing a way to track if two slashes are entered in a row
		// (work in progress)
		int currentOffset = event.getOffset();
		String currentInsert = event.getText();
		if (this.prevOffset == currentOffset - 1) {
			if (this.prevInsert.equals("/") && currentInsert.equals("/")) {
				System.out.println("Placed two slashes");
			}
		}
		this.prevOffset = currentOffset;
		this.prevInsert = currentInsert;
	}

}
