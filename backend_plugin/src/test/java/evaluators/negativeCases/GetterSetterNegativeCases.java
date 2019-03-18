package test.java.evaluators.negativeCases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.GetterSetterEvaluator;

/**
 * Tests a few basic test cases that the GetterSetter evaluation functions should trigger on and shouldn't trigger on
 */
public class GetterSetterNegativeCases {

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
	/** Evaluator being tested */
	private GetterSetterEvaluator eval;
	/** Document associated with the Evaluator */
	private IDocument doc;
	/** int variable name as a constant for easy reference */
	private final String INT_VAR_NAME = "X";
	/** String variable name as a constant for easy reference */
	private final String STRING_VAR_NAME = "testName";

	/**
	 * Initialize a new document and a new evaluator
	 */
	@Before
	public void runBeforeTests() {
		this.doc = new Document(INITIAL_CONTENT);
		this.eval = new GetterSetterEvaluator(doc);
	}


	/**
	 * Type "public int getY" with a declared variable named x
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeGetWithDifferentVarName() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public int get".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertFalse(mockUserInput("Y", offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "public String get_TestNotName" with a declared variable named testName (different spelling)
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeGet_WithDifferentVarName() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public String get_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out the string
		String truncatedVarName = "TestNotName";
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "public String setTestNamq" with a declared variable named testName (different spelling)
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeSetWithDifferentVarName() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public String set".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out the string except for the last character
		String truncatedVarName = STRING_VAR_NAME.substring(0, STRING_VAR_NAME.length()-1);
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		// Add any character that does not match our variable name
		assertFalse(mockUserInput("q", offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "public int setB" with a declared variable named x
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeSet_WithDifferentVarName() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public int set_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertFalse(mockUserInput("B", offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected int getY" with a declared variable named x
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeGetWithDifferentVarNameProtected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "protected int get".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertFalse(mockUserInput("Y", offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected String get_TestNotName" with a declared variable named testName (different spelling)
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeGet_WithDifferentVarNameProtected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public String get_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out the string
		String truncatedVarName = "TestNotName";
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected String setTestNamq" with a declared variable named testName (different spelling)
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeSetWithDifferentVarNameProtected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "protected String set".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out the string except for the last character
		String truncatedVarName = STRING_VAR_NAME.substring(0, STRING_VAR_NAME.length()-1);
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		// Add any character that does not match our variable name
		assertFalse(mockUserInput("q", offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected int setB" with a declared variable named x
	 * Verify evaluation does NOT trigger
	 */
	@Test
	public void oneCharacterAtATimeSet_WithDifferentVarNameProtected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "protected int set_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertFalse(mockUserInput("B", offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Verify evaluation does NOT trigger when adding spaces to the end of the method declaration
	 */
	@Test
	public void oneCharacterAtATimeGetXWithSpaces() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public int get".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertTrue(mockUserInput(INT_VAR_NAME, offset++));

		// Now add a space, and the evaluation should NOT trigger
		assertFalse(mockUserInput(" ", offset++));
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
