package test.java.negatives.evaluators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.junit.Before;
import org.junit.Test;

import main.evaluators.BlockCommentEvaluator;

/**
 * Tests against all known sequences of actions a user can take to manually attempt to comment out multiple lines
 * of code in their document
 */
public class BlockCommentNegative {

	private static final String content = "Line1\n Line2\n Line3\n";
	private static final String SINGLE_SLASH = "/";
	private static final String DOUBLE_SLASH = "//";
	private static final String STAR = "*";

	private IDocument doc;
	private BlockCommentEvaluator testEvaluator;

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
		testEvaluator = new BlockCommentEvaluator(doc);
	}

	/**
	 * Tests the block comment evaluation function returns true when line 0 then line 1 of a document are commented out
	 */
	@Test
	public void twoConsecutiveLinesDownCommentedOut() {
		// Mock a document event with a single backslash placed at the beginning of the first line
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 2 then line 1 of a document are commented out
	 */
	@Test
	public void twoConsecutiveLinesUpCommentedOut() {
		// Create mock document event changes
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Verify that evaluation function triggers if two consecutive lines are commented out and triggers again
	 * if another consecutive line is triggered
	 */
	@Test
	public void threeConsecutiveLinesDownCommented() {
		try {
			// Send mock data
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a second backslash after the first

			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the second line so we aren't guessing
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Comment out the third line
			// Place a single backslash at the start of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 3 then line 2 then line 1 are commented out
	 */
	@Test
	public void threeConsecutiveLinesUpCommentedOut() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests for /// one one line, then // on the next line
	 */
	@Test
	public void threeSlashesConsecutiveLinesDown() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a third backslash after the second
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the second line so we aren't guessing
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests for /// one one line, then // on the previous line
	 */
	@Test
	public void threeSlashesConsecutiveLinesUp() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a third backslash after the second
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests for //// one one line, then // on the next line
	 */
	@Test
	public void fourSlashesConsecutiveLinesDown() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a third backslash after the second
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a fourth backslash after the second
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the second line so we aren't guessing
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests for /// one one line, then // on the previous line
	 */
	@Test
	public void fourSlashesConsecutiveLinesUp() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a third backslash after the second
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a third backslash after the second
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests if a user inserts "//" on one line then "//" on the next line
	 * This can be accomplished by the user through ctrl + / on each line or if they paste a "//"
	 */
	@Test
	public void doubleSlashConsecutiveDown() {
		// Mock a document event with a double backslash placed at the beginning of the first line
		try {
			offset = 0;
			event = mockUserInput(offset, DOUBLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the second line so we aren't guessing
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, DOUBLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests if a user inserts "//" on one line then "//" on the next line
	 * This can be accomplished by the user through ctrl + / on each line or if they paste a "//"
	 */
	@Test
	public void doubleSlashConsecutiveUp() {
		try {
			// Mock a document event with a double backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, DOUBLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a double backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, DOUBLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests if a user inserts "/", the places another "/" in front of the first slash on one line, and
	 * repeats this behavior on the next line
	 */
	@Test
	public void singleSlashMoveLeftSingleSlashConsecutiveLinesDown() {
		// Mock a document event with a single backslash placed at the beginning of the first line
		try {
			offset = 0;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a second backslash at the beginning of the line
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the second line so we aren't guessing
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash at the start of the line
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests if a user inserts "/", the places another "/" in front of the first slash on one line, and
	 * repeats this behavior on the previous line
	 */
	@Test
	public void singleSlashMoveLeftSingleSlashConsecutiveLinesUp() {
		// Create mock document event changes
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash at the start of the same line
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash at the start of the second line
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Verify that evaluation function triggers if two consecutive lines are commented out and triggers again
	 * if another consecutive line is triggered
	 */
	@Test
	public void commentOutLine1Line3Line2() {
		try {
			// Send mock data
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = 0;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
			// Place a second backslash after the first

			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Pull the offset for the start of the third line
			// Place a single backslash at the start of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Comment out the second line
			// Place a single backslash at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 3 then line 2 then line 1 are commented out
	 */
	@Test
	public void commentOutLine3Line1Line2() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the block comment evaluation function returns true when line 3 then line 2 then line 1 are commented out
	 */
	@Test
	public void commentOutLine3Line1Line4Line2() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the fourth line
			offset = doc.getLineOffset(3);
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting // one line 1, then deletes until line 2 becomes line 1, then does the same for the next line
	 */
	@Test
	public void doubleSlashLineThenDeleteDown() {
		try {
			// Create a new document that has 6 lines, two being blank
			// Contrived to make deleting other lines easier
			doc = new Document("\nLine2\n\nLine3\nLine4");
			testEvaluator = new BlockCommentEvaluator(doc);

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Delete the rest of line 1
			offset++; // this offset is now at the \n character in the first line
			doc.replace(offset, 1, "");

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting // one line 2, then deletes the rest of line 2, then puts // on line 1
	 */
	@Test
	public void doubleSlashLineThenDeleteUp() {
		try {
			// Create a new document that has 6 lines, two being blank
			// Contrived to make deleting other lines easier
			doc = new Document("\nLine2\n\nLine3\nLine4");
			testEvaluator = new BlockCommentEvaluator(doc);

			// Mock a document event with a single backslash placed at the beginning of the third line
			offset = doc.getLineOffset(2);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a second backslash after the first
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Delete the rest of line 3
			offset++; // this offset is now at the \n character in the first line
			doc.replace(offset, 1, "");

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place another backslash after the previous one
			offset++;
			doc.replace(offset, 0, SINGLE_SLASH);
			event = createDocEvent(offset, SINGLE_SLASH);
			// Now the evaluation function should trigger
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting /* one line 1, then * on line 2
	 */
	@Test
	public void multilineCommentConsecutiveDownSimple() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a * after the backslash
			offset++;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Place a * at the start of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, STAR);
			// This should evaluate to true
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting * one line 2, then /* on line 1
	 */
	@Test
	public void multilineCommentConsecutiveUpSimple() {
		try {
			// Mock a document event with a single star placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Place a / at the start of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a / at the start of the first line
			offset++;
			event = mockUserInput(offset, STAR);
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting /* one line 1, then * and / at the end of line 2
	 */
	@Test
	public void multilineCommentConsecutiveDown() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a * after the backslash
			offset++;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// grab the start of line 3 and subtract one to get to the end of line 2
			// Place a * at the end of line 2
			offset = doc.getLineOffset(3) - 1;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a / after the *
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting * and / at the end of line 2, then /* at the start of line 1
	 */
	@Test
	public void multilineCommentConsecutiveUp() {
		try {
			// End of line 2 is found by subtracting one from the start of line 3
			offset = doc.getLineOffset(2) - 1;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a / after the star
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Place a / at the start of line 1
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a * after the /
			offset++;
			event = mockUserInput(offset, STAR);
			assertTrue(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting /* one line 1, then * and / at the end of line 1
	 * Then repeating that behavior for the next line
	 */
	@Test
	public void multilineCommentSingleLineConsecutiveDown() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a * after the backslash
			offset++;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// grab the start of line 2 and subtract one to get to the end of line 1
			// Place a * at the end of line 2
			offset = doc.getLineOffset(1) - 1;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a / after the *
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(1);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a * after the backslash
			offset++;
			event = mockUserInput(offset, STAR);
			assertTrue(testEvaluator.evaluateDocumentChanges(event));


			// Not sure where we should be evaluating to true here
			// Did it in the above evaluation, but it can be changed as needed

			delayUserInput();

			// grab the start of line 3 and subtract one to get to the end of line 2
			// Place a * at the end of line 2
			offset = doc.getLineOffset(2) - 1;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a / after the *
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}

	/**
	 * Mocks putting /* one line 2, then * and / at the end of line 2
	 * Then repeating that behavior for the previous line
	 */
	@Test
	public void multilineCommentSingleLineConsecutiveUp() {
		try {
			// Mock a document event with a single backslash placed at the beginning of the second line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a * after the backslash
			offset++;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// grab the start of line 3 and subtract one to get to the end of line 2
			// Place a * at the end of line 2
			offset = doc.getLineOffset(2) - 1;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a / after the *
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			delayUserInput();

			// Mock a document event with a single backslash placed at the beginning of the first line
			offset = doc.getLineOffset(0);
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a * after the backslash
			offset++;
			event = mockUserInput(offset, STAR);
			assertTrue(testEvaluator.evaluateDocumentChanges(event));


			// Not sure where we should be evaluating to true here
			// Did it in the above evaluation, but it can be changed as needed

			delayUserInput();

			// grab the start of line 2 and subtract one to get to the end of line 1
			// Place a * at the end of line 1
			offset = doc.getLineOffset(1) - 1;
			event = mockUserInput(offset, STAR);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));

			// Place a / after the *
			offset++;
			event = mockUserInput(offset, SINGLE_SLASH);
			assertFalse(testEvaluator.evaluateDocumentChanges(event));
		} catch (BadLocationException e) {
			// Should never get here
			fail("Should never see this error in: " + this.getClass().getSimpleName() + "::" + this.getClass().getName());
			e.printStackTrace();
		}
	}



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

	/**
	 * Helper method to mock user inputs. This will insert the given data at the given offset in the document
	 * This will also return a new event DocumentEvent that can be passed to the evaluateDocumentChanges method
	 *
	 * @param offset location in the document to add text
	 * @param data the text to be added to the document
	 * @return a DocumentEvent containing the changes
	 * @throws BadLocationException if the offset is not in the current document.
	 */
	private DocumentEvent mockUserInput(int offset, String data) throws BadLocationException {
		doc.replace(offset,  0, data);
		return createDocEvent(offset, data);
	}
}
