package com.majorproject.model.queries.pastebin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Output implementation that connects to the Pastebin api.
 * It is used to paste long reports of gathered data to the pastebin.com.
 */
public class OutputOnline implements Output
{
    private final String key;
    private final HttpClient client = HttpClient.newBuilder().build();

    public OutputOnline(String key)
    {
        this.key = key;
    }

    /**
     * Sends the provided report through to pastebin as a paste.
     * @param report the text to paste online
     * @return a link to the page containing the pasted report on pastebin.com
     */
    @Override
    public String postPaste(String report) throws Exception
    {
        try
        {
            String post = "api_dev_key=" + key + "&api_paste_code=" + report + "&api_option=paste";
            byte[] postEncoded = post.getBytes(StandardCharsets.UTF_8);

            String uri = "https://pastebin.com/api/api_post.php";
            HttpRequest request = HttpRequest.newBuilder(
                    new URI(uri))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(postEncoded)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() / 100 == 2)
                return response.body();
            else
                throw new Exception("Paste unsuccessful: " + response.statusCode() +
                        ". " + response.body());
        }
        catch(Exception e)
        {
           throw new Exception(e.getMessage());
        }
    }
}
