/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package utils;

import java.net.http.HttpRequest;

@FunctionalInterface
public interface HttpRequestModifier {

	HttpRequest.Builder modifyRequest(HttpRequest.Builder starter);
}
