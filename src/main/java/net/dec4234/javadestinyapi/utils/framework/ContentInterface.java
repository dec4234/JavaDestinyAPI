/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils.framework;

import net.dec4234.javadestinyapi.exceptions.APIException;

public interface ContentInterface {

	/**
	 * Used to verify if the raw JsonObject has been initialized
	 * Initialize the JsonObject from here if it is not initialized
	 */
	void checkJO() throws APIException;
}
