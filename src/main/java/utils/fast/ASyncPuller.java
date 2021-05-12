/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package utils.fast;

import java.util.List;

public class ASyncPuller {

	private static Runnable runnable;

	public static void asyncPull(List<?> source, List<?> returnList, Runnable runnable) {
		ASyncPuller.runnable = runnable;
		int index = 0; // The index of the stream to start from
		int[] list = splitIntoParts(source.size(), 15); // A list of integers used to separate the stream
		int listIndex = 0; // The index in the integer array we are currently on

		while (index < source.size()) { // Until we have completely looped through the stream
			int i = list[listIndex];
			new MemberThread(source, source.subList(index, index + i)).start();

			index += i;

			listIndex++;
		}
	}

	private static class MemberThread extends Thread {

		public MemberThread(List<?> source, List<?> list) {

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
