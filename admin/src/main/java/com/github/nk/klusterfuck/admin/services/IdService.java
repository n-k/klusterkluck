package com.github.nk.klusterfuck.admin.services;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by nipunkumar on 28/05/17.
 */
public class IdService {

	public String newId() {
		UUID uuid = UUID.randomUUID();
		long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
		return toString(l);
	}

	private static String toString(long i) {
		int radix = 26;
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);

		if (!negative) {
			i = -i;
		}

		while (i <= -radix) {
			buf[charPos--] = digits[(int) (-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative) {
			buf[--charPos] = '-';
		}

		return new String(buf, charPos, (65 - charPos));
	}

	final static char[] digits = {
			'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z'
	};
}
