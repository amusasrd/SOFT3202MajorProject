package com.majorproject.model.queries.pastebin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OutputOnlineTest
{
    /** Test with fake key. */
    @Test
    public void postPaste1()
    {
        OutputOnline outputOnline = new OutputOnline("fake_key");

        assertThrows(Exception.class, () -> outputOnline.postPaste("test"));
    }

    /** Test with real key. Commented due to:
     * https://edstem.org/au/courses/7942/discussion/835807?answer=1882717
     */
//    @Test
//    public void postPaste2()
//    {
//        OutputOnline outputOnline = new OutputOnline(System.getenv("PASTEBIN_API_KEY"));
//
//        assertDoesNotThrow(() -> outputOnline.postPaste("test"));
//    }

}
