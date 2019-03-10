package main.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is designed to single place of storage for the featureID strings.
 * The strings will all be public and will allow any class, whether in the frontend or backend to use them liberally
 */
 public class FeatureID {

     	// featureID strings used to communicate between frontend and backend
  	// These must match EXACTLY with the agreed upon strings
  	// A list of these strings can be found in featureIDStrings.txt in the main folder of this repo
  	public static final String BLOCK_COMMENT_FEATURE_ID = "blockCommentSuggestion";
  	public static final String ADD_IMPORT_FEATURE_ID = "addImportStatementsSuggestion";
  	public static final String CORRECT_INDENTATION_FEATURE_ID = "correctIndentationsSuggestion";
  	public static final String REMOVE_IMPORT_FEATURE_ID = "removeUnusedImportStatementSuggestion";
  	public static final String TRAILING_WHITE_SPACE_FEATURE_ID = "trailingWhiteSpaceSuggestion";


  	private static final List<String> featureIDs = Arrays.asList(BLOCK_COMMENT_FEATURE_ID,
  									ADD_IMPORT_FEATURE_ID,
  									CORRECT_INDENTATION_FEATURE_ID,
  									REMOVE_IMPORT_FEATURE_ID,
  									TRAILING_WHITE_SPACE_FEATURE_ID);

  	public static List<String> getAllFeatureIDs() {
  	    return new ArrayList<String>(featureIDs);
  	}

}
