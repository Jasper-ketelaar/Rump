/**
 * Rump is a REST client for Java that allows for easy configuration and default values.
 *
 * Copyright (C) 2020 Jasper Ketelaar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package dev.yasper.rump;

import dev.yasper.rump.config.RequestConfig;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;

/**
 * Class for storing headers
 */
public class Headers {

    private final Map<String, Header> headers = new HashMap<>();

    /**
     * Headers instance constructed from a map of header fields
     * @param headerFields the header fields from which this instance should be constructed
     */
    public Headers(Map<String, List<String>> headerFields) {
        for (String key : headerFields.keySet()) {
            headers.put(key, new SimpleHeader(key, String.join(", ", headerFields.get(key))));
        }
    }

    /**
     * Default constructor for empty instance
     */
    public Headers() {
    }

    public String getAccept() {
        return getSafeValue(HeaderNames.ACCEPT);
    }

    public Headers setAccept(String accept) {
        return setHeader(HeaderNames.ACCEPT, accept);
    }

    public String getAcceptCharset() {
        return getSafeValue(HeaderNames.ACCEPT_CHARSET);
    }

    public Headers setAcceptCharset(String accept_charset) {
        return setHeader(HeaderNames.ACCEPT_CHARSET, accept_charset);
    }

    public String getAcceptEncoding() {
        return getSafeValue(HeaderNames.ACCEPT_ENCODING);
    }

    public Headers setAcceptEncoding(String accept_encoding) {
        return setHeader(HeaderNames.ACCEPT_ENCODING, accept_encoding);
    }

    public String getAcceptLanguage() {
        return getSafeValue(HeaderNames.ACCEPT_LANGUAGE);
    }

    public Headers setAcceptLanguage(String accept_language) {
        return setHeader(HeaderNames.ACCEPT_LANGUAGE, accept_language);
    }

    public String getAcceptRanges() {
        return getSafeValue(HeaderNames.ACCEPT_RANGES);
    }

    public Headers setAcceptRanges(String accept_ranges) {
        return setHeader(HeaderNames.ACCEPT_RANGES, accept_ranges);
    }

    public String getAge() {
        return getSafeValue(HeaderNames.AGE);
    }

    public Headers setAge(String age) {
        return setHeader(HeaderNames.AGE, age);
    }

    public String getAllow() {
        return getSafeValue(HeaderNames.ALLOW);
    }

    public Headers setAllow(String allow) {
        return setHeader(HeaderNames.ALLOW, allow);
    }

    public String getAuthorization() {
        return getSafeValue(HeaderNames.AUTHORIZATION);
    }

    public Headers setAuthorization(String authorization) {
        return setHeader(HeaderNames.AUTHORIZATION, authorization);
    }

    public String getCacheControl() {
        return getSafeValue(HeaderNames.CACHE_CONTROL);
    }

    public Headers setCacheControl(String cache_control) {
        return setHeader(HeaderNames.CACHE_CONTROL, cache_control);
    }

    public String getConnection() {
        return getSafeValue(HeaderNames.CONNECTION);
    }

    public Headers setConnection(String connection) {
        return setHeader(HeaderNames.CONNECTION, connection);
    }

    public String getContentEncoding() {
        return getSafeValue(HeaderNames.CONTENT_ENCODING);
    }

    public Headers setContentEncoding(String content_encoding) {
        return setHeader(HeaderNames.CONTENT_ENCODING, content_encoding);
    }

    public String getContentLanguage() {
        return getSafeValue(HeaderNames.CONTENT_LANGUAGE);
    }

    public Headers setContentLanguage(String content_language) {
        return setHeader(HeaderNames.CONTENT_LANGUAGE, content_language);
    }

    public String getContentLength() {
        return getSafeValue(HeaderNames.CONTENT_LENGTH);
    }

    public Headers setContentLength(String content_length) {
        return setHeader(HeaderNames.CONTENT_LENGTH, content_length);
    }

    public String getContentLocation() {
        return getSafeValue(HeaderNames.CONTENT_LOCATION);
    }

