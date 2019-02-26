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
