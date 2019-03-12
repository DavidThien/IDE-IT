package test.java.evaluators.negativeCases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.CorrectIndentationEvaluator;

/**
 * Test Correct Indentation Evaluator for user actions that our function should NOT trigger on
 */
public class CorrectIndentationNegativeCases {

    /** Content to generate a Document with */
    private static final String content = " Line1\n Line2\n Line3\n";
    /** Content to generate a Document with */
    private static final String extraSpaceContent = "  Line1\n  Line2\n  Line3\n";
    /** Content to generate a Document with */
    private static final String tabContent = "\tLine1\n\tLine2\n\tLine3\n";
    /** Content to generate a Document with */
    private static final String extraTabContent = "\t\tLine1\n\t\tLine2\n\t\tLine3\n";
    /** Single space character that can be referenced by a constant */
    private static final String SINGLE_SPACE = " ";
    /** Single tab character that can be referenced by a constant */
    private static final String SINGLE_TAB = "\t";

    /** Document to be attached to the evaluator */
    private IDocument doc;
    /** Evaluator being tested */
    private CorrectIndentationEvaluator testEvaluator;

    /** DocumentEvent to store document changes */
    private DocumentEvent event;
    /** Offset of where the document changes occurred in the document */
    private int offset;

    /**
     * Initialize a new document and a new evaluator
     */
    @Before
    public void runBeforeTests() {
	// Create a document with four lines
	doc = new Document(content);
	testEvaluator = new CorrectIndentationEvaluator(doc);
    }

    //////////////////////////////////////
    /// Test for adding / removing spaces
    /////////////////////////////////////

    /**
     * Verifies that the evaluation function will not trigger when the user types " " anywhere but the start
     * of a line
     */
    @Test
    public void spaceNotAtStartOfLineDown() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Pull the offset for the end of the second line
	    offset = doc.getLineOffset(2) - 1;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));

	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies that the evaluation function will not trigger when the user types " " anywhere but the start
     * of a line
     */
    @Test
    public void spaceNotAtStartOfLineUp() {
	try {
	    // Mock a document event with a single backslash placed at the beginning of the third line
	    offset = doc.getLineOffset(2);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));

	    delayUserInput();

	    // Mock a document event with a single backslash placed at the end of the second line
	    offset = doc.getLineOffset(2) - 1;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }


    /**
     * Verifies that the evaluation function will not trigger when the user types " " + a character on line 1 and then adds a space at the start
     * of line 2
     */
    @Test
    public void spaceThenCharacterAtStartOfLine() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();
	    offset++;
	    assertFalse(mockUserInput(offset, "b"));

	    // Mock a document event with a single space placed at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies that the evaluation function will not trigger when the user removes space at the start then types a character on line 1 and then
     * removes a space at the start of line 2
     */
    @Test
    public void spaceRemovedThenCharacterAtStartOfLine() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();
	    offset++;
	    assertFalse(mockUserInput(offset, "b"));

	    // Mock a document event with a single space placed at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }


    ///////////////////////////////////
    // Tests for adding / removing tabs
    ///////////////////////////////////

    /**
     * Verifies that the evaluation function will not trigger when the user types " " anywhere but the start
     * of a line
     */
    @Test
    public void tabNotAtStartOfLineDown() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Pull the offset for the end of the second line
	    offset = doc.getLineOffset(2) - 1;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));

	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies that the evaluation function will not trigger when the user types " " anywhere but the start
     * of a line
     */
    @Test
    public void tabNotAtStartOfLineUp() {
	try {
	    // Mock a document event with a single backslash placed at the beginning of the third line
	    offset = doc.getLineOffset(2);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));

	    delayUserInput();

	    // Mock a document event with a single backslash placed at the end of the second line
	    offset = doc.getLineOffset(2) - 1;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }


    /**
     * Verifies that the evaluation function will not trigger when the user types " " + a character on line 1 and then adds a space at the start
     * of line 2
     */
    @Test
    public void tabThenCharacterAtStartOfLine() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();
	    offset++;
	    assertFalse(mockUserInput(offset, "b"));

	    // Mock a document event with a single space placed at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies that the evaluation function will not trigger when the user removes space at the start then types a character on line 1 and then
     * removes a space at the start of line 2
     */
    @Test
    public void tabRemovedThenCharacterAtStartOfLine() {
	try {
	    doc = new Document(tabContent);
	    testEvaluator = new CorrectIndentationEvaluator(doc);
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();
	    offset++;
	    assertFalse(mockUserInput(offset, "b"));

	    // Mock a document event with a single space placed at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    ////////////////////////////////
    // Helper Methods
    ////////////////////////////////

    /**
     * Helper method to create a new document event given the offset and text to be added
     * @param offset position in the document to add the text
     * @param text to be added to the document
     * @return
     */
    private DocumentEvent createDocEvent(int offset, String text) {
	return new DocumentEvent(doc, offset, text.length(), text);
    }

    /**
     * Helper method to delay 100 ms between document changes. This allows us to more realistically mock user input
     */
    private void delayUserInput() {
	// Wait for 101 ms to simulate the time it takes the user to manually move to the line above
	long startTime = System.currentTimeMillis();
	while (System.currentTimeMillis() - startTime <= 100) {}
    }

    private boolean mockUserInput(int offset, String text) throws BadLocationException {
	event = createDocEvent(offset, text);
	testEvaluator.evaluateDocumentBeforeChange(event);
	doc.replace(offset,  0,  text);
	return testEvaluator.evaluateDocumentChanges(event);
    }

    private boolean mockUserSingleBackspace(int offset) throws BadLocationException {
	event = new DocumentEvent(doc, offset, 1, "");
	testEvaluator.evaluateDocumentBeforeChange(event);
	doc.replace(offset, 1, "");
	return testEvaluator.evaluateDocumentChanges(event);
    }
}
