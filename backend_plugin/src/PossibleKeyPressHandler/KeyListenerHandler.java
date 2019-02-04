package PossibleKeyPressHandler;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class KeyListenerHandler {

	public void startListening() {
		
		// The way we currently have this set up, this runs when the user clicks the button
		// in the toolbar or menu to start the plugin. Ideally we will find a way to start this
		// automatically
		
		// Also, this only activates the document listener for the current active editor window,
		// so if you open a new file to edit, it needs to be reactivated for that window in order
		// to work in it. We need to find some way to detect when the user switches to a different
		// editor, and to make sure this listener handler is activated for that window so that the
		// user doesn't automatically have to activate it each time
		
		System.out.println("Starting to listen to key input....");
		
		// Gets the currently selected text editor window
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ITextEditor ite = (ITextEditor)editor;
		
		// Gets the document stored inside that text editor window
	    IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
	    
	    // Adds a MyDocumentListener to the document inside the text editor window
	    IDocumentListener docListener = new MyDocumentListener();
	    doc.addDocumentListener(docListener);
	}
	
}
