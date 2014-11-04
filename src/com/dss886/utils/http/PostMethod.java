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

import com.dss886.utils.http.utils.ResponseProcessor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.util.List;

/**
 * 该类封装了HTTP Get方法
 * @author dss886
 * @since 2014-9-7
 */
public class PostMethod {

    public static final String EXCEPTION_NETWORK = "Network Error";

	private CloseableHttpClient httpClient;

    public PostMethod(){
        httpClient =  HttpClients.createDefault();
	}

	public String execute(String url, List<NameValuePair> nameValuePairs) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if (null != nameValuePairs) {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        CloseableHttpResponse response = httpClient.execute(httpPost);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
            throw new Exception(EXCEPTION_NETWORK + ":" + statusCode);
        }

		String result = ResponseProcessor.getStringFromResponse(response);

        response.close();
        httpPost.abort();
		return result;
	}
	
}
