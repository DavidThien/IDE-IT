package test.java.evaluators.positiveCases;

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
 * Tests Getter Setter Evaluation for user actions that the evaluator should trigger on
 */
public class GetterSetterPositiveCases {

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
	 * Type "public int getX" with a declared variable named x
	 */
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

	/**
	 * Type "public int get_testName" with a declared variable named testName
	 */
	@Test
	public void oneCharacterAtATimeGet_StringVar() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public String get_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out the string except for the last character
		String truncatedVarName = STRING_VAR_NAME.substring(0, STRING_VAR_NAME.length()-1);
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Complete the variable name and the evaluation should trigger
		assertTrue(mockUserInput(STRING_VAR_NAME.substring(STRING_VAR_NAME.length()-1), offset));

	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected int getTestName" with a declared variable named testName
	 */
	@Test
	public void oneCharacterAtATimeGetVarNameProtected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "protected String get".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out "TestName' except for the last character
		String truncatedVarName = "TestNam";
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		// Complete the variable name and the evaluation should trigger
		assertTrue(mockUserInput(STRING_VAR_NAME.substring(STRING_VAR_NAME.length()-1), offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected int get_X" with a declared variable named x
	 */
	@Test
	public void oneCharacterAtATimeGet_XProtected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public int get_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertTrue(mockUserInput(INT_VAR_NAME, offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Paste "public int getY()", then backspace and change the value of Y to INT_VAR_NAME
	 */
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

	/**
	 * Paste "public String get_Y()", then backspace and change the value of Y to STRING_VAR_NAME
	 */
	@Test
	public void copyPasteGetAndPasteVarName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public string get_Y()";
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
	 * Paste "public int getY()", then select "Y" and change the value to X
	 */
	@Test
	public void copyPasteGetAndChangeJustName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public int getY()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();

		// Now we need to get the offset location of just "Y"
		offset -= 3; // Y is three characters from the end of the line added

		// Mock new user input, since we are replacing text and not just inserting new text
		DocumentEvent event = new DocumentEvent(doc, offset, 1, INT_VAR_NAME);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 1, INT_VAR_NAME);
		assertTrue(eval.evaluateDocumentChanges(event));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Paste "public String get_newVar()", then select "newVar" and change the value to TestName
	 */
	@Test
	public void copyPasteGet_AndChangeJustName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public String get_newVar()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();

		// Now we need to get the offset location of just "newVar"
		offset -= 8;

		// Mock new user input, since we are replacing text and not just inserting new text
		DocumentEvent event = new DocumentEvent(doc, offset, 6, STRING_VAR_NAME);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 6, STRING_VAR_NAME);
		assertTrue(eval.evaluateDocumentChanges(event));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Paste "public String get_testVar()", then select "Var" and change the value to "Name"
	 */
	@Test
	public void copyPasteGet_AndChangeJustPartialName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public String get_testVar()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();

		// Now we need to get the offset location of just "Var"
		offset -= 5;

		// Mock new user input, since we are replacing text and not just inserting new text
		DocumentEvent event = new DocumentEvent(doc, offset, 3, "Name");
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 3, "Name");
		assertTrue(eval.evaluateDocumentChanges(event));
	    } catch (BadLocationException e) {}
	}


	/**
	 * Type "public String setTestName" with a declared variable named testName
	 */
	@Test
	public void oneCharacterAtATimeSetStringVar() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public String set".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out "TestName' except for the last character
		String truncatedVarName = "TestNam";
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		// Complete the variable name and the evaluation should trigger
		assertTrue(mockUserInput(STRING_VAR_NAME.substring(STRING_VAR_NAME.length()-1), offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "public int set_X" with a declared variable named x
	 */
	@Test
	public void oneCharacterAtATimeSet_X() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public int set_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertTrue(mockUserInput(INT_VAR_NAME, offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected int setX" with a declared variable named x
	 */
	@Test
	public void oneCharacterAtATimeSetXPRotected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "protected int set".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		assertTrue(mockUserInput(INT_VAR_NAME, offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Type "protected String set_testName" with a declared variable named testName
	 */
	@Test
	public void oneCharacterAtATimeSetStringVarProtected() {
	    try {
		int offset = doc.getLineOffset(4);
		for (char c : "public String set_".toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}

		// Iterate through typing out "TestName' except for the last character
		String truncatedVarName = STRING_VAR_NAME.substring(0, STRING_VAR_NAME.length()-1);
		for (char c : truncatedVarName.toCharArray()) {
		    assertFalse(mockUserInput(String.valueOf(c), offset++));
		}
		// Complete the variable name and the evaluation should trigger
		assertTrue(mockUserInput(STRING_VAR_NAME.substring(STRING_VAR_NAME.length()-1), offset++));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Paste "public int setY()", then backspace and change the value of Y to INT_VAR_NAME
	 */
	@Test
	public void copyPasteSetAndChangeName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public int setY()";
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

	/**
	 * Paste "public String get_Y()", then backspace and change the value of Y to STRING_VAR_NAME
	 */
	@Test
	public void copyPasteSetAndPasteVarName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public String set_Y()";
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
	 * Paste "public int setY()", then select "Y" and change the value to X
	 */
	@Test
	public void copyPasteSetAndChangeJustName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public int setY()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();

		// Now we need to get the offset location of just "Y"
		offset -= 3; // Y is three characters from the end of the line added

		// Mock new user input, since we are replacing text and not just inserting new text
		DocumentEvent event = new DocumentEvent(doc, offset, 1, INT_VAR_NAME);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 1, INT_VAR_NAME);
		assertTrue(eval.evaluateDocumentChanges(event));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Paste "public String set_newVar()", then select "newVar" and change the value to TestName
	 */
	@Test
	public void copyPasteSet_AndChangeJustName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public String set_newVar()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();

		// Now we need to get the offset location of just "newVar"
		offset -= 8;

		// Mock new user input, since we are replacing text and not just inserting new text
		DocumentEvent event = new DocumentEvent(doc, offset, 6, STRING_VAR_NAME);
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 6, STRING_VAR_NAME);
		assertTrue(eval.evaluateDocumentChanges(event));
	    } catch (BadLocationException e) {}
	}

	/**
	 * Paste "public String set_testVar()", then select "Var" and change the value to "Name"
	 */
	@Test
	public void copyPasteSet_AndChangeJustPartialName() {
	    try {
		int offset = doc.getLineOffset(4);
		String meth_dec = "public String set_testVar()";
		assertFalse(mockUserInput(meth_dec, offset));
		offset += meth_dec.length();

		// Now we need to get the offset location of just "Var"
		offset -= 5;

		// Mock new user input, since we are replacing text and not just inserting new text
		DocumentEvent event = new DocumentEvent(doc, offset, 3, "Name");
		eval.evaluateDocumentBeforeChange(event);
		doc.replace(offset, 3, "Name");
		assertTrue(eval.evaluateDocumentChanges(event));
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
