package com.majorproject.model.queries.pastebin;

/**
 * Output implementation that is used for testing.
 */
public class OutputOffline implements Output
{

    /**
     * Superficial method to test app; offline.
     * @param report the text to paste
     * @return the given text, as is.
     */
    @Override
    public String postPaste(String report)
    {
        return report;
    }
}
