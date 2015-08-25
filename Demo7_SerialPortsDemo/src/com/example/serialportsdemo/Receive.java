package com.example.serialportsdemo;

public abstract class Receive {
	protected abstract void onDataReceived(final byte[] buffer, final int size);
}
