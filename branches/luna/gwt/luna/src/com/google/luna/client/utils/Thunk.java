// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.utils;

import com.google.luna.client.Luna;

public abstract class Thunk<T> implements IThunk<T> {

	@Override
	public void onError(Throwable error) {
		Luna.reportError(error);
	}

}
