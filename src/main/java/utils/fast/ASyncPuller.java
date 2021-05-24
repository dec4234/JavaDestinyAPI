/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package utils.fast;

import java.util.ArrayList;
import java.util.List;

public class ASyncPuller {

	/**
	 * Split the stream list into multiple parts, handle using the asyncpull consumer method
	 * Useful for accelerating processing of large or taxing operations
	 *
	 * @param stream The source of info to be split
	 * @param partsToSplitInto The amount of threads to be created, the stream list will be split as much as possible based on this
	 * @param aSyncPull What you want to happen to the info when in its in an ASync thread
	 */
	public static List<Object> asyncPull(List<Object> stream, int partsToSplitInto, ASyncPull aSyncPull) {
		int index = 0; // The index of the stream to start from
		int[] parts = splitIntoParts(stream.size(), partsToSplitInto); // A list of integers used to separate the stream
		int listIndex = 0; // The index in the integer array we are currently on
		List<Object> returnList = new ArrayList<>();

		while (index < stream.size()) { // Until we have completely looped through the stream
			int i = parts[listIndex];
			new MemberThread(returnList, stream.subList(index, index + i), aSyncPull).start();

			index += i;

			listIndex++;
		}

		return returnList;
	}

	private static class MemberThread extends Thread {

		public MemberThread(List<Object> source, List<?> stream, ASyncPull aSyncPull) {
			source.addAll(aSyncPull.run(stream));
		}

	}

	private static int[] splitIntoParts(int whole, int parts) {
		int[] arr = new int[parts];
		int remain = whole;
		int partsLeft = parts;
		for (int i = 0; partsLeft > 0; i++) {
			int size = (remain + partsLeft - 1) / partsLeft; // rounded up, aka ceiling
			arr[i] = size;
			remain -= size;
			partsLeft--;
		}
		return arr;
	}
}
