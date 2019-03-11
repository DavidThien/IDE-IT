package test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.GetterSetterEvaluator;

public class GetterSetterEvaluatorTest {

	/**
	 * Constants used as mock data. INITIAL_CONTENT is the content of the document with a declared variable and an empty class header
	 * Line 1 = class declaration
	 * Line 2 = private int x
	 * Line 3= private String name;
	 * Line 4 = blank
	 * Line 5 = blank
	 * Line 6 = class end
	 */
	private static final String INITIAL_CONTENT = "public class MockClass { \n" +
							"\tprivate int x;\n" +
							"\tprivate String testName;\n" +
							"\n" +
							"\n" +
							"}";

	private GetterSetterEvaluator eval;
	private IDocument doc;
	private final String INT_VAR_NAME = "x";
	private final String STRING_VAR_NAME = "testName";

	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		this.doc = new Document(INITIAL_CONTENT);
		this.eval = new GetterSetterEvaluator(doc);
	}

	@Test
	public void oneCharacterAtATimeGetX() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public int get".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertTrue(mockUserInput(INT_VAR_NAME, offset++));
	    } catch (BadLocationException e) {}
	}

	@Test
	public void copyPasteGetAndChangeName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public int getY()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();
		// Backspace to remove the "Y()"
		mockUserSingleBackspace(offset--);
		mockUserSingleBackspace(offset--);
		mockUserSingleBackspace(offset--);
		// Now we add the name of the variable
		assertTrue(mockUserInput(INT_VAR_NAME, offset));
	    } catch (BadLocationException e) {}
	}

	@Test
	public void copyPasteGetAndPastVarName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public string getY()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();
		// Backspace to remove the "Y()"
		mockUserSingleBackspace(offset--);
		mockUserSingleBackspace(offset--);
		mockUserSingleBackspace(offset--);
		// Now we add the name of the variable
		assertTrue(mockUserInput(STRING_VAR_NAME, offset));
	    } catch (BadLocationException e) {}
	}


	/**
	 * Mocks the user typing the given string into the document at
	 * the given offset. Evaluates the result to see if the feature
	 * evaluation has been triggered
	 * @param input
	 * @param offset
	 * @throws BadLocationException
	 */
	private boolean mockUserInput(String input, int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset, input.length(), input);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 0, input);
		return eval.evaluateDocumentChanges(event);
	}

	/**
	 * Mock the user typing a backspace in the document at the given offset
	 * @param offset
	 * @return
	 * @throws BadLocationException
	 */
	private boolean mockUserSingleBackspace(int offset) throws BadLocationException {
		DocumentEvent event = new DocumentEvent(doc, offset-1, 1, "");
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset-1, 1, "");
		return eval.evaluateDocumentChanges(event);
	}
}
