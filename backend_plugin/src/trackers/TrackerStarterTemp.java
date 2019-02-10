package trackers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * This is a temporary class that we will use to start the document change tracker.
 * At the moment, since we do not have our plugin automatically beginning tracking
 * when it is first loaded, we need this class to handle when the user clicks the icon
 * in the toolbar to start the tracking (this class is defined in the plugin.xml file
 * as the class that contains action code for that toolbar button)
 * 
 * Note: A document must already be open, otherwise this will throw a NullPointerException
 * when trying to access the current active editor window
 */
public class TrackerStarterTemp extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		System.out.println("Starting to listen to key input....");
		
		// Gets the currently selected text editor window
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ITextEditor ite = (ITextEditor)editor;
		
		// Gets the document stored inside that text editor window
	    IDocument doc = ite.getDocumentProvider().getDocument(ite.getEditorInput());
	    
	    // Adds a MyDocumentListener to the document inside the text editor window
	    DocumentChangesTracker docTracker = new DocumentChangesTracker();
	    doc.addDocumentListener(docTracker);
		return null;
	}
}