    public Headers setContentLocation(String content_location) {
        return setHeader(HeaderNames.CONTENT_LOCATION, content_location);
    }

    public String getContentMd5() {
        return getSafeValue(HeaderNames.CONTENT_MD5);
    }

    public Headers setContentMd5(String content_md5) {
        return setHeader(HeaderNames.CONTENT_MD5, content_md5);
    }

    public String getContentRange() {
        return getSafeValue(HeaderNames.CONTENT_RANGE);
    }

    public Headers setContentRange(String content_range) {
        return setHeader(HeaderNames.CONTENT_RANGE, content_range);
    }

    public String getContentType() {
        return getSafeValue(HeaderNames.CONTENT_TYPE);
    }

    public Headers setContentType(String type) {
        return setHeader(HeaderNames.CONTENT_TYPE, type);
    }

    public Headers setContentType(ContentType type) {
        return setContentType(type.getCode());
    }

    public String getDate() {
        return getSafeValue(HeaderNames.DATE);
    }

    public Headers setDate(String date) {
        return setHeader(HeaderNames.DATE, date);
    }

    public String getDav() {
        return getSafeValue(HeaderNames.DAV);
    }

    public Headers setDav(String dav) {
        return setHeader(HeaderNames.DAV, dav);
    }

    public String getDepth() {
        return getSafeValue(HeaderNames.DEPTH);
    }

    public Headers setDepth(String depth) {
        return setHeader(HeaderNames.DEPTH, depth);
    }

    public String getDestination() {
        return getSafeValue(HeaderNames.DESTINATION);
    }

    public Headers setDestination(String destination) {
        return setHeader(HeaderNames.DESTINATION, destination);
    }

    public String getEtag() {
        return getSafeValue(HeaderNames.ETAG);
    }

    public Headers setEtag(String etag) {
        return setHeader(HeaderNames.ETAG, etag);
    }

    public String getExpect() {
        return getSafeValue(HeaderNames.EXPECT);
    }

    public Headers setExpect(String expect) {
        return setHeader(HeaderNames.EXPECT, expect);
    }

    public String getExpires() {
        return getSafeValue(HeaderNames.EXPIRES);
    }

    public Headers setExpires(String expires) {
        return setHeader(HeaderNames.EXPIRES, expires);
    }

    public String getFrom() {
        return getSafeValue(HeaderNames.FROM);
    }

    public Headers setFrom(String from) {
        return setHeader(HeaderNames.FROM, from);
    }

    public String getHost() {
        return getSafeValue(HeaderNames.HOST);
    }

    public Headers setHost(String host) {
        return setHeader(HeaderNames.HOST, host);
    }

    public String getIf() {
        return getSafeValue(HeaderNames.IF);
    }

    public Headers setIf(String iff) {
        return setHeader(HeaderNames.IF, iff);
    }

    public String getIfMatch() {
        return getSafeValue(HeaderNames.IF_MATCH);
    }

    public Headers setIfMatch(String if_match) {
        return setHeader(HeaderNames.IF_MATCH, if_match);
    }

    public String getIfModifiedSince() {
        return getSafeValue(HeaderNames.IF_MODIFIED_SINCE);
    }

    public Headers setIfModifiedSince(String if_modified_since) {
        return setHeader(HeaderNames.IF_MODIFIED_SINCE, if_modified_since);
    }

    public String getIfNoneMatch() {
        return getSafeValue(HeaderNames.IF_NONE_MATCH);
    }

    public Headers setIfNoneMatch(String if_none_match) {
        return setHeader(HeaderNames.IF_NONE_MATCH, if_none_match);
    }

    public String getIfRange() {
        return getSafeValue(HeaderNames.IF_RANGE);
    }

    public Headers setIfRange(String if_range) {
        return setHeader(HeaderNames.IF_RANGE, if_range);
    }

    public String getIfUnmodifiedSince() {
        return getSafeValue(HeaderNames.IF_UNMODIFIED_SINCE);
    }

    public Headers setIfUnmodifiedSince(String if_unmodified_since) {
        return setHeader(HeaderNames.IF_UNMODIFIED_SINCE, if_unmodified_since);
    }

