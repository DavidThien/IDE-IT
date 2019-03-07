package main.evaluators;

import java.util.Iterator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;

import main.interfaces.FeatureID;

public class AddImportEvaluator extends FeatureEvaluator {

	private boolean unresolvedVariablesExist;
	private boolean lineHadImportStatementAlready;

	/**
	 * Construct an AddImportEvaluator
	 */
	public AddImportEvaluator(IDocument document) {
		this.featureID = FeatureID.ADD_IMPORT_FEATURE_ID;
		this.lineHadImportStatementAlready = false;
		this.unresolvedVariablesExist = false;
		this.document = document;
	}

	/**
	 * Checks whether a document change event is occurring on a line that did
	 * not have an import statement previously
	 * @param event
	 * @return false
	 */
	@Override
	public boolean evaluateDocumentBeforeChange(DocumentEvent event) {
		try {

			// If the line before the change event was an import statement,
			// update the boolean flag to reflect that
			int line = document.getLineOfOffset(event.getOffset());
			this.lineHadImportStatementAlready = lineIsAnImportStatement(line);
		} catch (BadLocationException e) {
		}
		return false;
	}

	/**
	 * Checks whether a document change event is occurring on a line that did
	 * not have an import statement previously
	 * @param event
	 * @return false
	 */
	@Override
	public boolean evaluateDocumentChanges(DocumentEvent event) {
		try {

			// If the line before the change was not an import statement, but the
			// line after the change is, and there are unresolved variables, return true
			int line = document.getLineOfOffset(event.getOffset());
			return this.unresolvedVariablesExist &&
					!this.lineHadImportStatementAlready && lineIsAnImportStatement(line);
		} catch (BadLocationException e) {
		}
		return false;
	}

	/**
	 * Checks if the given line begins with an import statement
	 * @param line
	 * @return
	 */
	public boolean lineIsAnImportStatement(int line) {
		try {

			int startOffset = document.getLineOffset(line);
			int length = document.getLineLength(line);

			// Use regex to remove only the leading white space from the line,
			// then check if the line starts with "import "
			String lineContents = document.get(startOffset, length);
			return lineContents.replaceAll("^\\s+", "").startsWith("import ");
		} catch (BadLocationException e) {
		}
		return false;
	}

	/**
	 * Updates the boolean flag of this AddImportEvaluator that tracks whether
	 * there exist any unresolved types in the document. This returns false
	 * always, as a change to the annotation model should not by itself trigger
	 * a notification to the frontend.
	 * @return boolean false
	 */
	@Override
	public boolean evaluateAnnotationModelChanges(IAnnotationModel model) {
		Iterator<Annotation> it = model.getAnnotationIterator();

		// Iterate through all annotations
		while (it.hasNext()) {
			Annotation current = it.next();

			// If the annotation is valid and represents an unresolved variable,
			// update the flag for unresolved variables existing
			if (current.getText() != null && current.getText().endsWith("cannot be resolved to a type") &&
					!current.isMarkedDeleted()) {
				this.unresolvedVariablesExist = true;
				return false;
			}
		}

		// There are no unresolved variables in the document
		this.unresolvedVariablesExist = false;
		return false;
	}

}
