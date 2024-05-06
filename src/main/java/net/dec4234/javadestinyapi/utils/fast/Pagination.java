/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils.fast;

import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * Paginate through an indexed portion of the API. This is usually used wherever you search for something, and could get
 * a large list of items in return. Paging through it allows you to potentially save on memory + time, since you could
 * find what you want early on and terminate early.
 * <br>
 * Implementation Note: See an example in {@link net.dec4234.javadestinyapi.material.DestinyAPI#searchUsers(String)}
 * @param <T> The type that EACH page will return. For example, each page could return a List of users, which would need
 *           to be combined into one large list if you wanted to look at all of them.
 */
public abstract class Pagination<T> implements Iterator<T>, Iterable<T> {

	protected T currentResponse;
	protected boolean hasGrabbed;

	public Pagination() {

	}

	public Pagination(final T currentResponse) {
		this.currentResponse = currentResponse;
	}

	/**
	 * Does the pagination object have another page to look at?
	 * <br>
	 * Implementation Note: This should make a request to the API iff there isn't an unread request inside. It then
	 * checks if the response is a new valid page, then updates currentResponse and hasGrabbed appropriately.
	 * @return True if there is another valid page of objects ready
	 * @throws APIException If something goes wrong in the request to the API
	 */
	public abstract boolean hasNext() throws APIException;

	/**
	 * Returns the next page of objects. Grabs a new set of objects (page) if the current one has already been used.
	 * @return The next page of objects, or null if you reach the end
	 * @throws APIException If something goes wrong in the request to the API
	 */
	public abstract T next() throws APIException;

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return this;
	}
}