    public String getLastModified() {
        return getSafeValue(HeaderNames.LAST_MODIFIED);
    }

    public Headers setLastModified(String last_modified) {
        return setHeader(HeaderNames.LAST_MODIFIED, last_modified);
    }

    public String getLocation() {
        return getSafeValue(HeaderNames.LOCATION);
    }

    public Headers setLocation(String location) {
        return setHeader(HeaderNames.LOCATION, location);
    }

    public String getLockToken() {
        return getSafeValue(HeaderNames.LOCK_TOKEN);
    }

    public Headers setLockToken(String lock_token) {
        return setHeader(HeaderNames.LOCK_TOKEN, lock_token);
    }

    public String getMaxForwards() {
        return getSafeValue(HeaderNames.MAX_FORWARDS);
    }

    public Headers setMaxForwards(String max_forwards) {
        return setHeader(HeaderNames.MAX_FORWARDS, max_forwards);
    }

    public String getOverwrite() {
        return getSafeValue(HeaderNames.OVERWRITE);
    }

    public Headers setOverwrite(String overwrite) {
        return setHeader(HeaderNames.OVERWRITE, overwrite);
    }

    public String getPragma() {
        return getSafeValue(HeaderNames.PRAGMA);
    }

    public Headers setPragma(String pragma) {
        return setHeader(HeaderNames.PRAGMA, pragma);
    }

    public String getProxyAuthenticate() {
        return getSafeValue(HeaderNames.PROXY_AUTHENTICATE);
    }

    public Headers setProxyAuthenticate(String proxy_authenticate) {
        return setHeader(HeaderNames.PROXY_AUTHENTICATE, proxy_authenticate);
    }

    public String getProxyAuthorization() {
        return getSafeValue(HeaderNames.PROXY_AUTHORIZATION);
    }

    public Headers setProxyAuthorization(String proxy_authorization) {
        return setHeader(HeaderNames.PROXY_AUTHORIZATION, proxy_authorization);
    }

    public String getRange() {
        return getSafeValue(HeaderNames.RANGE);
    }

    public Headers setRange(String range) {
        return setHeader(HeaderNames.RANGE, range);
    }

    public String getReferer() {
        return getSafeValue(HeaderNames.REFERER);
    }

    public Headers setReferer(String referer) {
        return setHeader(HeaderNames.REFERER, referer);
    }

    public String getRetryAfter() {
        return getSafeValue(HeaderNames.RETRY_AFTER);
    }

    public Headers setRetryAfter(String retry_after) {
        return setHeader(HeaderNames.RETRY_AFTER, retry_after);
    }

    public String getServer() {
        return getSafeValue(HeaderNames.SERVER);
    }

    public Headers setServer(String server) {
        return setHeader(HeaderNames.SERVER, server);
    }

    public String getStatusUri() {
        return getSafeValue(HeaderNames.STATUS_URI);
    }

    public Headers setStatusUri(String status_uri) {
        return setHeader(HeaderNames.STATUS_URI, status_uri);
    }

    public String getTe() {
        return getSafeValue(HeaderNames.TE);
    }

    public Headers setTe(String te) {
        return setHeader(HeaderNames.TE, te);
    }

    public String getTimeout() {
        return getSafeValue(HeaderNames.TIMEOUT);
    }

    public Headers setTimeout(String timeout) {
        return setHeader(HeaderNames.TIMEOUT, timeout);
    }

    public String getTrailer() {
        return getSafeValue(HeaderNames.TRAILER);
    }

    public Headers setTrailer(String trailer) {
        return setHeader(HeaderNames.TRAILER, trailer);
    }

    public String getTransferEncoding() {
        return getSafeValue(HeaderNames.TRANSFER_ENCODING);
    }

    public Headers setTransferEncoding(String transfer_encoding) {
        return setHeader(HeaderNames.TRANSFER_ENCODING, transfer_encoding);
    }

    public String getUpgrade() {
        return getSafeValue(HeaderNames.UPGRADE);
    }

