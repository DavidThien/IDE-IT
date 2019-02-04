package plugin_test.handlers;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

public class KeyPressHandlerOld implements IDocumentListener {
	
	// Provides an example of tracking key presses
	// Only tracks key presses that result in changes to the document
	
	int prevOffset = -2;
	String prevInsert = null;

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
//		System.out.println("ABOUT TO BE CHANGED");
//		System.out.println(event.getText());
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		// Can use IDocument.getDocument() to get the contents of the entire document
		IDocument doc = event.getDocument();
		
		// Testing out a simple evaluation to see if two slashes were placed in a row
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
