package test.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.CorrectIndentationEvaluator;

/**
 * Unit test for CorrectIndentationEvaluator
 */
public class CorrectIndentationEvaluatorTest {

    private static final String content = " Line1\n Line2\n Line3\n";
    private static final String extraSpaceContent = "  Line1\n  Line2\n  Line3\n";
    private static final String tabContent = "\tLine1\n\tLine2\n\tLine3\n";
    private static final String extraTabContent = "\t\tLine1\n\t\tLine2\n\t\tLine3\n";
    private static final String SINGLE_SPACE = " ";
    private static final String SINGLE_TAB = "\t";

    private IDocument doc;
    private CorrectIndentationEvaluator testEvaluator;

    // Used to store mock event data
    private DocumentEvent event;
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
     * Verifies adding space to the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownSpaceAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies adding two single spaces to the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleSpaceSeparateAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    offset++;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies adding two spaces in a single change to the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleSpaceTogetherAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE + SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing space at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownSpaceRemoved() {
	try {
	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing multiple spaces one action at a time at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleSpaceSeparatelyRemoved() {
	try {
	    // Mock a new document with extra spaces in the lines
	    doc = new Document(extraSpaceContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies adding space to the start of line 1, then removing a space from line 2 triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownSpaceAddedSpaceRemoved() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing space line 1, then adding space line 2 triggers
     */
    @Test
    public void twoConsecutiveLinesDownSpaceRemovedSpaceAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }



    /**
     * Verifies removing multiple spaces in one action at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleSpaceTogetherRemoved() {
	try {
	    // Mock a new document with extra spaces in the lines
	    doc = new Document(extraSpaceContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a double backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserDoubleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserDoubleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies evaluation returns true when space is added to the front of line 2, then a space is added to the front
     * of line 1
     */
    @Test
    public void twoConsecutiveLinesUpSpaceAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies evaluation returns true when space is added to the front of line 2, then a space is removed in line 1
     */
    @Test
    public void twoConsecutiveLinesUpSpaceRemovedSpaceAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies evaluation returns true when space is removed at the front of line 2, then a space is added in line 1
     */
    @Test
    public void twoConsecutiveLinesUpSpaceAddedSpaceRemoved() {
	try {
	    // Mock a document event with a single space placed at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Place a single space at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing multiple spaces one action at a time at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesUpSpaceRemoved() {
	try {
	    // Mock a new document with extra spaces in the lines
	    doc = new Document(extraSpaceContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a single backspace at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing multiple spaces one action at a time at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesUpMultipleSpaceSeparatelyRemoved() {
	try {
	    // Mock a new document with extra spaces in the lines
	    doc = new Document(extraSpaceContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a single backspace at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();
	    // Mock another backspace at the beginning of second line
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }


    /**
     * Verifies removing multiple spaces in one action at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesUpMultipleSpaceTogetherRemoved() {
	try {
	    // Mock a new document with extra spaces in the lines
	    doc = new Document(extraSpaceContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a double backspace at the beginning of the first line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserDoubleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserDoubleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verify that evaluation function triggers if two consecutive lines are adjusted out and triggers again
     * if another consecutive line is adjusted
     */
    @Test
    public void threeConsecutiveLinesDownSpaceAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the third line
	    offset = doc.getLineOffset(2);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verify that evaluation function triggers if two consecutive lines are adjusted out and triggers again
     * if another consecutive line is adjusted
     */
    @Test
    public void threeConsecutiveLinesUpSpaceAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the third line
	    offset = doc.getLineOffset(2);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Place a single space at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies evaluation triggers when space is added in line 3, line 1, then line 2
     */
    @Test
    public void threeConsecutiveLinesOutOfOrderSpaceAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the third line
	    offset = doc.getLineOffset(2);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Mock a document event with a single space placed at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
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
     * Verifies adding space to the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownTabAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies adding two single tabs to the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleTabSeparateAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    offset++;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies adding two tabs in a single change to the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleTabTogetherAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_TAB + SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing tabs at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownTabRemoved() {
	try {
	    doc = new Document(tabContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing multiple spaces one action at a time at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleTabSeparatelyRemoved() {
	try {
	    // Mock a new document with extra spaces in the lines
	    doc = new Document(tabContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();
	    // Mock a second backspace in the first line
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies removing multiple spaces in one action at the start of two consecutive lines of code triggers the evaluation function
     */
    @Test
    public void twoConsecutiveLinesDownMultipleTabsTogetherRemoved() {
	try {
	    // Mock a new document with extra tabs in the lines
	    doc = new Document(extraTabContent);
	    // set the evaluator to watch this new document
	    testEvaluator = new CorrectIndentationEvaluator(doc);

	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserDoubleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserDoubleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Add a tab line 1, remove a tab line 2
     */
    @Test
    public void twoConsecutiveLinesDownTabAddedTabRemoved() {
	try {
	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Add a tab line 2, remove a tab line 1
     */
    @Test
    public void twoConsecutiveLinesUpTabAddedTabRemoved() {
	try {
	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserSingleBackspace(offset));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Remove a tab line 1, add a tab line 2
     */
    @Test
    public void twoConsecutiveLinesDownTabRemovedTabAdded() {
	try {
	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Remove a tab line 1, Add a tab line 2
     */
    @Test
    public void twoConsecutiveLinesUpTabRemovedTabAdded() {
	try {
	    // Mock a document event with a single backspace at the beginning of the first line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserSingleBackspace(offset));
	    delayUserInput();

	    // Mock a backspace at the start of the second line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies evaluation returns true when tab is added to the front of line 2, then a tab is added to the front
     * of line 1
     */
    @Test
    public void twoConsecutiveLinesUpTabAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verify that evaluation function triggers if two consecutive lines are adjusted out and triggers again
     * if another consecutive line is adjusted
     */
    @Test
    public void threeConsecutiveLinesDownTabAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = 0;
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the third line
	    offset = doc.getLineOffset(2);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verify that evaluation function triggers if two consecutive lines are adjusted out and triggers again
     * if another consecutive line is adjusted
     */
    @Test
    public void threeConsecutiveLinesUpTabAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the third line
	    offset = doc.getLineOffset(2);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Place a single space at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Verifies evaluation triggers when space is added in line 3, line 1, then line 2
     */
    @Test
    public void threeConsecutiveLinesOutOfOrderTabAdded() {
	try {
	    // Mock a document event with a single space placed at the beginning of the third line
	    offset = doc.getLineOffset(2);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Mock a document event with a single space placed at the beginning of the first line
	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Mock a document event with a single space placed at the beginning of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
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


    ////////////////////////////////////////////////
    // Tests for adding / removing spaces with tabs
    ////////////////////////////////////////////////

    /**
     * Add space line 1, add tab line 2
     */
    @Test
    public void spaceAddedLine1TabAddedLine2() {
	try {

	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Mock a document event with a single tab placed at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Add space line 2, add tab line 1
     */
    @Test
    public void spaceAddedLine2TabAddedLine1() {
	try {

	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_SPACE));
	    delayUserInput();

	    // Mock a document event with a single tab placed at the start of the first line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_TAB));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Add tab line 1, add space line 2
     */
    @Test
    public void tabAddedLine1SpaceAddedLine2() {
	try {

	    offset = doc.getLineOffset(0);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Mock a document event with a single tab placed at the start of the second line
	    offset = doc.getLineOffset(1);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
	} catch (BadLocationException e) {
	    // Should never get here
	    fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
	    e.printStackTrace();
	}
    }

    /**
     * Add tab line 2, add space line 1
     */
    @Test
    public void tabAddedLine2SpaceAddedLine1() {
	try {

	    offset = doc.getLineOffset(1);
	    assertFalse(mockUserInput(offset, SINGLE_TAB));
	    delayUserInput();

	    // Mock a document event with a single tab placed at the start of the second line
	    offset = doc.getLineOffset(0);
	    assertTrue(mockUserInput(offset, SINGLE_SPACE));
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

    private boolean mockUserDoubleBackspace(int offset) throws BadLocationException {
	event = new DocumentEvent(doc, offset, 2, "");
	testEvaluator.evaluateDocumentBeforeChange(event);
	doc.replace(offset, 2, "");
	return testEvaluator.evaluateDocumentChanges(event);
    }

}
