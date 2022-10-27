package com.majorproject.model.queries.pastebin;

/**
 * This interface is the template by which the app sends its currency conversion report to be outputted.
 */
public interface Output
{
    String postPaste(String report) throws Exception;
}
