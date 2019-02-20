# IDE-IT

IDE Intelligent Tutorials

## Overview

The Integrated Development Environment - Intelligent Tutorials (IDE-IT) is a plug in for Eclipse that provides developers suggestions on Eclipse features that they may not be aware of. The number of features available in Eclipse, and IDEs in general, can be overwhelming, and it isn’t always clear to the user that they exist. These include features such as auto complete, block commenting, automatic imports, refactoring code on variable renaming, etc. However, learning that these features exist isn’t an easy process. The discoverability of IDE features is something that is a detriment to current programmers. The goal of IDE-IT is to provide developers with suggestions on how to take advantage of Eclipse’s features in a way that is relevant to what they are currently doing.

We plan to provide support for at least the following list of Eclipse features:

* Block Commenting
* Adding import statements using “shift-cmd-o”
* Removing unnecessary/unused import statements using “shift-cmd-o”
* Correcting indentation
* Refactor code base by renaming a variable throughout the entire project

This repository / plugin is specifically for the backend service of IDE-IT. This is not designed to be a standalone plugin. It requires a frontend service that uses this service to display feature suggestions to the user. We recommend the [IDE-IT frontend plugin](https://github.com/AlyssaRicketts/IDE-IT-Frontend), as this framework is built specifically for IDE-IT. If you would like to use your own custom frontend framework, see below on how to incorporate our service to your own plugin.

## Current Status as of 2/19/19

We have completed the following milestones:

* Basic plugin framework created
* Initial evaluation function written for evaluating block commenting
* Interface with frontend implemented
  * Manually tested with success - IDE-IT frontend plugin able to receive notification from feature evaluation
* Implement a working build file
* Integrate with Travis for CI testing on Github

Our next goals are:

* Write evaluation function for removing unused imports
* Write evaluation function for adding imports for unresolved classes
* Create a test suite and create unit tests for classes where appropriate

## Installation

### Required Software

* Eclipse 2018-12 (we recommend the RCP and RAP release version)
* Maven 3.6.0

### Building the plugin

Open a terminal on your machine and complete the following steps

* Clone this repo
* Change directory into IDE-IT/backend_plugin
* run "mvn clean install"

If any of the above does not work, please inform us through the issue tracker.

### Incorporating the plugin with your own project

To install the project from source, import the git repository into an eclipse workspace. Note that the eclipse must have the Plug-in Development Package installed. If this is not installed, you can install it by going to "Help" -> "Install new Software" selecting the "The Eclipse Project Updates" repository to work with. Then check the option for "Eclipse Plugin Development Tools" and continue through the wizard.

Once the project has been imported, simply right click on the project and click on "Export". Then click on "Plug-in Development" and "Deployable plug-ins and fragments" continuing through the wizard and selecting an output folder. This will create a file named something like `backend_plugin_1.0.0.201902122015.jar` inside a `plugins` folder. From there, the plugin can be installed by going to "Help" -> "Install New Software". Click "Add" and then "Local", browsing for the `plugins` folder that was created upon export. Select the plugin in the available software options that appear, and then continue through the wizard to install the plugin.

A standalone jar file for the latest version of eclipse is coming soon, as well as an installer directly through the eclipse marketplace (pending approval).

## Usage

This plugin serves as a backend service for IDE-IT as a whole. What that means is that this plugin will not function as a standalone plugin. If you install this plugin as is in your version of Eclipse, It will do nothing on its own.

In order to use this service, you must have a frontend plugin that designates the IDE-IT backend plugin as a dependency. See installation instructions above for how to get either the jar file or the source code downloaded into your own project. Take the following steps to to integrate our service with your frontend plugin:

1. Create a class that extends the FeatureSuggestionObserver class. In your class, provide an implementation of the notify(String featureID) method. Your implementation of the notify(String) method will determine what your plugin will do with the information collected by the IDE-IT backend plugin (see the list of featureID Strings below for possible inputs to this function).

2. In your own plugin's code, create a new instance of FeatureSuggestion, as well as an instance of your class that extends FeatureSuggestionObserver.

3. In your plugin's code, register the instantiated FeatureSuggestionObserver object with the instantiated FeatureSuggestion object, using the FeatureSuggestion's registerObserver(FeatureSuggestionObserver obs) method.

4. In your plugin's code, call the start() method of the instantiated FeatureSuggestion object. This will begin evaluation of user input. Your FeatureSuggestionObserver object will now be notified whenever the IDE-IT backend plugin detects that the user could benefit from knowing about a feature.

Observers are notified with a string that uniquely identifies an Eclipse feature. The current list of supported featureID strings can be found [in this repository](https://github.com/DavidThien/IDE-IT/blob/master/featureIDStrings.txt).

The following is a list of the featureID strings and descriptions of what Eclipse IDE features they represent:

* blockCommentSuggestion
  * Comment out multiple selected lines of code at once
* addImportStatementsSuggestion
  * Automatically include a needed import statement
* removeUnusedImportStatementsSuggestion
  * Remove all unused import statements
* correctIndentationsSuggestion
  * Correct indentation (either for multiple selected lines or for entire document)
* variableRenameRefactorSuggestion
  * Rename a variable throughout the entire scope of said variable

The documentation for FeatureSuggestionInterface and FeatureSuggestionObserver are listed below for easy reference:

* FeatureSuggestionObserver
  * void notify(String featureID)
    * All observers will be notified with the featureID (a unique string identifier) of an Eclipse feature when an evaluation function triggers.

* FeatureSuggestionInterface
  * boolean registerObserver(FeatureSuggestionObserver obs)
    * Register an observer with our service to be updated when any evaluation functions trigger.
  * boolean removeObserver(FeatureSuggestionObserver obs)
    * Removes a registered observer with our service.
  * void start()
    * Starts the backend service. Observers will receive notifications when feature suggestions are triggered through user action.
  * void stop()
    * Stops the backend server. Observers will no longer receive notifications.
  * boolean isRunning()
    * Provides a check to see the current running state of the backend service
  * List<String> getAllFeatureIDs()
    * Provides a list of all featureIDs. This is designed to be used to allow frontend services to check against the list of supported featureID strings for accuracy and verification at runtime.

## Implementation Details

* FeatureSuggestion
  * Must be created by the frontend plugin that uses our service. Creating and starting the FeatureSuggestion starts the entire service our plugin provides. This is the only object a frontend plugin should have access to.
* FeatureSuggestionInterface
  * Details what methods are available to a frontend plugin through the FeatureSuggestion object. See above in usage for more details.
* FeatureSuggestionObserver
  * Provides an abstract FeatureSuggestionObserver class that a frontend plugin can extend. The frontend plugin will instantiate that extended class and then register the instance with the FeatureSuggestion, as documented through the interface details in usage.
* EvaluationManager
  * Created during the construction of the FeatureSuggestion object. The EvaluatorManager assigns Evaluators to document editor windows, keeps track of all active Evaluators that have been assigned to document editor windows, and handles reporting triggered features from each Evaluator to the FeatureSuggestion. This ensures that all triggered feature reports notify the same FeatureSuggestion.
* EditorWindowListener
  * An extension of IPartListener2 from the Eclipse Plugin API. Created and added to the list of Eclipse workspace listeners when the EvaluatorManager is constructed. Listens for activation of document editor windows (i.e. when a document editor window or opened, or its tab is switched to). When that occurs, the EditorWindowListener will notify the EvaluatorManager to assign an Evaluator to the given document editor window.
* Evaluator
  * Responsible for evaluating document changes detected within a single document editor window. When document changes are detected, the Evaluator will cycle through each feature evaluation function, passing the document change event information. If a feature evaluation function returns true (indicating that the user has neglected to use the respective feature), it will notify the EvaluatorManager with the unique ID string of the feature that was triggered.
* DocumentChangesTracker
  * An extension of IDocumentListener from the Eclipse Plugin API. Created when a new Evaluator is assigned to a document editor window. Responsible for listening for changes made within that document editor window. When changes are detected, the DocumentChangesTracker will pass the change information back to the Evaluator.
