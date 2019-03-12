package test.java.evaluators;

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

public class AddImportsEvaluatorTest {

     	/** Initial content that will be included in a document */
	private static final String INITIAL_CONTENT = "\n\nLine1\n Line2\n Line3\n";
	/** Mock Annotation that matches with an unresolved variable type annotation */
	private static final Annotation ANNOTATION = new Annotation("org.eclipse.jdt.ui.error",
			false, "MockType cannot be resolved to a type");
	/** Position used with the mock Annotation */
	private static final Position POSITION = new Position(0);
	/** Import statement for quick reference */
	private static final String MOCK_IMPORT = "import java.util.*;";

	/** Evaluator being tested */
	private AddImportEvaluator eval;
	/** Document to be attached to the evaluator */
	private IDocument doc;
	/** AnnotationModel to be attached to the evaluator */
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

			// Type "import " which should not trigger
			for (char c : "import ".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
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
			int offset = 0;

			// Add the unresolved type annotation to the annotation model
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);

			// Type "import ", which should trigger
			for (char c : "import".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
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

			// Type "imort " (forgetting to add the p)
			for (char c : "imort ".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}

			// Erase the last 4 characters to go back to "im"
			for (int i = 0; i < 4; i++) {
				assertFalse(mockUserSingleBackspace(offset--));
			}

			// Retype the last part "port ", which should not trigger
			for (char c : "port ".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
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
			int offset = 0;

			// Add the unresolved type annotation to the annotation model
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);

			// Type "imort " (forgetting to add the p)
			for (char c : "imort ".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}

			// Erase the last 4 characters to go back to "im"
			for (int i = 0; i < 4; i++) {
				assertFalse(mockUserSingleBackspace(offset--));
			}

			// Retype the last part "port ", which should trigger
			for (char c : "port".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
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
	@Test
	public void noUnresolvedTypesAddImportStatementWithCopyPaste() {
		try {
			int offset = 0;

			// Mock an import statement being copy-pasted into the document, which should not trigger
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
	@Test
	public void unresolvedTypesAddImportStatementWithCopyPaste() {
		try {
			int offset = 0;

			// Add the unresolved type annotation to the annotation model
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);

			// Mock an import statement being copy-pasted into the document, which should trigger
			assertTrue(mockUserInput(MOCK_IMPORT, offset));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user declaring a variable called "importantVar" to make sure
	 * that typing "import" does not trigger the evaluator when no unresolved
	 * variables exist
	 */
	@Test
	public void noUnresolvedVariablesImportInVariableName() {
		try {
			int offset = 0;

			// Add a variable declaration named importantVar to the document, which should not trigger
			for (char c : "int importantVar;".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user declaring a variable called "importantVar" to make sure
	 * that typing "import" does not trigger the evaluator when unresolved
	 * variables exist
	 */
	@Test
	public void unresolvedVariablesImportInVariableName() {
		try {
			int offset = 0;

			// Add the unresolved type annotation to the annotation model
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);

			// Add a variable declaration named importantVar to the document, which should not trigger
			for (char c : "int importantVar;".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user typing "import " inside a comment line, rather than as
	 * an import statement at the start of a line, when no unresolved variables
	 * exist
	 */
	@Test
	public void noUnresolvedVariablesImportInComment() {
		try {
			int offset = 0;

			// Add comment line "//import ", which should not trigger
			for (char c : "//import ".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the user typing "import " inside a comment line, rather than as
	 * an import statement at the start of a line, when unresolved variables
	 * exist
	 */
	@Test
	public void unresolvedVariablesImportInComment() {
		try {
			int offset = 0;

			// Add the unresolved type annotation to the annotation model
			am.addAnnotation(ANNOTATION, POSITION);
			eval.evaluateAnnotationModelChanges(am);

			// Add comment line "//import ", which should not trigger
			for (char c : "//import ".toCharArray()) {
				assertFalse(mockUserInput(String.valueOf(c), offset++));
			}
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
