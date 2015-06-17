package api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class HttpClientConfiguration {
    private static final String APP_NAME_KEY = "spring.application.name";

    @Autowired
    private Environment environment;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate httpClient = new RestTemplate(clientHttpRequestFactory());
        return httpClient;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(2000);
        factory.setConnectTimeout(2000);
        return factory;
    }

    private class XUserAgentInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
            HttpHeaders headers = httpRequest.getHeaders();
            headers.add("X-User-Agent", environment.getProperty(APP_NAME_KEY));
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        }
    }
}
