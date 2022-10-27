package com.majorproject.model.queries.pastebin;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class OutputOfflineTest
{
    /** Test to see if same thing is returned. */
    @Test
    public void postPaste()
    {
        OutputOffline outputOffline = new OutputOffline();

        assertThat(outputOffline.postPaste("test"), is("test"));
    }

}
