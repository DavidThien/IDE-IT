package evaluators;

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jface.text.DocumentEvent;

/**
 * Evaluator function to determine if there are any unused import statements in the current document. If any unused import
 * statements are found, then evaluate will return true
 *
 */
public class RemoveImportEvaluator {
	
	/**
	 * Default constructor. This isn't a static class because we need to create separate evaluators for each opened document
	 */
	public RemoveImportEvaluator() {
		
	}
	
	/**
	 * Checks the current document to see if there are any unused import statements
	 * 
	 * @param event
	 * @return true if there are unused import statements in the current document, false otherwise
	 */
	public boolean evaluate(DocumentEvent event) {
		
		//TODO:
		// This doesn't need to check every DocumentEvent, it only needs to check each time Eclipse is saved / compiled
		// The markers only update upon saving the file
		// We don't use the DocumentEvent at all
		// It's possible to create a listener for the workspace, and maybe we can detect changes off of that? 
		
		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IMarker markers[] = workspace.getRoot().findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		
			for (IMarker marker : markers) {
				Map<String, Object> markerAttributes = marker.getAttributes();
				
				// All unused imports will have IProblem.UnusedImport value under the "id" key
				System.out.println("MarkerID: " + marker.getId());
				if (marker.getId() == IProblem.UnusedImport) {
					return true;
				}
				
				if (markerAttributes.containsKey("id")) {
					if ((int)markerAttributes.get("id") == IProblem.UnusedImport) {
						return true;
					}
				}
			}
		} catch (CoreException e) {
			System.out.println("Exception happened in RemoveImportEvaluator");
		}
	
		return false;
	}
		
}
