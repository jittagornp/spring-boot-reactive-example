/*
 * Copyright 2017-2019 Pamarin.com
 */
package com.pamarin.learning.webflux.resolver;

import java.net.InetSocketAddress;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 * @author jitta
 */
public class HttpClientIPAddressResolverTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private HttpClientIPAddressResolver resolver;

    //private ServerWebExchange exchange;
    private ServerWebExchange exchange;

    @Before
    public void before() {
        resolver = new DefaultHttpClientIPAddressResolver();
        //exchange = mock(ServerWebExchange.class);
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("http://localhost:8080"));
    }

    @Test
    public void shouldBeThrowIllegalArgumentException_whenInputIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("require exchange.");

        resolver.resolve(null);
    }

    @Test
    public void shouldBeNull_whenAnyHeadersIsNull() {
        String output = resolver.resolve(exchange);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenXForwardedForHeaderIsUnknown() {
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").header("X-Forwarded-For", "UNKNOWN"));
        String output = resolver.resolve(exchange);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe127_0_0_1_whenXForwardedForHeaderIs127_0_0_1() {
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").header("X-Forwarded-For", "127.0.0.1"));
        String output = resolver.resolve(exchange);
        String expected = "127.0.0.1";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe127_0_0_1_whenRemoteAddrIs127_0_0_1() {

        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").remoteAddress(InetSocketAddress.createUnresolved("127.0.0.1", 80)));
        String output = resolver.resolve(exchange);
        String expected = "127.0.0.1:80";
        assertThat(output).isEqualTo(expected);
    }

}
