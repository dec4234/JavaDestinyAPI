/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils;

import java.net.http.HttpRequest;

@FunctionalInterface
public interface HttpRequestModifier {

	HttpRequest.Builder modifyRequest(HttpRequest.Builder starter);
}
