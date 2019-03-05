package test.java.negatives.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModel;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.AddImportEvaluator;

public class AddImportsNegative {
	
	/**
	 * Constants used as mock data. INITIAL_CONTENT is the content of the document,
	 * ANNOTATION represents an unresolved type annotation, and POSITION is a variable
	 * necessary for adding the annotation to the annotation model
	 */
	private static final String INITIAL_CONTENT = "\n\nLine1\n Line2\n Line3\n";
	private static final Annotation ANNOTATION = new Annotation("org.eclipse.jdt.ui.error", 
			false, "Mock cannot be resolved to a type");
	private static final Position POSITION = new Position(0);
	private static final String MOCK_IMPORT = "import java.util.*;";
	
	private AddImportEvaluator eval;
	private IDocument doc;
	private AnnotationModel am;

	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		this.doc = new Document(INITIAL_CONTENT);
		this.am = new AnnotationModel();
		this.eval = new AddImportEvaluator(doc);
	}

	/**
	 * Tests the user starting a valid import statement by typing
	 * "import " one character at a time when there are no unresolved
	 * variables
	 */
	@Test
	public void noUnresolvedTypesAddImportOneCharacterAtATime() {
		try {
			int offset = 0;
			assertFalse(mockUserInput("i", offset++));
			assertFalse(mockUserInput("m", offset++));
			assertFalse(mockUserInput("p", offset++));
			assertFalse(mockUserInput("o", offset++));
			assertFalse(mockUserInput("r", offset++));
			assertFalse(mockUserInput("t", offset++));
			assertFalse(mockUserInput(" ", offset++));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user starting a valid import statement by typing
	 * "import " one character at a time when there are unresolved
	 * variables
	 */
	@Test
	public void unresolvedTypesAddImportOneCharacterAtATime() {
		try {
			// Add the unresolved type annotation to the annotation model
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);
			int offset = 0;
			assertFalse(mockUserInput("i", offset++));
			assertFalse(mockUserInput("m", offset++));
			assertFalse(mockUserInput("p", offset++));
			assertFalse(mockUserInput("o", offset++));
			assertFalse(mockUserInput("r", offset++));
			assertFalse(mockUserInput("t", offset++));
			assertTrue(mockUserInput(" ", offset++));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user starting a valid import statement by typing
	 * "import " one character at a time, making and correcting
	 * a typo along the way when there are no unresolved variables
	 */
	@Test
	public void noUnresolvedTypesAddImportOneCharacterAtATimeCorrectingTypos() {
		try {
			int offset = 0;
			assertFalse(mockUserInput("i", offset++));
			assertFalse(mockUserInput("m", offset++));
			assertFalse(mockUserInput("o", offset++));
			assertFalse(mockUserInput("r", offset++));
			assertFalse(mockUserSingleBackspace(--offset));
			assertFalse(mockUserSingleBackspace(--offset));
			assertFalse(mockUserInput("p", offset++));
			assertFalse(mockUserInput("o", offset++));
			assertFalse(mockUserInput("r", offset++));
			assertFalse(mockUserInput("t", offset++));
			assertFalse(mockUserInput(" ", offset++));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user starting a valid import statement by typing
	 * "import " one character at a time, making and correcting
	 * a typo along the way when there are no unresolved variables
	 */
	@Test
	public void unresolvedTypesAddImportOneCharacterAtATimeCorrectingTypos() {
		try {
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);
			int offset = 0;
			assertFalse(mockUserInput("i", offset++));
			assertFalse(mockUserInput("m", offset++));
			assertFalse(mockUserInput("o", offset++));
			assertFalse(mockUserInput("r", offset++));
			assertFalse(mockUserSingleBackspace(offset--));
			assertFalse(mockUserSingleBackspace(offset--));
			assertFalse(mockUserInput("p", offset++));
			assertFalse(mockUserInput("o", offset++));
			assertFalse(mockUserInput("r", offset++));
			assertFalse(mockUserInput("t", offset++));
			assertTrue(mockUserInput(" ", offset++));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the user copy-pasting an import statement into the document
	 * when there are no unresolved variables
	 */
	public void noUnresolvedTypesAddImportStatementWithCopyPaste() {
		try {
			int offset = 0;
			assertFalse(mockUserInput(MOCK_IMPORT, offset));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user copy-pasting an import statement into the document
	 * when there are unresolved variables
	 */
	public void unresolvedTypesAddImportStatementWithCopyPaste() {
		try {
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);
			int offset = 0;
			assertTrue(mockUserInput(MOCK_IMPORT, offset));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Mocks the user typing the given string into the document at
	 * the given offset. Evaluates the result to see if the feature
	 * evaluation has been triggered
	 * @param input
	 * @param offset
	 */
	private boolean mockUserInput(String input, int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset, input.length(), input);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 0, input);
		return eval.evaluateDocumentChanges(event);
	}
	
	private boolean mockUserSingleBackspace(int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset-1, 1, "");
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset-1, 1, "");
		return eval.evaluateDocumentChanges(event);
	}
}