    public Headers setUpgrade(String upgrade) {
        return setHeader(HeaderNames.UPGRADE, upgrade);
    }

    public String getUserAgent() {
        return getSafeValue(HeaderNames.USER_AGENT);
    }

    public Headers setUserAgent(String userAgent) {
        return setHeader(HeaderNames.USER_AGENT, userAgent);
    }

    public String getVary() {
        return getSafeValue(HeaderNames.VARY);
    }

    public Headers setVary(String vary) {
        return setHeader(HeaderNames.VARY, vary);
    }

    public String getVia() {
        return getSafeValue(HeaderNames.VIA);
    }

    public Headers setVia(String via) {
        return setHeader(HeaderNames.VIA, via);
    }

    public String getWarning() {
        return getSafeValue(HeaderNames.WARNING);
    }

    public Headers setWarning(String warning) {
        return setHeader(HeaderNames.WARNING, warning);
    }

    public String getWwwAuthenticate() {
        return getSafeValue(HeaderNames.WWW_AUTHENTICATE);
    }

    public Headers setWwwAuthenticate(String www_authenticate) {
        return setHeader(HeaderNames.WWW_AUTHENTICATE, www_authenticate);
    }

    public Set<String> headerKeys() {
        return headers.keySet();
    }

    public Header getHeader(String key) {
        return this.headers.get(key);
    }

    /**
     * Gets the header value safely (if the header does not exist returns empty string)
     * @param key the key to get the header value for
     * @return the header value
     */
    public String getSafeValue(String key) {
        Header header = getHeader(key);
        if (header == null) {
            return "";
        }

        return header.getValue();
    }

    /**
     * Converts this Headers instance to a RequestConfig passable instance
     * @return the request instance
     */
    public RequestConfig toConfig() {
        return new RequestConfig()
                .setRequestHeaders(this);
    }

    public Headers setAuthentication(String authentication) {
        return setHeader(HeaderNames.AUTHORIZATION, authentication);
    }

    /**
     * Sets the header value directly
     * @param key header key name
     * @param value header value string
     * @return Headers instance for setter chaining
     */
    public Headers setHeader(String key, String value) {
        headers.put(key, new SimpleHeader(key, value));
        return this;
    }

    /**
     * Sets the header to a supplier value evaluated when the request is made
     * @param key they header key to set
     * @param supplier the supplier value to set the header to
     * @return Headers instance for setter chaining
     */
    public Headers setHeader(String key, Supplier<String> supplier) {
        headers.put(key, new SupplierHeader(key, supplier));
        return this;
    }

    @Override
    public String toString() {
        Collection<Header> headers = this.headers.values();
        return "RequestHeaders{" + "headers=" + headers +
                '}';
    }

    public enum ContentType {
        APPLICATION_ATOM_XML("application/atom+xml", StandardCharsets.ISO_8859_1),
        APPLICATION_FORM_URL_ENCODED("application/x-www-form-urlencoded", StandardCharsets.ISO_8859_1),
        APPLICATION_JSON("application/json", StandardCharsets.UTF_8),
        IMAGE_BMP("image/bmp"),
        IMAGE_GIF("image/gif"),

        TEXT("text/plain"),
        WILDCARD("*/*");

        private final String code;
        private Charset charset = StandardCharsets.UTF_8;

        ContentType(String code) {
            this.code = code;
        }

        ContentType(String code, Charset charset) {
            this.charset = charset;
            this.code = code;
        }

        public Charset getCharset() {
            return charset;
        }

        public String getCode() {
            return code;
        }
    }

    private static class SupplierHeader implements Header {
        private final String key;
        private final Supplier<String> supplier;

        public SupplierHeader(String key, Supplier<String> supplier) {
            this.key = key;
            this.supplier = supplier;
        }

        @Override
        public String getName() {
            return key;
        }

        @Override
        public String getValue() {
            return supplier.get();
        }
    }

    private static class SimpleHeader implements Header {
        private final String key;
        private final String value;

        public SimpleHeader(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getName() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("%s=%s", getName(), getValue());
        }
    }
}
