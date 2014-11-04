/*
 * Copyright (C) 2010-2014 dss886
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dss886.utils.http;

import com.dss886.utils.http.utils.ProgressListener;
import com.dss886.utils.http.utils.ResponseProcessor;
import com.dss886.utils.io.FileHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 该类封装了HTTP Get方法
 * @author dss886
 * @since 2014-9-7
 */
public class GetMethod {

    public static final String EXCEPTION_NETWORK = "Network Error";

    private CloseableHttpClient httpClient;
    private HttpGet httpGet;


    public GetMethod(){
        httpClient =  HttpClients.createDefault();
	}

	public String execute(String url) throws IOException {
        CloseableHttpResponse response = onExecute(url);
        String result = ResponseProcessor.getStringFromResponse(response);
        response.close();
        httpGet.abort();
        return result;
    }

    public void executeToFile(String url, File folder) throws IOException {
        CloseableHttpResponse response = onExecute(url);
        InputStream inputStream = response.getEntity().getContent();
        FileHelper.writeFile(inputStream, folder);
        response.close();
        httpGet.abort();
    }

    public void executeToFile(String url, File file, ProgressListener progressListener)
            throws IOException {
        CloseableHttpResponse response = onExecute(url);
        InputStream inputStream = response.getEntity().getContent();
        int fileSize = Integer.parseInt(response.getFirstHeader("Content-Length").toString().split(": ")[1]);
        FileHelper.writeFile(inputStream, file, fileSize, progressListener);
        response.close();
        httpGet.abort();
    }

    private CloseableHttpResponse onExecute(String url) throws IOException {
        httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new IOException(EXCEPTION_NETWORK + ":" + statusCode);
        }
        return response;
    }

}
