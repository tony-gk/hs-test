package org.hyperskill.hstest.v4.exception;

import org.hyperskill.hstest.v4.common.Utils;
import org.hyperskill.hstest.v4.statics.ObjectsCloner;
import org.hyperskill.hstest.v4.statics.StaticFieldsManager;
import org.hyperskill.hstest.v4.statics.serialization.Serialized;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystemException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import static org.hyperskill.hstest.v4.exception.StackTraceUtils.*;


public class FailureHandler {

    public static boolean detectStaticCloneFails() {
        return !StaticFieldsManager.cantClone.isEmpty()
            || !ObjectsCloner.circularLinks.isEmpty()
            || !ObjectsCloner.cantDeserialize.isEmpty()
            || !ObjectsCloner.cantSerialize.isEmpty();
    }

    public static final String avoidStaticsMsg =
        "We detected that you are using static variables, " +
        "but they are not fully supported in testing. " +
        "It might happen that if you try to avoid using " +
        "them you will pass this stage.";

    private static String getReport() {
        String os = System.getProperty("os.name");
        String java = System.getProperty("java.version");
        String vendor = System.getProperty("java.vendor");

        String info =
            "OS " + os + "\n" +
            "Java " + java + "\n" +
            "Vendor " + vendor + "\n" +
            "Testing library version 4";

        StringBuilder cantClone = new StringBuilder("Cannot be cloned: ");

        if (StaticFieldsManager.cantClone.isEmpty()) {
            cantClone.append("nothing");
        } else {
            for (Class clazz : StaticFieldsManager.cantClone.keySet()) {
                Exception exception = StaticFieldsManager.cantClone.get(clazz);
                String stackTrace = getStackTrace(exception);
                String filteredStackTrace = removeNonLibraryClasses(stackTrace);

                cantClone.append("\n");
                cantClone.append(clazz.toString());
                cantClone.append("\n");
                cantClone.append(filteredStackTrace);
            }
        }

        StringBuilder cantDeserialize = new StringBuilder("Cannot deserialize: ");

        if (ObjectsCloner.cantDeserialize.isEmpty()) {
            cantDeserialize.append("nothing");
        } else {
            for (Serialized serialized : ObjectsCloner.cantDeserialize) {

                cantDeserialize.append("\n");
                cantDeserialize.append(serialized.sourceClass.toString());
                cantDeserialize.append("\nJackson:\n");

                if (serialized.jacksonDeserialized != null) {
                    String stackTrace = getStackTrace(serialized.jacksonDeserialized);
                    String filteredStackTrace = removeNonLibraryClasses(stackTrace);
                    cantDeserialize.append(filteredStackTrace);
                } else {
                    cantDeserialize.append("nothing");
                }

                cantDeserialize.append("\nJsonIo:\n");

                if (serialized.jsonioDeserialized != null) {
                    String stackTrace = getStackTrace(serialized.jsonioDeserialized);
                    String filteredStackTrace = removeNonLibraryClasses(stackTrace);
                    cantDeserialize.append(filteredStackTrace);
                } else {
                    cantDeserialize.append("nothing");
                }
            }
        }

        StringBuilder cantSerialize = new StringBuilder("Cannot serialize: ");

        if (ObjectsCloner.cantSerialize.isEmpty()) {
            cantSerialize.append("nothing");
        } else {
            for (Serialized serialized : ObjectsCloner.cantSerialize) {

                cantSerialize.append("\n");
                cantSerialize.append(serialized.sourceClass.toString());
                cantSerialize.append("\nJackson:\n");

                if (serialized.jacksonSerialized != null) {
                    String stackTrace = getStackTrace(serialized.jacksonSerialized);
                    String filteredStackTrace = removeNonLibraryClasses(stackTrace);
                    cantSerialize.append(filteredStackTrace);
                } else {
                    cantSerialize.append("nothing");
                }

                cantSerialize.append("\nJsonIo:\n");

                if (serialized.jsonioSerialized != null) {
                    String stackTrace = getStackTrace(serialized.jsonioSerialized);
                    String filteredStackTrace = removeNonLibraryClasses(stackTrace);
                    cantSerialize.append(filteredStackTrace);
                } else {
                    cantSerialize.append("nothing");
                }
            }
        }

        StringBuilder circularLinks = new StringBuilder("Circular links: ");

        if (ObjectsCloner.circularLinks.isEmpty()) {
            circularLinks.append("nothing");
        } else {
            for (Serialized serialized : ObjectsCloner.circularLinks) {
                circularLinks.append("\n");
                circularLinks.append(serialized.sourceClass.toString());
            }
        }

        return info + "\n\n" +
            cantClone.toString() + "\n" +
            cantDeserialize.toString() + "\n" +
            cantSerialize.toString() + "\n" +
            circularLinks.toString() + "\n";
    }

    public static String getFeedback(Exception ex, int currTest) {

        String errorText;
        String stackTraceInfo;
        if (ex.getCause() != null &&
            ex instanceof InvocationTargetException) {
            // If user failed then ex == InvocationTargetException
            // and ex.getCause() == Actual user exception
            errorText = "Exception in test #" + currTest;
            stackTraceInfo = filterStackTrace(getStackTrace(ex.getCause()));

            Throwable cause = ex.getCause();

            if (cause instanceof InputMismatchException
                && stackTraceInfo.contains("java.util.Scanner")) {

                errorText += "\n\nProbably you have nextInt() (or similar Scanner method) " +
                    "followed by nextLine() - in this situation nextLine() often gives an " +
                    "empty string and the second nextLine() gives correct string.";

            } else if (cause instanceof NoSuchElementException
                && stackTraceInfo.contains("java.util.Scanner")) {

                errorText += "\n\nMaybe you created more than one instance of Scanner? " +
                    "You should use a single Scanner in program.";
            }

            if (stackTraceInfo.contains("java.lang.Runtime.exit")) {
                errorText = "Error in test #" + currTest + " - Tried to exit";
            }

            if (detectStaticCloneFails()) {
                errorText += "\n\n" + avoidStaticsMsg;
            }

        } else if (ex instanceof FileSystemException) {

            errorText = "Error in test #" + currTest ;
            stackTraceInfo = "";

            // without "class "
            String exceptionName = ex.getClass().toString().substring(6);

            String file = ((FileSystemException) ex).getFile();

            if (file.startsWith(Utils.CURRENT_DIR)) {
                file = file.substring(Utils.CURRENT_DIR.length());
            }

            errorText += "\n\n" + exceptionName + "\n\nThe file " + file +
                " can't be deleted after the end of the test. " +
                "Probably you didn't close File or Scanner.";

        } else {

            String whenErrorHappened;
            if (currTest == 0) {
                whenErrorHappened = "during testing";
            } else {
                whenErrorHappened = "in test #" + currTest;
            }

            errorText = "Fatal error " + whenErrorHappened +
                ", please send the report to Hyperskill team.\n\n" + getReport();
            if (ex.getCause() == null) {
                stackTraceInfo = getStackTrace(ex);
            } else {
                stackTraceInfo = getStackTrace(ex) +
                    "\n" + getStackTrace(ex.getCause());
            }
        }

        return errorText + "\n\n" + stackTraceInfo;
    }
}
