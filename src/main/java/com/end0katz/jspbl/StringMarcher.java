package com.end0katz.jspbl;

import java.util.*;

public class StringMarcher {

    protected String data;
    protected String result;
    protected Stack<String> scopes;

    public StringMarcher(String data) {
        this.data = data;
    }

    /**
     * Backup current data to the scope stack.
     *
     * @return {@code this}
     */
    public StringMarcher enterScope() {
        scopes.add(data);
        return this;
    }

    /**
     * Restore data by leaving this scope
     *
     * @return {@code this}
     */
    public StringMarcher exitScope() {
        data = scopes.pop();
        return this;
    }

    /**
     * Confirm this scope as succesful and exit it without restoring previous
     * data
     *
     * @return {@code this}
     */
    public StringMarcher discardScope() {
        scopes.pop();
        return this;
    }

    /**
     * get the unprocessed section of data
     *
     * @return a string containing all data which has not yet been processed
     */
    public String data() {
        return this.data;
    }

    /**
     * Return the area removed by the last operation, or null if the operation
     * failed.
     *
     * @return the section removed by the last operation, or null if it failed
     */
    public String result() {
        return result;
    }

    /**
     * Return if the last operation failed
     *
     * @return true if the last operation succeeded, and false if result() would
     * return null
     */
    public boolean success() {
        return result != null;
    }

    /**
     * Return the area removed by the last operation, but add it back at the
     * end. chained calls to this can be diastrous
     *
     * @return {@code this.result();}
     */
    public String peek() {
        data = data + result();
        return result;
    }

    public String expect() throws Backup {
        if (success()) {
            return result();
        } else {
            throw new Backup();
        }
    }

    /**
     * Operation to discard the string {@code s} if it appears at the start of
     * data
     *
     * @param s the string to check and potentially discard
     * @return {@code this}
     */
    public StringMarcher str(String s) {
        result = data.startsWith(s) ? s : null;
        if (success()) {
            data = data.substring(s.length());
        }
        return this;
    }

    /**
     * Operation to discard the character {@code c} if it appears at the start
     * of data
     *
     * @param c the character to check and potentially discard
     * @return {@code this}
     */
    public StringMarcher str(char c) {
        return this.str(String.valueOf(c));
    }

    /**
     * Checks for and discards an area matching that regex. Note that if the
     * match is empty it will still succeed. As such, the regex
     * {@code new StringMarcher("aa").re("a*", StringMarcher.ExpansionType.START_SMALL_AND_GROW)}
     * will always succeed but match nothing, whereas {@code "a+"} will succeed
     * and remove 1 character.
     *
     * @param regex the regex to check
     * @param expansion the way the regex should decide what to match
     * @return {@code this}
     *
     */
    public StringMarcher re(String regex, ExpansionType expansion) {
        String match = null;
        int size;
        switch (expansion) {
            case ExpansionType.START_LARGE_AND_SHRINK -> {
                size = data.length();
                try {
                    while (!data.substring(0, size).matches(regex)) {
                        size--;
                    }
                    match = data.substring(0, size);
                } catch (IndexOutOfBoundsException e) {
                }
            }
            case ExpansionType.START_SMALL_AND_GROW -> {
                size = 0;
                try {
                    while (!data.substring(0, size).matches(regex)) {
                        size++;
                    }
                    match = data.substring(0, size);
                } catch (IndexOutOfBoundsException e) {
                }
            }
            default -> {
                size = -1;
            }
        }
        result = match;
        data = data.substring(size);
        return this;
    }

    /**
     * Discard the current scope and enter a new one, effectively resetting it
     *
     * @return {@code this}
     */
    public StringMarcher resetScope() {
        exitScope();
        enterScope();
        return this;
    }

    public static enum ExpansionType {
        START_LARGE_AND_SHRINK,
        START_SMALL_AND_GROW;
    }
}
