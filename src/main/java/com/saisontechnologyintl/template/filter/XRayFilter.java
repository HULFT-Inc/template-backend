/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.filter;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import jakarta.inject.Singleton;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

@Slf4j
@Singleton
@Filter("/**")
public class XRayFilter implements HttpServerFilter {

  @Value("${aws.xray.enabled:false}")
  private Boolean xrayEnabled;

  @Override
  public Publisher<MutableHttpResponse<?>> doFilter(
      HttpRequest<?> request, ServerFilterChain chain) {
    
    if (!xrayEnabled) {
      return chain.proceed(request);
    }

    Segment segment = AWSXRay.beginSegment("changetracker-" + request.getPath());

    try {
      HashMap<String, Object> requestMap = new HashMap<>();
      requestMap.put("method", request.getMethod().name());
      requestMap.put("url", request.getPath());
      requestMap.put("user_agent", request.getHeaders().get("User-Agent"));
      segment.putHttp("request", requestMap);

      return new Publisher<MutableHttpResponse<?>>() {
        @Override
        public void subscribe(org.reactivestreams.Subscriber<? super MutableHttpResponse<?>> s) {
          chain
              .proceed(request)
              .subscribe(
                  new org.reactivestreams.Subscriber<MutableHttpResponse<?>>() {
                    @Override
                    public void onSubscribe(org.reactivestreams.Subscription subscription) {
                      s.onSubscribe(subscription);
                    }

                    @Override
                    public void onNext(MutableHttpResponse<?> response) {
                      HashMap<String, Object> responseMap = new HashMap<>();
                      responseMap.put("status", response.getStatus().getCode());
                      segment.putHttp("response", responseMap);
                      s.onNext(response);
                    }

                    @Override
                    public void onError(Throwable error) {
                      segment.addException(error);
                      segment.setError(true);
                      AWSXRay.endSegment();
                      s.onError(error);
                    }

                    @Override
                    public void onComplete() {
                      AWSXRay.endSegment();
                      s.onComplete();
                    }
                  });
        }
      };
    } catch (Exception e) {
      segment.addException(e);
      AWSXRay.endSegment();
      throw e;
    }
  }
}
