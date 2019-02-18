package evaluators;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.util.IAnnotation;
import org.eclipse.jdt.core.util.IAnnotationComponent;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.ui.texteditor.ITextEditor;

import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;




/**
 * Evaluator function to determine if there are any unused import statements in the current document. If any unused import
 * statements are found, then evaluate will return true
 *
 */
public class RemoveImportEvaluator {
	
	// TODO
	// Dev notes: by John
	// Need to test when renaming files
	// This relies on matching up the listener to a file name
	// Unsure of behavior when a file is renamed with an open window. It probably breaks
	
	
	ITextEditor editor;
	IResource docResource;
	
	/**
	 * Constructor
	 * @param docName the name of the document this evaluator is attached to
	 */
	public RemoveImportEvaluator(ITextEditor editor) {
		this.editor = editor;
		
		// docResource is currently unused
		//this.docResource = findResource();
	}
	
	/**
	 * Checks the document that matches the docName to see if there are any unused import statements
	 * 
	 * @param event
	 * @return true if there are unused import statements in the document, false otherwise
	 */
	public boolean evaluate(DocumentEvent event) {
		
		//TODO:
		// This doesn't need to check every DocumentEvent, it only needs to check each time Eclipse is saved / compiled
		// The markers only update upon saving the file
		// We don't use the DocumentEvent at all
		// It's possible to create a listener for the workspace, and maybe we can detect changes off of that?
		
		// There has to be a way to only get Markers off certain resources / files
		// Right now we grab all markers in for the entire project, then filter by ID, then by filename
		
		
		Iterator it = editor.getDocumentProvider().getAnnotationModel(editor.getEditorInput()).getAnnotationIterator();
		
//		System.out.println("Iterator things:");
		while (it.hasNext()) {
			Annotation current = (Annotation)it.next();
			
			if (current instanceof SimpleMarkerAnnotation) {
				IMarker mark = ((SimpleMarkerAnnotation) current).getMarker();
				System.out.println("matching SimpleMarkerAnnotation");
				
				try {
					System.out.println("Attribute ID: " + mark.getAttribute("id"));
					if ((int)mark.getAttribute("id") == IProblem.UnusedImport) {
						return true;
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		
//			
//			System.out.println("Class: " + current.getClass().toString());
//			System.out.println("Annotation Text:" + current.getText());
//			System.out.println("Annotation Type: " + current.getType());
//			System.out.println(current.toString());
		}
		
		
		
//		try {
//			// Grab the workspace, then grab all markers in the workspace
//			IWorkspace workspace = ResourcesPlugin.getWorkspace();
//			IMarker markers[] = workspace.getRoot().findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
//			
//			for (IMarker marker : markers) {
//				Map<String, Object> markerAttributes = marker.getAttributes();
//				IResource resource = marker.getResource();
//				
//				// DEBUG
////				System.out.println("Resource name: " + resource.getName());
////				System.out.println("Doc name: " + docTitle);
//				
//				// DEBUG:
////				System.out.println("");
////				for(Entry<String, Object> entry : markerAttributes.entrySet()) {
////					System.out.println("Key: " + entry.getKey() + "     Value: " + entry.getValue().toString());
////				}
//				
//				
//				// All unused imports will have IProblem.UnusedImport value under the "id" key
////				System.out.println("MarkerID: " + markerAttributes.get("id"));
//				if (resource.getName().equals(docName) && (int)markerAttributes.get("id") == IProblem.UnusedImport) {
//					return true;
//				}
//
//				
//			}
//		} catch (CoreException e) {
//			System.out.println("Exception happened in RemoveImportEvaluator");
//		}
	
		return false;
	}
	
	
	
	// NOTES:
	// Below is currently not working
	// I'm trying to traverse the project and subfolders to find a matching resource that's a file and matches
	// the name of the docName provided. 
	// This seems cumbersome and not great - It may be easier to just keep iterating through the markers
	// Even if we get the resource working properly - we may have to deal with name change shenanigans
	
	
	/**
	 * Recursively calls itself to traverse the project to find the resource associated with the document name provided
	 * @return
	 */
//	private IResource findResource() {
//
//		try {
//			IWorkspace workspace = ResourcesPlugin.getWorkspace();
//			IResource result = findResourceHelper(workspace.getRoot().members());
//			
//			if (result != null) {
//				return result;
//			} else {
//				System.out.println("Mismatch of IResource name and file name. Something went wrong");
//				return null;
//			}
//		} catch (CoreException e) {
//			System.out.println("Core Exception in RemoveImportEvaluato.findResource");
//		}
//		return null;
//		
//	}
//	
//	private IResource findResourceHelper(IResource res[]) {
//		
//		if (res.length == 0) {
//			return null;
//		}
//		
//		for (IResource r : res) {
//			// If it's a file, check the name and return it
//			if (r.getType() == IFile.FILE) {
//				// If a file, then check the name. If it matches, return
//				if (((IFile)r).getName().equals(docName)) {
//					return r;
//				}
//			}
//			// If it's a folder and we haven't found the resource yet, keep traversing
//			if (r.getType() == IFolder.FOLDER) {
//				try {
//					return findResourceHelper(((IFolder)r).members());
//				} catch (CoreException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}	
//		}
//		
//		return null;
//	}
//	
//		
		
}
