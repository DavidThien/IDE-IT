package temp;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import evaluators.Evaluator;

/**
 * This is a temporary class that we will use to start the document change tracker.
 * At the moment, since we do not have our plugin automatically beginning tracking
 * when it is first loaded, we need this class to handle when the user clicks the icon
 * in the toolbar to start the tracking (this class is defined in the plugin.xml file
 * as the class that contains action code for that toolbar button)
 */
public class TrackerStarterTemp extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Dev notes: (from John)
		// I have this returning null because I'm not sure how to modify the .xml to avoid calling this file
		// I use the Activator class to set up everything in our plugin. 
		// Any changes and improvements to how this starts up are very welcome
		
		return null;
	}
}
