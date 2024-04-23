/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
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
